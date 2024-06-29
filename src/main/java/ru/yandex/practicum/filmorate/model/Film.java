package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
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
    private int duration;

}
