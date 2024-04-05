package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDAO;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("userDAOImpl")
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_USER_SQL = """
                INSERT INTO users (name, email, login, birthday)
                VALUES (?,?,?,?)
                """;
    private static final String UPDATE_USER_SQL = """
                UPDATE users SET name = ?, email = ?, login = ?, birthday = ?
                WHERE id = ?
                """;
    private static final String GET_ALL_SQL = """
                SELECT u.id, u.name, u.email, u.login, u.birthday FROM users AS u
                ORDER BY u.id
                """;
    private static final String SELECT_FRIENDS_SQL = """
            SELECT friend_id
            FROM friends
            WHERE user_id = ?
            """;
    private static final String DELETE_USERS_SQL = """
                DELETE FROM users
                WHERE id = ?
                """;
    private static final String FIND_BY_ID_SQL = """
                SELECT u.id, u.name, u.email, u.login, u.birthday FROM users AS u
                WHERE id = ?
                ORDER BY id;
                """;
    private static final String UPDATE_FRIENDS_SQL = """
            UPDATE friends SET is_friend = true
            WHERE user_id = ? AND friend_id = ?
            """;
    private static final String ADD_FRIEND_SQL = """
                INSERT INTO friends (user_id, friend_id, is_friend)
                VALUES (?,?,?)
                """;
    private static final String REMOVE_FRIEND_SQL = """
                DELETE FROM friends
                WHERE user_id = ? AND friend_id = ?
                """;

    @Override
    public User create(User user) throws AlreadyExistException {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) throws NotFoundException {
        if (findById(user.getId()).isEmpty()) {
            throw new NotFoundException("User with id: " + user.getId() + " not found");
        }
        jdbcTemplate.update(UPDATE_USER_SQL,
                user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_SQL, (rs, rowNum) -> User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(jdbcTemplate.query(SELECT_FRIENDS_SQL,
                        (rs1, rowNum1) -> rs1.getLong("friend_id"), rs.getLong("id")))
                .build());
    }

    @Override
    public boolean delete(User user) {
        return jdbcTemplate.update(DELETE_USERS_SQL, user.getId()) > 0;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, (rs, rowNum) -> User.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .email(rs.getString("email"))
                        .login(rs.getString("login"))
                        .birthday(rs.getDate("birthday").toLocalDate())
                        .friends(jdbcTemplate.query(SELECT_FRIENDS_SQL,
                                (rs1, rowNum1) -> rs1.getLong("friend_id"), id))
                        .build(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<User> findByIds(User user) {
        List<Long> friendList = jdbcTemplate.query(SELECT_FRIENDS_SQL,
                (rs1, rowNum1) -> rs1.getLong("friend_id"), user.getId());
        List<User> userList = new ArrayList<>();
        for (Long id : friendList) {
            userList.add(findById(id).get());
        }
        return userList;
    }

    @Override
    public List<User> getAllCommonFriends(User user, User otherUser) {
        List<Long> commonFriends = new ArrayList<>(
                jdbcTemplate.query(SELECT_FRIENDS_SQL,
                        (rs, rowNum) -> rs.getLong("friend_id"), user.getId()));
        commonFriends.addAll(
                jdbcTemplate.query(SELECT_FRIENDS_SQL,
                        (rs, rowNum) -> rs.getLong("friend_id"), otherUser.getId()));
        List<Long> result = commonFriends.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting())).entrySet().stream()
                .filter(c -> c.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        List<User> userList = new ArrayList<>();
        for (Long id : result) {
            userList.add(findById(id).get());
        }
        return userList;
    }

    public User addFriend(Long userId, Long friendId) throws NotFoundException {
        User user = findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User friend = findById(friendId).orElseThrow(
                () -> new NotFoundException("User with id: " + friendId + " not found"));
        boolean isFriend = false;

        List<Long> friendList = jdbcTemplate.query(SELECT_FRIENDS_SQL, (rs, rowNum) -> rs.getLong("friend_id"), friendId);

        if (friendList.contains(userId)) {
            jdbcTemplate.update(UPDATE_FRIENDS_SQL,
                    friendId, userId);
            isFriend = true;
        }
        jdbcTemplate.update(ADD_FRIEND_SQL, userId, friendId, isFriend);
        user.getFriends().add(friendId);
        return user;
    }

    public boolean removeFriend(Long userId, Long friendId) throws NotFoundException {
        User user = findById(userId).orElseThrow(
                () -> new NotFoundException("User with id: " + userId + " not found"));
        User friend = findById(friendId).orElseThrow(
                () -> new NotFoundException("User with id: " + friendId + " not found"));
        return jdbcTemplate.update(REMOVE_FRIEND_SQL, userId, friendId) > 0;
    }
}
