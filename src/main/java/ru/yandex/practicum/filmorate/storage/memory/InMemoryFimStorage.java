package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFimStorage implements FilmStorage {
    private final Map<Long, Film> filmsStorage = new HashMap<>();
    private long id = 0L;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsStorage.values());
    }

    @Override
    public void addFilm(Film film) {
        if (filmsStorage.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id: " + film.getId() + " уже существует");
        }
        film.setId(++id);
        filmsStorage.put(id, film);
    }


    @Override
    public void updateFilm(Film film) {
        if (filmsStorage.containsKey(film.getId())) {
            filmsStorage.put(film.getId(), film);
        } else {
            throw new NotFoundException("Запрашиваемый фильм " + film.getId() + " для обновления не найден. Повторите попытку");
        }
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Вы не указали ID фильма. Повторите запрос.");
        }
        if (!filmsStorage.containsKey(filmId)) {
            throw new NotFoundException("Фильм с ID: " + filmId + " не найден.");
        }
        return filmsStorage.get(filmId);
    }
}
