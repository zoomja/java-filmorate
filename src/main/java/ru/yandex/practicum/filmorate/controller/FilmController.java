package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Set;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.putFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Set<Long> deleteLikes(@PathVariable long filmId, @PathVariable long userId) {
        return filmService.deleteLikes(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam int count){
        return filmService.getPopular(count);
    }


//    private final FilmStorage filmStorage;
//
//    public FilmController(FilmStorage filmStorage) {
//        this.filmStorage = filmStorage;
//    }
//
//    @PostMapping
//    public Film addFilm(@Valid @RequestBody Film film) {
//        Film addedFilm = filmStorage.addFilm(film);
//        log.info("Фильм из storage", filmStorage.getAllFilms().size());
//        return addedFilm;
//    }
//
//    @PutMapping
//    public Film putFilm(@Valid @RequestBody Film newFilm) {
//        Film updatedFilm = filmStorage.putFilm(newFilm);
//        log.info("Фильм с id {} обновлён", updatedFilm.getId());
//        return updatedFilm;
//    }
//
//    @GetMapping
//    public Collection<Film> getAllFilms() {
//        log.info("Получение списка всех фильмов");
//        return filmStorage.getAllFilms();
//    }

}
