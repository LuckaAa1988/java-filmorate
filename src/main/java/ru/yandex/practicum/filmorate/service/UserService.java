package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    private int id = 1;

    public User create(User user) throws AlreadyExistException {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        if (!users.contains(user)) {
            user.setId(id++);
            users.add(user);
            return user;
        }
        throw  new AlreadyExistException("User already exist");
    }

    public User update(User user) throws NotFoundException {
        return users.stream()
                .filter(u -> u.getId() == user.getId())
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

    public List<User> getAll() {
        return users;
    }
}
