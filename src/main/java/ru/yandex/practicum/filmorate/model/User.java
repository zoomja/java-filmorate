package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {

    private Long id;

    private String name;

    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NotBlank(message = "Логин не может быть пустым или содержать пробелы")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;

    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    @JsonFormat(pattern = ("yyyy-MM-dd"))
    private LocalDate birthday;

    Set<Long> friends;
}
