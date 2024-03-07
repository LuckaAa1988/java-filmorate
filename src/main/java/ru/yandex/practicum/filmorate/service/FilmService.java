package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final List<Film> films = new ArrayList<>();
    private int id = 1;

    public Film create(Film film) throws AlreadyExistException {
        if (!films.contains(film)) {
            film.setId(id++);
            films.add(film);
            return film;
        }
        throw  new AlreadyExistException("Film already exist");
    }

    public Film update(Film film) throws NotFoundException {
        return films.stream()
                .filter(f -> f.getId() == film.getId())
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

    public List<Film> getAll() {
        return films;
    }
}
