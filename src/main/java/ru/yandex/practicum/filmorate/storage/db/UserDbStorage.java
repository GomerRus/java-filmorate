package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, email, login, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY user_id", new UserRowMapper());
    }

    @Override
    public User addUser(User user) {
        log.info("Добавляем пользователя: {}", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_QUERY, new String[]{"user_id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getLogin());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Добавлен пользователь: {}.", user.getLogin());
        return user;
    }

    @Override
    public void updateUser(User user) {
        log.info("Обновляем данные пользователя: {}", user);
        validateUser(user.getId());
        jdbcTemplate.update(
                UPDATE_USER_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        log.info("Данные пользователя {} обновлены", user.getLogin());
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Получаем пользователя по ID: {}", userId);
        List<User> user = jdbcTemplate.query(FIND_BY_ID_QUERY, new UserRowMapper(), userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        log.info("По ID {} получен пользователь: {}", userId, user.getFirst());
        return user.getFirst();
    }

    @Override
    public void addFriends(Long idUser1, Long idUser2) {
        log.info("Пользователь {} хочет добавить в друзья {}", idUser1, idUser2);
        validateUser(idUser1);
        validateUser(idUser2);
        String sql = "INSERT INTO friends (user_id1, user_id2) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUser1, idUser2);
        log.info("Пользователь {} добавил в друзья {}", idUser1, idUser2);
    }

    @Override
    public void removeFriend(Long idUser1, Long idUser2) {
        log.info("Пользователь {} хочет удалить из друзей {}", idUser1, idUser2);
        validateUser(idUser1);
        validateUser(idUser2);
        String sql = "DELETE FROM friends WHERE user_id1 = ? AND user_id2 = ?";
        jdbcTemplate.update(sql, idUser1, idUser2);
        log.info("Пользователь {} удалил из друзей {}", idUser1, idUser2);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        log.info("Найти друга по ID: {}", id);
        validateUser(id);
        String sql = "SELECT u.* FROM friends AS f " +
                "JOIN users AS u ON f.user_id2 = u.user_id " +
                "WHERE f.user_id1 = ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), id);
    }

    @Override
    public Collection<User> getCommonFriends(Long id1, Long id2) {
        log.info("Найти общих друзей пользователей с ID {} и ID {}", id1, id2);
        validateUser(id1);
        validateUser(id2);
        String sql = "SELECT u.* FROM friends AS f1 " +
                "JOIN friends AS f2 ON f1.user_id2 = f2.user_id2 " +
                "JOIN users AS u ON f1.user_id2 = u.user_id " +
                "WHERE f1.user_id1 = ? AND f2.user_id1 = ?";

        return jdbcTemplate.query(sql, new UserRowMapper(), id1, id2);
    }

    private boolean userExists(Long userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    private void validateUser(Long userId) {
        if (!userExists(userId)) {
            throw new NotFoundException("Пользователя с id" + userId + " не существует");
        }
    }
}