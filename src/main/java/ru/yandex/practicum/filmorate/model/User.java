package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.NoSpaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class User {
    private Long id;
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
    private List<Long> friends = new ArrayList<>();
}
