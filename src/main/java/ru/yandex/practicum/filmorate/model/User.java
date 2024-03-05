package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class User {
    private int id;
    private String name;
    @Email
    private String email;
    @NotNull
    @NotBlank
    @NoSpaces
    private String login;
    @Past
    @NotNull
    private LocalDate birthday;
}
