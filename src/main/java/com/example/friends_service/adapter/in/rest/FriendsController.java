package com.example.friends_service.adapter.in.rest;

import com.example.friends_service.adapter.in.rest.util.ConvertToJSON;
import com.example.friends_service.entity.request.FriendsIdsData;
import com.example.friends_service.entity.request.IdUser;
import com.example.friends_service.port.in.FriendPort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/friendsService/api/v1/friends")
public class FriendsController {

    private final FriendPort friendsPort;

    public FriendsController(FriendPort friendsPort) {
        this.friendsPort = friendsPort;
    }

    @PostMapping("/createFriends")
    public Mono<ResponseEntity<String>> createFriends(@RequestBody @Valid Mono<FriendsIdsData> friendsIdsMono) {
        return friendsPort.createFriends(friendsIdsMono).flatMap(ConvertToJSON::convert);
    }

    @GetMapping("/isFriends")
    public Mono<ResponseEntity<String>> isFriends(@RequestParam("friendFirstId") @Valid Long idFirstFriend, @RequestParam("friendSecondId") @Valid Long idSecondFriend) {
        return friendsPort.isFriends(Mono.just(new FriendsIdsData(idFirstFriend,idSecondFriend))).flatMap(ConvertToJSON::convert);
    }

    @GetMapping("/getFriends")
    public Mono<ResponseEntity<String>> getFriends(@RequestParam("idUser") @Valid Long idUser) {
        return ConvertToJSON.convert(friendsPort.getFriends(Mono.just(new IdUser(idUser))));

    }

}
