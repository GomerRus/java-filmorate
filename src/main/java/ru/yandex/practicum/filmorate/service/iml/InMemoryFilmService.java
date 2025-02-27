package ru.yandex.practicum.filmorate.service.iml;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        film.setGenres(genreStorage.getGenres(film));
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        return filmStorage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        film.setGenres(genreStorage.getGenres(film));
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public void addLikeFilm(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLikeFilm(Long filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilm(Integer countSize) {
        return filmStorage.getPopularFilmOnLike(countSize);
    }
}