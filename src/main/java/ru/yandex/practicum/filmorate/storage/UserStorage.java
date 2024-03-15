package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user) throws AlreadyExistException;

    User update(User user) throws NotFoundException;

    List<User> getAll();

    boolean delete(User user);

    Optional<User> findById(Long id);
}
