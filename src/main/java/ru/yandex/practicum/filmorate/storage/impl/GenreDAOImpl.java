package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDAO;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GenreDAOImpl implements GenreDAO {

    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_GENRES_SQL = "SELECT id, name FROM genre ORDER BY id";
    private static final String FIND_BY_ID_SQL = "SELECT id, name FROM genre WHERE id = ?";
    private static final String FIND_GENRES_BY_FILM_SQL = "SELECT gf.genre_id, g.name " +
                                                          "FROM genre_films gf " +
                                                          "JOIN genre AS g ON gf.genre_id = g.id " +
                                                          "WHERE gf.film_id = ?";

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_GENRES_SQL, (rs, rowNum) -> Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, (rs, rowNum) -> Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build(), id).stream().findFirst();
    }

    @Override
    public List<Genre> getGenresByFilmId(Long id) {
        return jdbcTemplate.query(FIND_GENRES_BY_FILM_SQL, (rs, rowNum) -> Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("name"))
                .build(), id);
    }
}
