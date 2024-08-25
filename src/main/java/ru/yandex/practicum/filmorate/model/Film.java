package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validation.ValidFilm;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidFilm
public class Film {

    private int id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @NotNull(message = "Дата выхода не может быть пустой.")
    @PastOrPresent(message = "Дата выхода не может быть в будущем.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом.")
    private Integer duration;

    Set<Long> likes;

    private Mpa mpa;
    private List<Genre> genres;
}