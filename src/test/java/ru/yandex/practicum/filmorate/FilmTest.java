package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;
    private static final LocalDate VALID_RELEASE_DATE = LocalDate.of(1995, 7, 15);

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFilmWithNullReleaseDate() {
        Film film = Film.builder()
                .id(1)
                .name("Название")
                .description("Описание")
                .releaseDate(null)
                .duration(80)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Дата релиза не может быть null");
    }

    @Test
    public void testFilmWithInvalidName() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Описание")
                .releaseDate(VALID_RELEASE_DATE)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Название не должно быть пустым");
    }

    @Test
    public void testFilmWithInvalidDescription() {
        Film film = Film.builder()
                .id(1)
                .name("Название")
                .description("")
                .releaseDate(VALID_RELEASE_DATE)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Описание не должно быть пустым");
    }

    @Test
    public void testFilmReleaseDateInFuture() {
        Film film = Film.builder()
                .id(1)
                .name("Название")
                .description("Описание")
                .releaseDate(LocalDate.now().plusDays(1))
                .duration(80)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Дата релиза не может быть в будущем");
    }

    @Test
    public void testFilmWithValidFields() {
        Film film = Film.builder()
                .id(1)
                .name("Название")
                .description("Описание")
                .releaseDate(VALID_RELEASE_DATE)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Фильм должен быть валидным");
    }

    @Test
    public void testFilmWithMaxDescriptionLength() {
        Film film = Film.builder()
                .id(1)
                .name("Описание")
                .description("o".repeat(200))
                .releaseDate(VALID_RELEASE_DATE)
                .duration(120)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Описание фильма должно содержать 200 символов");

        film.setDescription("o".repeat(201));
        violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Описание фильма не может быть больше 200");
    }

    @Test
    public void testFilmWithNegativeDuration() {
        Film film = Film.builder()
                .id(1)
                .name("Название")
                .description("valid description")
                .releaseDate(VALID_RELEASE_DATE)
                .duration(-50)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Продолжительность должна быть позитивным числом");
    }
}