package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDAO;
import ru.yandex.practicum.filmorate.storage.GenreDAO;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDAOImplTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testFindFilmById() throws AlreadyExistException, NotFoundException {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Test")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();
        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(newFilm);

        Film savedFilm = filmDAO.findById(1L).get();

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void testUpdateFilm() throws AlreadyExistException, NotFoundException {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Test")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();

        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(newFilm);

        Film updatedFilm = Film.builder()
                .id(1L)
                .name("Updated Test")
                .description("Test desc")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();

        filmDAO.update(updatedFilm);

        Film savedFilm = filmDAO.findById(1L).get();

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

    @Test
    void testGetAllFilms() throws AlreadyExistException, NotFoundException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Test1")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();
        Film film2 = Film.builder()
                .id(2L)
                .name("Test2")
                .description("Test desc")
                .releaseDate(LocalDate.of(1990, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();
        Film film3 = Film.builder()
                .id(3L)
                .name("Test3")
                .description("Test desc")
                .releaseDate(LocalDate.of(1995, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();


        List<Film> listFilms = new ArrayList<>();
        listFilms.add(film1);
        listFilms.add(film2);
        listFilms.add(film3);

        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(film1);
        filmDAO.create(film2);
        filmDAO.create(film3);

        List<Film> savedFilms = filmDAO.getAll();

        assertThat(savedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilms);
    }

    @Test
    void testDeleteFilm() throws AlreadyExistException, NotFoundException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Test1")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();
        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(film1);

        filmDAO.delete(film1);

        Optional<Film> savedFilm = filmDAO.findById(1L);

        assertThat(savedFilm)
                .isEqualTo(Optional.empty());
    }

    @Test
    void testAddLikes() throws NotFoundException, AlreadyExistException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Test1")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(new ArrayList<>())
                .build();
        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(film1);
        User newUser = User.builder()
                .id(1L)
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(newUser);

        filmDAO.addLike(1L,1L);

        List<Long> friendList = new ArrayList<>();
        friendList.add(1L);

        List<Long> savedFriendList = filmDAO.findById(1L).get().getLikes();

        assertThat(savedFriendList)
                .usingRecursiveComparison()
                .isEqualTo(friendList);
    }

    @Test
    void testRemoveLikes() throws NotFoundException, AlreadyExistException {
        List<Long> friendList = new ArrayList<>();
        Film film1 = Film.builder()
                .id(1L)
                .name("Test1")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .likes(friendList)
                .build();
        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(film1);
        User newUser = User.builder()
                .id(1L)
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(newUser);

        filmDAO.addLike(1L,1L);

        filmDAO.removeLike(1L,1L);

        List<Long> savedFriendList = filmDAO.findById(1L).get().getLikes();

        assertThat(savedFriendList)
                .usingRecursiveComparison()
                .isEqualTo(friendList);
    }

    @Test
    void testGetPopularFilms() throws NotFoundException, AlreadyExistException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Test1")
                .description("Test desc")
                .releaseDate(LocalDate.of(1991, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .build();
        Film film2 = Film.builder()
                .id(2L)
                .name("Test2")
                .description("Test desc")
                .releaseDate(LocalDate.of(1990, 2, 2))
                .mpa(MPA.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .build();

        User firstUser = User.builder()
                .id(1L)
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User secondUser = User.builder()
                .id(2L)
                .name("Ivan Popov")
                .email("user123@email.ru")
                .login("Popov123")
                .birthday(LocalDate.of(1991, 2, 2))
                .friends(new ArrayList<>())
                .build();

        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);
        GenreDAO genreDAO = new GenreDAOImpl(jdbcTemplate);
        FilmDAO filmDAO = new FilmDAOImpl(jdbcTemplate,mpadao,genreDAO);
        filmDAO.create(film1);
        filmDAO.create(film2);

        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(firstUser);
        userDAO.create(secondUser);

        filmDAO.addLike(2L,1L);
        filmDAO.addLike(2L,2L);
        filmDAO.addLike(1L,1L);

        List<Film> savedPopularFilms = filmDAO.getPopularFilms(3);

        assertAll(() -> {
            assertEquals(savedPopularFilms.get(0), film2);
            assertEquals(savedPopularFilms.get(1), film1);
        });
    }
}