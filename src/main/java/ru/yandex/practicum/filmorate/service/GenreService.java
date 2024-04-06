package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDAO genreDAO;

    public List<Genre> getAll() {
        return genreDAO.getAll();
    }

    public Genre findById(Long id) throws NotFoundException {
        return genreDAO.findById(id).orElseThrow(() -> new NotFoundException("Id " + id + "not found"));
    }

    public List<Genre> getGenresByFilmId(Long id) {
        return genreDAO.getGenresByFilmId(id);
    }
}
