package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmDAO {
    private final List<Film> films = new ArrayList<>();
    private long id = 1;

    @Override
    public Film create(Film film) throws AlreadyExistException {
        if (!films.contains(film)) {
            film.setId(id++);
            films.add(film);
            return film;
        }
        throw new AlreadyExistException("Film already exist");
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        return films.stream()
                .filter(f -> f.getId().equals(film.getId()))
                .map(f -> {
                    f.setName(film.getName());
                    f.setDescription(film.getDescription());
                    f.setDuration(film.getDuration());
                    f.setReleaseDate(film.getReleaseDate());
                    return f;
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public boolean delete(Film film) {
        return films.removeIf(f -> f.getId().equals(film.getId()));
    }

    @Override
    public Optional<Film> findById(Long id) {
        return films.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        if (count > films.size()) {
            count = films.size();
        }
        return films.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .collect(Collectors.toList())
                .subList(0, count);
    }

    @Override
    public Film addLike(Long filmId, Long userId) throws NotFoundException {
        Optional<Film> filmOptional = findById(filmId);
        filmOptional.ifPresent(film -> {
            if (film.getLikes() == null) {
                film.setLikes(new ArrayList<>());
            }
            film.getLikes().add(userId);
        });
        return filmOptional.orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }

    @Override
    public Film removeLike(Long filmId, Long userId) throws NotFoundException {
        Optional<Film> filmOptional = findById(filmId);
        filmOptional.ifPresent(film -> {
            if (film.getLikes() == null) {
                film.setLikes(new ArrayList<>());
            }
            film.getLikes().remove(userId);
        });
        return filmOptional.orElseThrow(() -> new NotFoundException("Film with id: " + filmId + " not Found"));
    }

    @Override
    public List<Film> getAll() {
        return films;
    }
}
