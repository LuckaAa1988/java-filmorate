package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film) throws AlreadyExistException;

    Film update(Film film) throws NotFoundException;

    List<Film> getAll();

    boolean delete(Film film);

    Optional<Film> findById(Long id);

    List<Film> getPopularFilms(int count);
}
