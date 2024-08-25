package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users ";
    private static final String GET_USERS_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    private static final String REMOVE_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String GET_FRIENDS_BY_USER_ID = "SELECT friend_id FROM friends WHERE user_id = ?";


    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc).withTableName("users").usingGeneratedKeyColumns("id");
        Number id = insert.executeAndReturnKey(convertUserToMap(user));
        user.setId(id.longValue());
        user.setFriends(new HashSet<>());
        log.info("Добавлен пользователь с id {}", id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == 0) {
            throw new ValidationException("ID не найдено");
        }

        log.info("Обновление пользователя с id {}: email={}, login={}, name={}, birthday={}",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        int lines = jdbc.update(UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (lines != 0) {
            return user;
        }
        log.error("Не удалось обновить пользователя с id {}", user.getId());
        throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
    }


    @Override
    public Collection<User> getAllUsers() {
        return jdbc.query(GET_ALL_USERS, mapper);
    }

    @Override
    public void delete(int id) {
        int rows = jdbc.update(DELETE_USER, id);
        if (rows != 1) throw new NotFoundException("User with id = " + id + " not found");
    }


    public User getUserById(int userId) {
        try {
            return jdbc.queryForObject(GET_USERS_BY_ID, mapper, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with id = " + userId + " not found");
        }
    }

    public void addFriends(int id, int friendID) {
        getUserById(id);
        getUserById(friendID);
        jdbc.update(ADD_FRIEND, id, friendID);
    }

    public void removeFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        jdbc.update(REMOVE_FRIEND, userId, friendId);
    }

    public Set<Integer> getFriendsByUserId(int userId) {
        return new HashSet<>(jdbc.queryForList(GET_FRIENDS_BY_USER_ID, Integer.class, userId));
    }

    private Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("email", user.getEmail());
        result.put("login", user.getLogin());
        result.put("name", user.getName());
        result.put("birthday", user.getBirthday());
        return result;
    }
}
