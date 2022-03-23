package ru.job4j.tracker.model;

import java.util.Comparator;

public class ItemAscendingSortByName implements Comparator<Item> {
    @Override
    public int compare(Item first, Item second) {
        return first.getName().compareTo(second.getName());
    }
}
