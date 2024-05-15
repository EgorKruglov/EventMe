package ru.practicum.user.service;

import ru.practicum.user.model.dto.TotalUserDto;

import java.util.List;

public interface UserService {
    TotalUserDto addUser(TotalUserDto user);

    void deleteUser(Long userId);

    List<TotalUserDto> getUsers(List<Long> idList, int from, int size);
}
