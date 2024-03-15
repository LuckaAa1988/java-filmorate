package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) throws AlreadyExistException {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws NotFoundException {
        return ResponseEntity.ok(userService.update(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(value = "/{userId}")
    public Optional<User> findById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PutMapping(value = "/{userId}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Long userId,
                                          @PathVariable Long friendId) throws NotFoundException {
        return ResponseEntity.ok(userService.addFriend(userId,friendId));
    }

    @DeleteMapping(value = "/{userId}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable Long userId,
                                             @PathVariable Long friendId) throws NotFoundException {
        return ResponseEntity.ok(userService.removeFriend(userId,friendId));
    }

    @GetMapping(value = "/{userId}/friends")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable Long userId) throws NotFoundException {
        return ResponseEntity.ok(userService.getAllFriends(userId));
    }

    @GetMapping(value = "/{userId}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getAllCommonFriends(@PathVariable Long userId,
                                                          @PathVariable Long otherId) throws NotFoundException {
        return ResponseEntity.ok(userService.getAllCommonFriends(userId,otherId));
    }

}
