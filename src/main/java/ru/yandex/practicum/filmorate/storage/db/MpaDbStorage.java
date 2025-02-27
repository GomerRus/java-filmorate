package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa_rating ORDER BY mpa_rating_id", new MpaRowMapper());
    }

    @Override
    public Mpa getMpaById(int id) {
        String mpaTableQuery = "SELECT * FROM mpa_rating WHERE mpa_rating_id = ?";
        List<Mpa> mpa = jdbcTemplate.query(mpaTableQuery, new MpaRowMapper(), id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }
        return mpa.getFirst();
    }
}