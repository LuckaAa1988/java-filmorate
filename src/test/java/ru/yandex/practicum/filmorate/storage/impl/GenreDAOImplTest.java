package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDAO;
import ru.yandex.practicum.filmorate.storage.GenreDAO;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDAOImplTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testGetAllGenres() {
        Genre genre1 = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();
        Genre genre2 = Genre.builder()
                .id(2L)
                .name("Драма")
                .build();
        Genre genre3 = Genre.builder()
                .id(3L)
                .name("Мультфильм")
                .build();
        Genre genre4 = Genre.builder()
                .id(4L)
                .name("Триллер")
                .build();
        Genre genre5 = Genre.builder()
                .id(5L)
                .name("Документальный")
                .build();
        Genre genre6 = Genre.builder()
                .id(6L)
                .name("Боевик")
                .build();

        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre1);
        genreList.add(genre2);
        genreList.add(genre3);
        genreList.add(genre4);
        genreList.add(genre5);
        genreList.add(genre6);

        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);

        List<Genre> savedGenreList = genreDAO.getAll();

        assertThat(savedGenreList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genreList);
    }

    @Test
    void testFindById() {
        Genre genre = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();

        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);

        Genre savedGenre = genreDAO.findById(1L).get();

        assertThat(savedGenre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @Test
    void testGetGenresByFilmId() throws NotFoundException, AlreadyExistException {
        Genre genre1 = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();
        Genre genre2 = Genre.builder()
                .id(2L)
                .name("Драма")
                .build();

        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre1);
        genreList.add(genre2);

        Film fIlm = Film.builder()
                .id(1L)
                .name("Test")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .genres(genreList)
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .build();

        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);

        filmDAO.create(fIlm);

        List<Genre> savedGenreList = genreDAO.getGenresByFilmId(fIlm.getId());

        assertThat(savedGenreList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genreList);
    }
}