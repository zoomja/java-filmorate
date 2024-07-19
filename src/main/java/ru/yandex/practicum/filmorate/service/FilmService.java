package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public Film addLike(long filmId, long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        Film film = inMemoryFilmStorage.findById(filmId);
        film.getLikes().add(userId);
        inMemoryFilmStorage.putFilm(film);
        return film;
    }

    public Set<Long> deleteLikes(long filmId, long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        Film film = inMemoryFilmStorage.findById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не ставил лайк фильму " + filmId);
        }
        film.getLikes().remove(userId);
        inMemoryFilmStorage.putFilm(film);
        return film.getLikes();
    }

    public List<Film> getPopular(int count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count > 0 ? count : 10)
                .collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void delete(long id) {
        inMemoryFilmStorage.delete(id);
    }

    public Film putFilm(Film film) {
        return inMemoryFilmStorage.putFilm(film);
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    private void checkFilmExists(long id) {
        if (inMemoryFilmStorage.findById(id) == null) {
            throw new NotFoundException("Нет фильма с id: " + id);
        }
    }

    private void checkUserExists(long id) {
        if (inMemoryUserStorage.findById(id) == null) {
            throw new NotFoundException("Нет пользователя с id: " + id);
        }
    }
}
