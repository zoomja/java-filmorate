package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

public class FilmValidator implements ConstraintValidator<ValidFilm, Film> {

    @Override
    public void initialize(ValidFilm constraintAnnotation) {
    }

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext context) {
        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        }

        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Film release date must be after 1895-12-28")
                    .addPropertyNode("releaseDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
