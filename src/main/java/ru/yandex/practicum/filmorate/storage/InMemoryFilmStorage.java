package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0;

    @Override
    public Film addFilm(Film film) {
        log.info("Создание нового фильма: {}", film);
        validateFilm(film);
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Добавлен фильм с id= {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Нет id");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            validateFilm(newFilm);

            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            log.info("Фильм обновлен: {}", oldFilm);
            return oldFilm;
        }
        log.error("Нет фильма с данным id: {}", newFilm.getId());
        throw new NotFoundException("Пост с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void delete(long id) {
        if (films.containsKey(id)) {
            films.remove(id);
        }
    }

    public Film findById(long id) {
        return films.get(id);
    }

    public long getNextId() {
        return ++currentId;
    }

    private void validateFilm(Film film) {
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Дата выхода фильма должна быть после 1985 года.");
    }
}
