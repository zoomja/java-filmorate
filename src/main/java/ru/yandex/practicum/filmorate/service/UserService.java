package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public User addFriends(int id, int friendID) {
        if (id == friendID)
            throw new ValidationException("Невозможно добавить себя в друзья");
        userDbStorage.addFriends(id, friendID);
        return userDbStorage.getUserById(id);
    }

    public void deleteFriends(int id, int friendID) {
        userDbStorage.removeFriend(id, friendID);
    }

    public List<User> listCommonFriends(int id, int secondId) {


        Set<Integer> userFriends = userDbStorage.getFriendsByUserId(id);
        Set<Integer> secondUserFriends = userDbStorage.getFriendsByUserId(secondId);

        return userFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userDbStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> listFriends(int id) {
        userDbStorage.getUserById(id);
        Set<Integer> friendIds = userDbStorage.getFriendsByUserId(id);
        return friendIds.stream()
                .map(userDbStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public void delete(int id) {
        userDbStorage.delete(id);
    }

    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }
}