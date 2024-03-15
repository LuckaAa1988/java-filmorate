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
        Optional<Film> film = filmStorage.findById(filmId);
        if (film.isPresent()) {
            if (film.get().getLikes() == null) {
                film.get().setLikes(new HashSet<>());
            }
            film.get().getLikes().add(userId);
            return film.get();
        } else throw new NotFoundException("Film with id: " + filmId + " not Found");
    }

    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        Optional<Film> film = filmStorage.findById(filmId);
        if (film.isPresent()) {
            if (film.get().getLikes() == null) {
                film.get().setLikes(new HashSet<>());
            }
            film.get().getLikes().remove(userId);
            return film.get();
        } else throw new NotFoundException("Film with id: " + filmId + " not Found");
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage.getAll();
        if (count > popularFilms.size()) {
            count = popularFilms.size();
        }
        return popularFilms.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .collect(Collectors.toList())
                .subList(0, count);
    }

    public Optional<Film> findById(Long filmId) {
        return filmStorage.findById(filmId);
    }
}
