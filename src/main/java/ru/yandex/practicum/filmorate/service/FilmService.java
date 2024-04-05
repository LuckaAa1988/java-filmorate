package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDAO;

import java.util.List;

@Service
public class FilmService {

    private final FilmDAO filmDAO;

    public FilmService(@Qualifier("filmDAOImpl") FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }

    public Film create(Film film) throws AlreadyExistException, NotFoundException {
        return filmDAO.create(film);
    }

    public Film update(Film film) throws NotFoundException {
        return filmDAO.update(film);
    }

    public List<Film> getAll() {
        return filmDAO.getAll();
    }

    public Film addLike(Long filmId, Long userId) throws NotFoundException {
        return filmDAO.addLike(filmId,userId);
    }

    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        return filmDAO.removeLike(filmId,userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmDAO.getPopularFilms(count);
    }

    public Film findById(Long filmId) throws NotFoundException {
        return filmDAO.findById(filmId).orElseThrow(
                () -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }
}
