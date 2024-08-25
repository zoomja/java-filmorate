package ru.yandex.practicum.filmorate.mapper;


import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDbStorage.getMpaById(rs.getInt("mpa_id")))
                .genres(genreDbStorage.getGenresListForFilm(rs.getInt("id")))
                .build();
    }
}
