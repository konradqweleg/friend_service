package com.example.friends_service.adapter.in.rest;

import com.example.friends_service.adapter.in.rest.util.ResponseUtil;
import com.example.friends_service.model.api_models.IdUserDto;
import com.example.friends_service.model.api_models.UserDataDto;
import com.example.friends_service.model.api_models.IsFriendsDto;
import com.example.friends_service.model.api_models.FriendRelationDto;
import com.example.friends_service.port.in.FriendPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/friends")
public class FriendsController {
    private final FriendPort friendsPort;

    public FriendsController(FriendPort friendsPort) {
        this.friendsPort = friendsPort;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> createFriends(@RequestBody @Valid FriendRelationDto friendRelationDto) {
        return ResponseUtil.toResponseEntity(friendsPort.createFriends(friendRelationDto), HttpStatus.OK);
    }

    @GetMapping("/{firstFriendId}/is-friend-with/{secondFriendId}")
    public Mono<ResponseEntity<IsFriendsDto>> isFriends(
            @PathVariable("firstFriendId") @Valid Long idFirstFriend,
            @PathVariable("secondFriendId") @Valid Long idSecondFriend) {
        return ResponseUtil.toResponseEntity(friendsPort.isFriends(new FriendRelationDto(idFirstFriend, idSecondFriend)), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<List<UserDataDto>>> getFriends(@PathVariable("userId") @Valid Long idUser) {
        return ResponseUtil.toResponseEntity(friendsPort.getFriends(new IdUserDto(idUser)), HttpStatus.OK);
    }
}
