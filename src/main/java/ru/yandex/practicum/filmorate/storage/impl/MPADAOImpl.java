package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADAO;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MPADAOImpl implements MPADAO {

    private final JdbcTemplate jdbcTemplate;
    private static final String GET_ALL_MPA_SQL = "SELECT id, name FROM mpa ORDER BY id";
    private static final String FIND_BY_ID_SQL = "SELECT id, name FROM mpa WHERE id = ?";

    @Override
    public List<MPA> getAll() {
        return jdbcTemplate.query(GET_ALL_MPA_SQL, (rs, rowNum) -> MPA.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }

    @Override
    public Optional<MPA> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, (rs, rowNum) -> MPA.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build(), id).stream().findFirst();
    }
}
