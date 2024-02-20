package com.example.friends_service.adapter.in.rest;

import com.example.friends_service.adapter.in.rest.util.ConvertToJSON;
import com.example.friends_service.entity.request.FriendData;
import com.example.friends_service.port.in.FriendPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/friendsService/api/v1/friends")
public class FriendsController {

    private final FriendPort friendsPort;

    public FriendsController(FriendPort friendsPort) {
        this.friendsPort = friendsPort;
    }

    @PostMapping("/createFriends")
    public Mono<ResponseEntity<String>> createFriends(@RequestBody @Valid Mono<FriendData> friendsIdsMono) {
        return friendsPort.createFriends(friendsIdsMono).flatMap(ConvertToJSON::convert);
    }

    @PostMapping("/isFriends")
    public Mono<ResponseEntity<String>> isFriends(@RequestBody @Valid Mono<FriendData> friendsIdsMono) {
        return friendsPort.isFriends(friendsIdsMono).flatMap(ConvertToJSON::convert);
    }


}
