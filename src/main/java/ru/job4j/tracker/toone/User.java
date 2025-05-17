package ru.job4j.tracker.toone;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import ru.job4j.tracker.model.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "j_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "j_user_id")
    private List<UserMessenger> messengers = new ArrayList<>();

    @ManyToMany(mappedBy = "participates")
    private Set<Item> tasks = new HashSet<>();
}
