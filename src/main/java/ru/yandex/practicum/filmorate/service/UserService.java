package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) throws AlreadyExistException {
        return userStorage.create(user);
    }

    public User update(User user) throws NotFoundException {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addFriend(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User friend = userStorage.findById(friendId).orElseThrow(
                () -> new NotFoundException("User with id: " + friendId + " not found"));
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) throws NotFoundException {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User friend = userStorage.findById(friendId).orElseThrow(
                () -> new NotFoundException("User with id: " + friendId + " not found"));
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> getAllFriends(Long userId) throws NotFoundException {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        return userStorage.findByIds(user.getFriends());
    }

    public List<User> getAllCommonFriends(Long userId, Long otherId) throws NotFoundException {
        User user = userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User other = userStorage.findById(otherId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        return userStorage.getAllCommonFriends(user, other);
    }

    public User findById(Long userId) throws NotFoundException {
        return userStorage.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
    }
}
