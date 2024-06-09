package com.example.friends_service.integration.mocks;

import com.example.friends_service.entity.request.IdUserData;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.port.out.services.UserServicePort;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

public class UserServicePortMock {

    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;
    private final Long idThirdUser = 3L;

//    public void mockGetUserAboutIdForUserAboutIdOneAndTwo(UserServicePort userServicePort) {
//        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
//            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
//            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 1L) {
//                return Mono.just(Result.success(new UserData(idFirstUser, "User1", "User1", "User1")));
//            }
//            return Mono.just(Result.success(new UserData(idSecondUser, "User2", "User2", "User2")));
//        });
//    }

    public void mockGetUserAboutIdForUserAboutIdOneOtherNoExists(UserServicePort userServicePort) {
        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 1L) {
                return Mono.just(Result.success(new UserData(idFirstUser, "User1", "User1", "User1")));
            }
            return Mono.just(Result.error("User not found"));
        });
    }

    public void mockGetUserById(UserServicePort userServicePort) {
        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            Long idCreatedUser = Objects.requireNonNull(monoIdUSer.block()).idUser();
            String userDataPlaceHolder = "User" + idCreatedUser;
            return Mono.just(Result.success(new UserData(idCreatedUser, userDataPlaceHolder, userDataPlaceHolder, userDataPlaceHolder)));

        });
    }
}
