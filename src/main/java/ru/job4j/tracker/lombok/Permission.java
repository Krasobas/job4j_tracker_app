package ru.job4j.tracker.lombok;

import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(builderMethodName = "of")
public class Permission {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    @Singular("rule")
    private List<String> rules;
}
