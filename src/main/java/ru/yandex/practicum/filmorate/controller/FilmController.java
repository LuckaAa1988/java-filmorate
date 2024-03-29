package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) throws AlreadyExistException {
        return ResponseEntity.ok(filmService.create(film));
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) throws NotFoundException {
        return ResponseEntity.ok(filmService.update(film));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(filmService.getAll());
    }

    @GetMapping(value = "/{filmId}")
    public Film findById(@PathVariable Long filmId) throws NotFoundException {
        return filmService.findById(filmId);
    }

    @PutMapping(value = "/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Long filmId,
                                        @PathVariable Long userId) throws NotFoundException {
        return ResponseEntity.ok(filmService.addLike(filmId, userId));
    }

    @DeleteMapping(value = "/{filmId}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable Long filmId,
                                           @PathVariable Long userId) throws NotFoundException {
        return ResponseEntity.ok(filmService.removeLike(filmId, userId));
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count));
    }
}
