package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(@Qualifier("userDAOImpl") UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User create(User user) throws AlreadyExistException {
        return userDAO.create(user);
    }

    public User update(User user) throws NotFoundException {
        return userDAO.update(user);
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

    public User addFriend(Long userId, Long friendId) throws NotFoundException {
        return userDAO.addFriend(userId,friendId);
    }

    public boolean removeFriend(Long userId, Long friendId) throws NotFoundException {
        return userDAO.removeFriend(userId,friendId);
    }

    public List<User> getAllFriends(Long userId) throws NotFoundException {
        User user = userDAO.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        return userDAO.findByIds(user);
    }

    public List<User> getAllCommonFriends(Long userId, Long otherId) throws NotFoundException {
        User user = userDAO.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User other = userDAO.findById(otherId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        return userDAO.getAllCommonFriends(user, other);
    }

    public User findById(Long userId) throws NotFoundException {
        return userDAO.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
    }
}
