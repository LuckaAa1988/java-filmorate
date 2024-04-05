package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDAO;
import ru.yandex.practicum.filmorate.storage.GenreDAO;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("filmDAOImpl")
public class FilmDAOImpl implements FilmDAO {

    private final JdbcTemplate jdbcTemplate;
    private final MPADAO mpadao;
    private final GenreDAO genreDAO;
    private static final String INSERT_FILM_SQL = """
                INSERT INTO films (name, description, duration, release_date, mpa_id)
                VALUES (?,?,?,?,?)
                """;
    private static final String INSERT_FILM_GENRES_SQL = """
                INSERT INTO genre_films (film_id, genre_id)
                VALUES (?,?)
                """;
    private static final String UPDATE_FILM_SQL = """
                UPDATE films
                SET name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ?
                WHERE id = ?
                """;
    private static final String GET_ALL_FILMS_SQL = """
                SELECT f.id, f.name, f.description, f.duration, f.release_date, f.mpa_id, m.name
                FROM films AS f
                JOIN mpa as m ON f.mpa_id = m.id
                """;
    private static final String GET_FILM_LIKES_SQL = """
                        SELECT user_id FROM films_likes
                        WHERE film_id = ?
                        """;
    private static final String DELETE_FILM_SQL = """
                DELETE FROM films
                WHERE id = ?
                """;
    private static final String FIND_BY_ID_SQL = """
                SELECT f.id, f.name, f.description, f.duration, f.release_date, f.mpa_id, m.name
                FROM films AS f
                JOIN mpa as m ON f.mpa_id = m.id
                WHERE f.id = ?
                """;
    private static final String GET_POPULAR_FILMS_SQL = """
                SELECT fl.film_id,
                       COUNT(fl.user_id) AS likes,
                       f.name,
                       f.description,
                       f.duration,
                       f.release_date,
                       f.mpa_id,
                       m.name
                FROM films_likes AS fl
                JOIN films as f ON fl.film_id = f.id
                JOIN mpa as m ON f.mpa_id= m.id
                GROUP BY fl.film_id
                ORDER BY likes DESC
                LIMIT ?
                """;
    private static final String ADD_LIKE_SQL = """
                INSERT INTO films_likes (film_id, user_id)
                VALUES (?,?)
                """;
    private static final String REMOVE_LIKE_SQL = """
                DELETE FROM films_likes
                WHERE film_id = ? AND user_id = ?
                """;

    @Override
    public Film create(Film film) throws AlreadyExistException, NotFoundException {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_FILM_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        film.setMpa(mpadao.findById(film.getMpa().getId()).orElseThrow(() -> new NotFoundException("Genre not found")));
        if (film.getGenres() != null) {
            List<Genre> uniqueGenre = film.getGenres().stream().distinct().collect(Collectors.toList());
            addFilmGenres(uniqueGenre, film.getId());
        }
        film.setGenres(genreDAO.getGenresByFilmId(film.getId()));
        return film;
    }

    private void addFilmGenres(List<Genre> genreList, Long filmId) {
        jdbcTemplate.batchUpdate(INSERT_FILM_GENRES_SQL, genreList, 10,
                (PreparedStatement ps, Genre g) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, g.getId());
                });
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        findById(film.getId()).orElseThrow(()
                -> new NotFoundException("Film with id: " + film.getId() + " not Found"));
        jdbcTemplate.update(UPDATE_FILM_SQL, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());
        film.setMpa(mpadao.findById(film.getMpa().getId()).orElseThrow(() -> new NotFoundException("Genre not found")));
        film.setGenres(genreDAO.getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_FILMS_SQL, (rs, rowNum) -> Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .genres(genreDAO.getGenresByFilmId(rs.getLong("id")))
                .mpa(MPA.builder()
                        .id(rs.getLong("mpa_id"))
                        .name(rs.getString(7))
                        .build())
                .likes(jdbcTemplate.query(GET_FILM_LIKES_SQL,
                        (rs1, rowNum1) -> rs1.getLong("user_id"), rs.getLong("id")))
                .build());
    }

    @Override
    public boolean delete(Film film) {
        return jdbcTemplate.update(DELETE_FILM_SQL, film.getId()) > 0;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, (rs, rowNum) -> Film.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .description(rs.getString(3))
                .duration(rs.getInt(4))
                .releaseDate(rs.getDate(5).toLocalDate())
                .genres(genreDAO.getGenresByFilmId(rs.getLong(1)))
                .mpa(MPA.builder()
                        .id(rs.getLong(6))
                        .name(rs.getString(7))
                        .build())
                .likes(jdbcTemplate.query(GET_FILM_LIKES_SQL,
                        (rs1, rowNum1) -> rs1.getLong("user_id"), rs.getLong("id")))
                .build(), id).stream().findFirst();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query(GET_POPULAR_FILMS_SQL, (rs, rowNum) -> Film.builder()
                .id(rs.getLong(1))
                .name(rs.getString(3))
                .description(rs.getString(4))
                .duration(rs.getInt(5))
                .releaseDate(rs.getDate(6).toLocalDate())
                .genres(genreDAO.getGenresByFilmId(rs.getLong(1)))
                .mpa(MPA.builder()
                        .id(rs.getLong(7))
                        .name(rs.getString(8))
                        .build())
                .build(), count);
    }

    @Override
    public Film addLike(Long filmId, Long userId) throws NotFoundException {
        jdbcTemplate.update(ADD_LIKE_SQL, filmId, userId);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }

    @Override
    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        jdbcTemplate.update(REMOVE_LIKE_SQL, filmId, userId);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }
}
