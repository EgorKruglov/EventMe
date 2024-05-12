package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.TotalUserDto;
import ru.practicum.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public TotalUserDto addUser(TotalUserDto user) {
        log.info("Добавление пользователя");
        if (userRepository.isEmailExist(user.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        TotalUserDto result = UserMapper.toTotalDto(userRepository.save(UserMapper.toUser(user)));
        return result;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя id:{}", userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public List<TotalUserDto> getUsers(List<Long> idList, int from, int size) {
        log.info("Запрос на получение списка пользователей");
        Pageable pageable = PageRequest.of(from, size, Sort.Direction.ASC, "id");
        List<User> users;
        if (idList.isEmpty()) {
            users = userRepository.findAllUser(pageable);
        } else {
            users = userRepository.findAllById(idList, pageable);
        }
        if (users.isEmpty()) {
            return List.of();
        }
        List<TotalUserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(UserMapper.toTotalDto(user));
        }
        return usersDto;
    }
}
