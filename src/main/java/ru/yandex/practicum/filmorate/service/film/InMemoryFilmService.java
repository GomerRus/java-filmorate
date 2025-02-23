package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private FilmStorage filmStorage;
    private UserService userService;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        userService.getUserById(userId);
        filmStorage.getFilmById(filmId).getLikesId().add(userId);
    }

    @Override
    public void deleteLikeFilm(Long filmId, Long userId) {
        userService.getUserById(userId);
        filmStorage.getFilmById(filmId).getLikesId().remove(userId);
    }

    @Override
    public List<Film> getPopularFilm(Integer countSize) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(Film::getAmountLikesFilm).reversed())
                .limit(countSize)
                .collect(Collectors.toList());
    }
}