package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPADAOImplTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    void testGetAllMPA() {
        MPA mpa1 = MPA.builder()
                .id(1L)
                .name("G")
                .build();
        MPA mpa2 = MPA.builder()
                .id(2L)
                .name("PG")
                .build();
        MPA mpa3 = MPA.builder()
                .id(3L)
                .name("PG-13")
                .build();
        MPA mpa4 = MPA.builder()
                .id(4L)
                .name("R")
                .build();
        MPA mpa5 = MPA.builder()
                .id(5L)
                .name("NC-17")
                .build();

        List<MPA> mpaList = new ArrayList<>();
        mpaList.add(mpa1);
        mpaList.add(mpa2);
        mpaList.add(mpa3);
        mpaList.add(mpa4);
        mpaList.add(mpa5);

        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);

        List<MPA> savedMPAList = mpadao.getAll();

        assertThat(savedMPAList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpaList);
    }

    @Test
    void testFindById() {
        MPA mpa = MPA.builder()
                .id(1L)
                .name("G")
                .build();

        MPADAO mpadao = new MPADAOImpl(jdbcTemplate);

        MPA savedMPA = mpadao.findById(1L).get();

        assertThat(savedMPA)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpa);
    }
}