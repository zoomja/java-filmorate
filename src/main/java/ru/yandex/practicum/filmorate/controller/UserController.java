package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == 0) {
            throw new ValidationException("id отсутствует");
        }

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        User updatedUser = users.get(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setBirthday(user.getBirthday());
        log.info("Пользователь {} был обновлён", updatedUser.getId());
        return updatedUser;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        user.setName(user.getName());
        users.put(user.getId(), user);
        log.info("Пользователь с id {} был создан", user.getId());
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Получение списка всех пользователей");
        return users.values();
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
