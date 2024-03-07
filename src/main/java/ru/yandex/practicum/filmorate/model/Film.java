package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.AfterDate;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @AfterDate(current = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
