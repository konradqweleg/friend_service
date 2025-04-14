package com.example.friends_service.adapter.out.persistence;

import com.example.friends_service.model.api_models.IdUserDto;

import com.example.friends_service.exceptions.RepositoryException;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.model.mapper.FriendMapper;
import com.example.friends_service.port.out.persistence.PersistencePort;
import com.example.friends_service.repository.FriendsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostgresFriendRepository implements PersistencePort {
    private final FriendsRepository friendsRepository;
    private final FriendMapper friendMapper;

    private final Logger logger = LogManager.getLogger(PostgresFriendRepository.class);

    public PostgresFriendRepository(FriendsRepository friendsRepository, FriendMapper friendMapper) {
        this.friendsRepository = friendsRepository;
        this.friendMapper = friendMapper;
    }

    @Override
    public Mono<FriendRelationDto> createFriend(FriendRelationDto friendRelationToCreate) {
        return Mono.fromSupplier(() -> friendMapper.toEntity(friendRelationToCreate))
                .flatMap(friendsRepository::save)
                .map(friendMapper::toDto)
                .onErrorResume(e -> {
                    logger.error("Error creating friend relation: {}", e.getMessage());
                    return Mono.error(new RepositoryException("Error creating friend relation"));
                });
    }


    @Override
    public Mono<FriendRelationDto> findFriendsRelation(FriendRelationDto friendRelationToFind) {
        return friendsRepository.findFriends(friendRelationToFind.idFirstFriend(), friendRelationToFind.idSecondFriend())
                .map(friendMapper::toDto)
                .onErrorResume(e -> {
                    logger.error("Error finding friend relation: {}", e.getMessage());
                    return Mono.error(new RepositoryException("Error finding friend relation"));
                });
    }

    @Override
    public Flux<FriendRelationDto> findUserFriends(IdUserDto idUserDto) {
        return friendsRepository.findFriendsUser(idUserDto.idUser())
                .map(friendMapper::toDto)
                .onErrorResume(e -> {
                    logger.error("Error finding user friends: {}", e.getMessage());
                    return Flux.error(new RepositoryException("Error finding user friends"));
                });
    }

}
