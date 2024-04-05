package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPADAO mpadao;

    public MPA findById(Long id) throws NotFoundException {
        return mpadao.findById(id).orElseThrow(() -> new NotFoundException("Id " + id + "not found"));
    }

    public List<MPA> getAll() {
        return mpadao.getAll();
    }
}
