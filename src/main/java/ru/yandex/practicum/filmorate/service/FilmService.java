package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;

    public Film addLike(int filmId, int userId) {
        likeDbStorage.addLike(filmId, userId);
        return filmDbStorage.findById(filmId);
    }

    public List<Integer> deleteLikes(int filmId, int userId) {
        User user = userDbStorage.getUserById(userId);
        Film film = filmDbStorage.findById(filmId);
        likeDbStorage.deleteLike(filmId, userId);
        return likeDbStorage.getLikesByFilmId(film.getId());
    }

    public List<Film> getPopular(int count) {
        List<Film> list = new ArrayList<>();
        for (int id : likeDbStorage.getFilmsIdByLike()) {
            list.add(filmDbStorage.findById(id));
        }

        return list;
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) {
        return filmDbStorage.findById(filmId);
    }


    public void delete(int id) {
        filmDbStorage.delete(id);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public Film addNewFilm(Film film) {
        return filmDbStorage.addFilm(film);
    }
}
