package ru.job4j.tracker.mapstruct;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentDto {
    private int id;
    private String name;
    private String className;
}
