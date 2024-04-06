package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPADAO {
    List<MPA> getAll();

    Optional<MPA> findById(Long id);
}
