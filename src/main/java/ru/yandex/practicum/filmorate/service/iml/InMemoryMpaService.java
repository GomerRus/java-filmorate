package ru.yandex.practicum.filmorate.service.iml;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryMpaService implements MpaService {

    private final MpaStorage storage;

    @Override
    public List<Mpa> getAllMpa() {
        return storage.getAllMpa();
    }

    @Override
    public Mpa getMpaById(int id) {
        return storage.getMpaById(id);
    }
}