package com.example.friends_service.integration.mocks;

import com.example.friends_service.entity.request.IdUserData;
import com.example.friends_service.entity.request.UserData;
import com.example.friends_service.entity.response.Result;
import com.example.friends_service.port.in.UserServicePort;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

public class UserServicePortMock {

    private final Long idFirstUser = 1L;
    private final Long idSecondUser = 2L;
    private final Long idThirdUser = 3L;

    public void mockGetUserAboutIdForUserAboutIdOneAndTwo(UserServicePort userServicePort) {
        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 1L) {
                return Mono.just(Result.success(new UserData(idFirstUser, "User1", "User1", "User1")));
            }
            return Mono.just(Result.success(new UserData(idSecondUser, "User2", "User2", "User2")));
        });
    }

    public void mockGetUserAboutIdForUserAboutIdOneOtherNoExists(UserServicePort userServicePort) {
        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == 1L) {
                return Mono.just(Result.success(new UserData(idFirstUser, "User1", "User1", "User1")));
            }
            return Mono.just(Result.error("User not found"));
        });
    }

    public void mockUserServicePortReturnUserForUserAboutIdsOneTwoAndThree(UserServicePort userServicePort) {
        Mockito.when(userServicePort.getUserAboutId(any(Mono.class))).thenAnswer(invocation -> {
            Mono<IdUserData> monoIdUSer = invocation.getArgument(0);
            if (Objects.requireNonNull(monoIdUSer.block()).idUser() == idFirstUser) {
                return Mono.just(Result.success(new UserData(idSecondUser, "User1", "User1", "User1")));
            } else if (Objects.requireNonNull(monoIdUSer.block()).idUser() == idSecondUser) {
                return Mono.just(Result.success(new UserData(idSecondUser, "User2", "User2", "User2")));
            } else {
                return Mono.just(Result.success(new UserData(idThirdUser, "User3", "User3", "User3")));
            }
        });
    }
}
