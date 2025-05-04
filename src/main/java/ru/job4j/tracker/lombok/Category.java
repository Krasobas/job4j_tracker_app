package ru.job4j.tracker.lombok;

import lombok.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {
    @NonNull
    @EqualsAndHashCode.Include
    private int id;
    @Setter
    private String name;
}
