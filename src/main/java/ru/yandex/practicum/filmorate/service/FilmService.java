package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film create(Film film) throws AlreadyExistException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws NotFoundException {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(Long filmId, Long userId) throws NotFoundException {
        Optional<Film> filmOptional = filmStorage.findById(filmId);
        filmOptional.ifPresent(film -> {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().add(userId);
        });
        return filmOptional.orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }

    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        Optional<Film> filmOptional = filmStorage.findById(filmId);
        filmOptional.ifPresent(film -> {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().remove(userId);
        });
        return filmOptional.orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film findById(Long filmId) throws NotFoundException {
        return filmStorage.findById(filmId).orElseThrow(
                () -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }
}
