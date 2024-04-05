package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPAController {

    private final MPAService mpaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<MPA> getMPAById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(mpaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MPA>> getAllMPA() {
        return ResponseEntity.ok(mpaService.getAll());
    }
}
