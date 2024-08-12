package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
public class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));

        Film savedFilm = filmDbStorage.addFilm(film);

        assertThat(savedFilm.getId()).isNotNull();
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));
        Film savedFilm = filmDbStorage.addFilm(film);

        savedFilm.setName("Updated Test Film");
        filmDbStorage.updateFilm(savedFilm);

        Film updatedFilm = filmDbStorage.findById(savedFilm.getId());
        assertThat(updatedFilm.getName()).isEqualTo("Updated Test Film");
    }

    @Test
    public void testFindFilmById() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));
        Film savedFilm = filmDbStorage.addFilm(film);

        Film foundFilm = filmDbStorage.findById(savedFilm.getId());
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getName()).isEqualTo(savedFilm.getName());
    }

    @Test
    public void testGetAllFilms() {
        Film film1 = new Film();
        film1.setName("Test Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2020, 1, 1));
        film1.setDuration(120);
        film1.setMpa(new Mpa(1, "G"));

        Film film2 = new Film();
        film2.setName("Test Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2020, 2, 1));
        film2.setDuration(150);
        film2.setMpa(new Mpa(2, "PG"));

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        Collection<Film> films = filmDbStorage.getAllFilms();
        assertThat(films.size()).isGreaterThanOrEqualTo(2);
    }
}
