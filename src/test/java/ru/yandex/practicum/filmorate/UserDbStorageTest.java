package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
        jdbcTemplate.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, email VARCHAR(255), login VARCHAR(255), name VARCHAR(255), birthday DATE)");
        jdbcTemplate.execute("DROP TABLE IF EXISTS friends");
        jdbcTemplate.execute("CREATE TABLE friends (user_id BIGINT, friend_id BIGINT, PRIMARY KEY (user_id, friend_id))");
    }


    @Test
    void updateUser() {
        User user = new User(null, "Test User", "test@example.com", "testuser", LocalDate.of(2000, 1, 1), new HashSet<>());
        User createdUser = userDbStorage.addUser(user);

        createdUser.setName("Updated User");
        User updatedUser = userDbStorage.updateUser(createdUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated User");
    }

    @Test
    void deleteUser() {
        User user = new User(null, "Test User", "test@example.com", "testuser", LocalDate.of(2000, 1, 1), new HashSet<>());
        User createdUser = userDbStorage.addUser(user);
        userDbStorage.delete(createdUser.getId().intValue());

        assertThatThrownBy(() -> userDbStorage.getUserById(createdUser.getId().intValue()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addAndRemoveFriends() {
        User user1 = new User(null, "User 1", "user1@example.com", "user1", LocalDate.of(2000, 1, 1), new HashSet<>());
        User user2 = new User(null, "User 2", "user2@example.com", "user2", LocalDate.of(2000, 1, 1), new HashSet<>());

        User createdUser1 = userDbStorage.addUser(user1);
        User createdUser2 = userDbStorage.addUser(user2);

        userDbStorage.addFriends(createdUser1.getId().intValue(), createdUser2.getId().intValue());

        assertThat(userDbStorage.getFriendsByUserId(createdUser1.getId().intValue()))
                .contains(createdUser2.getId().intValue());

        userDbStorage.removeFriend(createdUser1.getId().intValue(), createdUser2.getId().intValue());

        assertThat(userDbStorage.getFriendsByUserId(createdUser1.getId().intValue()))
                .doesNotContain(createdUser2.getId().intValue());
    }
}
