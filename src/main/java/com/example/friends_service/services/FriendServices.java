package com.example.friends_service.services;

import com.example.friends_service.exceptions.FriendRelationAlreadyExists;
import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.IsFriendsDto;
import com.example.friends_service.exceptions.UserNotFoundException;

import com.example.friends_service.exceptions.UserToCreateRelationNotFoundException;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.port.in.FriendPort;
import com.example.friends_service.port.out.services.UserServicePort;
import com.example.friends_service.port.out.persistence.PersistencePort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FriendServices implements FriendPort {
    private final PersistencePort persistencePort;
    private final UserServicePort userServicePort;
    private final Logger logger = LogManager.getLogger(FriendServices.class);

    public FriendServices(PersistencePort persistencePort, UserServicePort userServicePort) {
        this.persistencePort = persistencePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public Mono<IsFriendsDto> isFriends(FriendRelationDto friendsRelationToFind) {
        return persistencePort.findFriendsRelation(friendsRelationToFind)
                .map(friend -> new IsFriendsDto(true))
                .defaultIfEmpty(new IsFriendsDto(false));
    }


    @Override
    public Mono<Void> createFriends(FriendRelationDto friendRelationToCreate) {
        IdUserDto idFirstFriend = new IdUserDto(friendRelationToCreate.idFirstFriend());
        IdUserDto idSecondFriend = new IdUserDto(friendRelationToCreate.idSecondFriend());

        return userServicePort.getUserAboutId(idFirstFriend)
                .flatMap(userFirst -> userServicePort.getUserAboutId(idSecondFriend)
                        .flatMap(userSecond -> persistencePort.findFriendsRelation(friendRelationToCreate)
                                .flatMap(existingRelation -> {
                                    logger.error("Friend relation already exists: {} and {}", idFirstFriend.idUser(), idSecondFriend.idUser());
                                    return Mono.error(new FriendRelationAlreadyExists("Friend relation already exists"));
                                })
                                .switchIfEmpty(
                                        Mono.defer(() -> {
                                            logger.info("Creating new friend relation: {} and {}", idFirstFriend.idUser(), idSecondFriend.idUser());
                                            return persistencePort.createFriend(friendRelationToCreate).then();
                                        })
                                )

                        )
                        .onErrorMap(UserNotFoundException.class, e -> {
                            logger.error("Second user in relation not found: {}", e.getMessage());
                            return new UserToCreateRelationNotFoundException("Second user in relation not found");
                        })
                )
                .onErrorMap(UserNotFoundException.class, e -> {
                    logger.error("First user in relation not found: {}", e.getMessage());
                    return new UserToCreateRelationNotFoundException("First user in relation not found");
                })
                .then();
    }


    private boolean isTheSameUser(IdUserDto idUserDtoToFindFriends, Long idFriend) {
        return idUserDtoToFindFriends.idUser().equals(idFriend);
    }

    @Override
    public Flux<UserDataDto> getFriends(IdUserDto idUserDtoToFindFriends) {
        return userServicePort.getUserAboutId(idUserDtoToFindFriends)
                .thenMany(persistencePort.findUserFriends(idUserDtoToFindFriends))
                .flatMap(friendRelation -> {
                    IdUserDto friendId;
                    if (isTheSameUser(idUserDtoToFindFriends, friendRelation.idFirstFriend())) {
                        friendId = new IdUserDto(friendRelation.idSecondFriend());
                    } else {
                        friendId = new IdUserDto(friendRelation.idFirstFriend());
                    }

                    return userServicePort.getUserAboutId(friendId)
                            .map(user -> new UserDataDto(friendId.idUser(), user.name(), user.surname(), user.email()));
                });
    }

}
