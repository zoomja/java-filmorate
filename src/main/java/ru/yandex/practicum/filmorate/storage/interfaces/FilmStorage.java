package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film putFilm(Film newFilm);

    Collection<Film> getAllFilms();

    public void delete(long id);

}
