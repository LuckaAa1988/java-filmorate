package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDAOImplTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testFindUserById() throws AlreadyExistException {
        User newUser = User.builder()
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(newUser);

        Long userId = newUser.getId();

        User savedUser = userDAO.findById(userId).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void testUpdateUser() throws AlreadyExistException, NotFoundException {
        User newUser = User.builder()
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(newUser);

        Long userId = newUser.getId();

        User updatedUser = User.builder()
                .id(userId)
                .name("Ivan Popov")
                .email("user123@email.ru")
                .login("Popov123")
                .birthday(LocalDate.of(1991, 2, 2))
                .friends(new ArrayList<>())
                .build();

        userDAO.update(updatedUser);

        User savedUser = userDAO.findById(userId).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    void testGetAllUsers() throws AlreadyExistException {
        User firstUser = User.builder()
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User secondUser = User.builder()
                .name("Ivan Popov")
                .email("user123@email.ru")
                .login("Popov123")
                .birthday(LocalDate.of(1991, 2, 2))
                .friends(new ArrayList<>())
                .build();

        List<User> listUsers = new ArrayList<>();
        listUsers.add(firstUser);
        listUsers.add(secondUser);

        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);

        userDAO.create(firstUser);
        userDAO.create(secondUser);

        List<User> savedUsers = userDAO.getAll();

        assertThat(savedUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listUsers);
    }

    @Test
    void testDeleteUser() throws AlreadyExistException {
        User newUser = User.builder()
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(newUser);

        Long userId = newUser.getId();

        userDAO.delete(newUser);

        Optional<User> savedUser = userDAO.findById(userId);

        assertThat(savedUser)
                .isEqualTo(Optional.empty());
    }

    @Test
    void testGetFriendsFromUser() throws AlreadyExistException, NotFoundException {
        List<Long> friendListId1 = new ArrayList<>();
        List<Long> friendListId2 = new ArrayList<>();

        User firstUser = User.builder()
                .name("Ivan Petrov")
                .email("user@email.ru")
                .login("vanya123")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(friendListId1)
                .build();
        User secondUser = User.builder()
                .name("Ivan Popov")
                .email("user123@email.ru")
                .login("Popov123")
                .birthday(LocalDate.of(1991, 2, 2))
                .friends(friendListId2)
                .build();

        UserDAOImpl userDAO = new UserDAOImpl(jdbcTemplate);
        userDAO.create(firstUser);
        userDAO.create(secondUser);

        Long user1Id = firstUser.getId();
        Long user2Id = secondUser.getId();

        friendListId1.add(user2Id);
        friendListId2.add(user1Id);

        userDAO.addFriend(user1Id,user2Id);
        userDAO.addFriend(user2Id,user1Id);

        User savedFirstUser = userDAO.findById(user1Id).get();
        User savedSecondUser = userDAO.findById(user2Id).get();

        assertAll(() -> {
            assertThat(savedFirstUser)
                    .usingRecursiveComparison()
                    .isEqualTo(firstUser);
            assertThat(savedSecondUser)
                    .usingRecursiveComparison()
                    .isEqualTo(secondUser);
        });
    }
}