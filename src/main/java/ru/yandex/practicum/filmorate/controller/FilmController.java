package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("добавлен фильм с id= {}", film.getId());
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == 0) {
            throw new ValidationException("ID фильма отсутствует");
        }

        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        validateFilm(newFilm);
        Film updatedFilm = films.get(newFilm.getId());
        updatedFilm.setName(newFilm.getName());
        updatedFilm.setDescription(newFilm.getDescription());
        updatedFilm.setReleaseDate(newFilm.getReleaseDate());
        updatedFilm.setDuration(newFilm.getDuration());
        log.info("Фильм с id {} обновлён", newFilm.getId());
        return updatedFilm;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return films.values();
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Дата выхода фильма должна быть после 1985 года.");
    }
}
