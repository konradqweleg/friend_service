package com.example.friends_service.model.mapper;

import com.example.friends_service.model.db.Friend;
import com.example.friends_service.model.api_models.FriendRelationDto;
import org.springframework.stereotype.Service;

@Service
public class FriendMapper {
    public  FriendRelationDto toDto(Friend friend) {
        return new FriendRelationDto(friend.idFirstFriend(), friend.idSecondFriend());
    }

    public  Friend toEntity(FriendRelationDto dto) {
        return new Friend(null, dto.idFirstFriend(), dto.idSecondFriend());
    }
}