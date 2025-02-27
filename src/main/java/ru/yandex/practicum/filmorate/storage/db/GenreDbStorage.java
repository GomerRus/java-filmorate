package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return namedParameterJdbcTemplate.query("SELECT * FROM genre", new GenreRowMapper());
    }

    @Override
    public List<Genre> getGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            List<Integer> genreIds = film.getGenres().stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());
            String sqlTestGenreId = "SELECT * FROM genre WHERE genre_id IN (:genreIds)";
            Map<String, Object> params = new HashMap<>();
            params.put("genreIds", genreIds);
            List<Genre> genres = namedParameterJdbcTemplate.query(sqlTestGenreId, params, new GenreRowMapper());
            if (genres.isEmpty()) {
                throw new NotFoundException("Жанры с id " + genreIds + " не найдены");
            }
            return genres;
        } else return new ArrayList<>();
    }

    @Override
    public Genre getGenreById(int id) {
        String genreTableQuery = "SELECT * FROM genre WHERE genre_id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Genre> genre = namedParameterJdbcTemplate.query(genreTableQuery, params, new GenreRowMapper());
        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
        return genre.getFirst();
    }
}
