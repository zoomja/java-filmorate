package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserWithAllValidField() {
        User user = User.builder()
                .id(1)
                .email("example@gmail.com")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Пользователь должен быть валидным");
    }

    @Test
    public void testUserWithEmptyEmail() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email не может быть пустым");
    }

    @Test
    public void testUserWithInvalidEmail() {
        User user = User.builder()
                .id(1)
                .email("examplegmail.com")
                .login("Login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email должен содержать символ @");
    }

    @Test
    public void testUserWithEmptyLogin() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Логин не может быть пустым");
    }

    @Test
    public void testUserHaveFutureBirthday() {
        User user = User.builder()
                .id(1)
                .email("")
                .login("Login")
                .name("name")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "День рождение пользователя не может быть в будущем");
    }
}