package ru.job4j.tracker.model;

import java.util.Comparator;

public class ItemDescendingSortByName implements Comparator<Item> {
    @Override
    public int compare(Item first, Item second) {
        return second.getName().compareTo(first.getName());
    }
}
