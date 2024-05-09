package ru.practicum.user.service;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.TotalUserDto;
import ru.practicum.user.model.dto.UserDto;

@UtilityClass
public class UserMapper {

    public User toUser(TotalUserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getName())
                .build();
    }

    public TotalUserDto toTotalDto(User user) {
        return TotalUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
