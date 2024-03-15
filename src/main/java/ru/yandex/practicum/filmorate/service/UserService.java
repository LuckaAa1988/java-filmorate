package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

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
        Optional<User> user = userStorage.findById(userId);
        Optional<User> friend = userStorage.findById(friendId);
        if (user.isPresent() && friend.isPresent()) {
            if (user.get().getFriends() == null) {
                user.get().setFriends(new HashSet<>());
            }
            if (friend.get().getFriends() == null) {
                friend.get().setFriends(new HashSet<>());
            }
            user.get().getFriends().add(friendId);
            friend.get().getFriends().add(userId);
            return user.get();
        } else throw new NotFoundException("User with id: " + userId + " not found");
    }

    public User removeFriend(Long userId, Long friendId) throws NotFoundException {
        Optional<User> user = userStorage.findById(userId);
        Optional<User> friend = userStorage.findById(friendId);
        if (user.isPresent() && friend.isPresent()) {
            if (user.get().getFriends() == null) {
                user.get().setFriends(new HashSet<>());
            }
            if (friend.get().getFriends() == null) {
                friend.get().setFriends(new HashSet<>());
            }
            user.get().getFriends().remove(friendId);
            friend.get().getFriends().remove(userId);
            return user.get();
        } else throw new NotFoundException("User with id: " + userId + " not found");
    }

    public List<User> getAllFriends(Long userId) throws NotFoundException {
        Optional<User> user = userStorage.findById(userId);
        List<User> friends = new ArrayList<>();
        if (user.isPresent() && user.get().getFriends() != null) {
            for (Long id : user.get().getFriends()) {
                friends.add(userStorage.findById(id).get());
            }
            return friends;
        } else throw new NotFoundException("User with id: " + userId + " not found");
    }

    public List<User> getAllCommonFriends(Long userId, Long otherId) throws NotFoundException {
        Optional<User> user = userStorage.findById(userId);
        Optional<User> other = userStorage.findById(otherId);
        List<User> commonFriends = new ArrayList<>();
        if (user.isPresent() && other.isPresent()) {
            commonFriends.addAll(getAllFriends(userId));
            commonFriends.addAll(getAllFriends(otherId));
            Map<User, Long> map = commonFriends.stream()
                    .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
            return map.entrySet().stream().filter(c -> c.getValue() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } else throw new NotFoundException("User with id: " + userId + " not found");
    }

    public Optional<User> findById(Long userId) {
        return userStorage.findById(userId);
    }
}
