package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User create(User user) throws AlreadyExistException;

    User update(User user) throws NotFoundException;

    List<User> getAll();

    boolean delete(User user);

    Optional<User> findById(Long id);

    List<User> findByIds(Set<Long> id);

    List<User> getAllCommonFriends(User user, User otherUser);
}
