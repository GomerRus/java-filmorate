package ru.yandex.practicum.filmorate.service.iml;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryGenreService implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    @Override
    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }
}
