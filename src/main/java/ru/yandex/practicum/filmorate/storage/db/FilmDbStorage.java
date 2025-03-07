package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = """
                SELECT f.film_id, f.name, f.description, f.release_Date, f.duration,
                       m.mpa_rating_id, m.mpa_name, fg.genre_id, g.genre_name
                FROM films AS f
                JOIN mpa_rating AS m ON f.rating_id = m.mpa_rating_id
                LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id
                LEFT JOIN genre AS g ON fg.genre_id = g.genre_id
                ORDER BY f.film_id;
                """;

        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Добавляем фильм: {}", film);
        String sql = "INSERT INTO films (name, description," +
                " release_Date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);

        List<Genre> genres = film.getGenres();
        String genreSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(genreSql, genres, genres.size(), (ps, genre) -> {
            ps.setLong(1, id);
            ps.setInt(2, genre.getId());
        });
        film.setGenres(genres);
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @Override
    public void updateFilm(Film film) {
        log.info("Обновляем фильм: {}", film);
        String sql = "UPDATE films SET name = ?, description = ?, release_Date = ?," +
                " duration = ?, rating_id = ? WHERE film_id = ?";
        int rows = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rows == 0) throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        String deleteGenresSql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            String genreSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(genreSql, uniqueGenres, uniqueGenres.size(), (ps, genre) -> {
                ps.setLong(1, film.getId());
                ps.setInt(2, genre.getId());
            });
        }
        log.info("Обновлен фильм: {}.", film.getName());
    }

    @Override
    public Film getFilmById(Long filmId) {
        log.info("Получаем фильм по ID: {}.", filmId);
        String sql = "SELECT f.*, m.mpa_rating_id, m.mpa_name FROM films AS f " +
                "JOIN mpa_rating AS m ON f.rating_id = m.mpa_rating_id  WHERE f.film_id = ?";
        Film film = jdbcTemplate.query(sql, new FilmRowMapper(), filmId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));
        String genreSql = "SELECT g.genre_id, g.genre_name FROM genre AS g " +
                "JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(genreSql, new GenreRowMapper(), filmId);
        film.setGenres(genres);
        log.info("По ID {} получен фильм: {}.", filmId, film.getName());
        return film;
    }

    @Override
    public List<Film> getPopularFilmOnLike(int count) {
        String sql = """
                SELECT f.film_id, f.name, f.description, f.release_Date, f.duration, f.rating_id, m.*,fg.genre_id,
                       g.genre_name, COUNT(l.user_id) AS sum \
                FROM films f \
                LEFT JOIN film_likes l ON f.film_id=l.film_id \
                JOIN mpa_rating m ON f.rating_id = m.mpa_rating_id \
                JOIN film_genre AS fg ON f.film_id = fg.film_id \
                LEFT JOIN genre AS g ON fg.genre_id = g.genre_id \
                GROUP BY f.film_id, f.name, f.description, f.release_Date, f.duration, f.rating_id, fg.genre_id,
                      g.genre_name \
                ORDER BY sum DESC \
                LIMIT ?""";

        return jdbcTemplate.query(sql, new FilmRowMapper(), count);
    }
}
