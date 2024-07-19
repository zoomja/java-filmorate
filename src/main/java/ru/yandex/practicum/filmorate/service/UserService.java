package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public User addFriends(long id, long friendID) {
        checkUserExists(id);
        checkUserExists(friendID);

        User user = inMemoryUserStorage.findById(id);
        user.getFriends().add(friendID);

        User friend = inMemoryUserStorage.findById(friendID);
        friend.getFriends().add(id);

        inMemoryUserStorage.putUser(user);
        inMemoryUserStorage.putUser(friend);

        return friend;
    }

    public Set<Long> deleteFriends(long id, long friendID) {
        checkUserExists(id);
        checkUserExists(friendID);
        User user = inMemoryUserStorage.findById(id);
        if (!user.getFriends().contains(friendID)) {
            return user.getFriends();
        }
        user.getFriends().remove(friendID);
        User friend = inMemoryUserStorage.findById(friendID);
        friend.getFriends().remove(id);

        inMemoryUserStorage.putUser(user);
        inMemoryUserStorage.putUser(friend);

        return user.getFriends();
    }

    public List<User> listCommonFriends (long id, long secondId) {
        checkUserExists(id);
        checkUserExists(secondId);

        User user = inMemoryUserStorage.findById(id);
        User secondUser = inMemoryUserStorage.findById(secondId);
        List<User> listCommonFriends = new ArrayList<>();
        for (Long firstId : user.getFriends()) {
            if (secondUser.getFriends().contains(firstId)) {
                listCommonFriends.add(inMemoryUserStorage.findById(firstId));
            }
        }
        return listCommonFriends;
    }

    public List<User> listFriends(long id) {
        checkUserExists(id);
        User user = inMemoryUserStorage.findById(id);
        List<User> listFriends = new ArrayList<>();
        if (user.getFriends() == null) {
            return listFriends;
        }
        for (Long firstId : user.getFriends()) {
            listFriends.add(inMemoryUserStorage.findById(firstId));
        }
        return listFriends;
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void delete(long id) {
        inMemoryUserStorage.delete(id);
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User putUser(User user) {
        return inMemoryUserStorage.putUser(user);
    }

    private void checkUserExists(long id) {
        if (inMemoryUserStorage.findById(id) == null) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
    }
}
