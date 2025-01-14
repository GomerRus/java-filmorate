package ru.yandex.practicum.filmorate.service.memory;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryFilmService implements FilmService {
    private final Map<Long, Film> filmsService = new HashMap<>();
    private long id = 0L;

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsService.values());
    }

    @Override
    public void addFilm(Film film) {
        if (filmsService.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id: " + film.getId() + " уже существует");
        }
        film.setId(++id);
        filmsService.put(id, film);
    }


    @Override
    public void updateFilm(Film film) {
        if (filmsService.containsKey(film.getId())) {
            filmsService.put(film.getId(), film);
        } else {
            throw new NotFoundException("Запрашиваемый фильм " + film.getId() + " для обновления не найден. Повторите попытку");
        }
    }
}
