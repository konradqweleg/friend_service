package com.example.friends_service.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("friends")
public record  Friend(@Id Long idFriends,Long idFirstFriend,Long idSecondFriend) {
}
