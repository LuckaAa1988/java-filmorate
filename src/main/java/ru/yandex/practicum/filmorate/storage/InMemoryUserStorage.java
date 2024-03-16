package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private long id = 1;

    @Override
    public User create(User user) throws AlreadyExistException {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        if (!users.contains(user)) {
            user.setId(id++);
            users.add(user);
            return user;
        }
        throw new AlreadyExistException("User already exist");
    }

    @Override
    public User update(User user) throws NotFoundException {
        return users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .map(u -> {
                    u.setName(user.getName());
                    u.setLogin(user.getLogin());
                    u.setEmail(user.getEmail());
                    u.setBirthday(user.getBirthday());
                    return u;
                })
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public boolean delete(User user) {
        return users.removeIf(u -> u.getId().equals(user.getId()));
    }

    @Override
    public Optional<User> findById(Long friendId) {
        return users.stream()
                .filter(u -> u.getId().equals(friendId))
                .findFirst();
    }

    @Override
    public List<User> findByIds(Set<Long> friends) {
        return friends.stream()
                .flatMap(f -> users.stream()
                        .filter(u -> u.getId().equals(f)))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllCommonFriends(User user, User otherUser) {
        List<User> commonFriends = new ArrayList<>(findByIds(user.getFriends()));
        commonFriends.addAll(findByIds(otherUser.getFriends()));
        return commonFriends.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting())).entrySet().stream()
                .filter(c -> c.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return users;
    }
}
