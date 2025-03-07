package ru.yandex.practicum.filmorate.service.iml;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaStorage storage;

    @Override
    public List<Mpa> getAllMpa() {
        return storage.getAllMpa();
    }

    @Override
    public Mpa getMpaById(int id) {
        log.info("Получаем рейтинг фильма по ID: {}" + id);
        return storage.getMpaById(id);
    }
}