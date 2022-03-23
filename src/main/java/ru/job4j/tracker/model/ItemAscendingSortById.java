package ru.job4j.tracker.model;

import java.util.Comparator;

public class ItemAscendingSortById implements Comparator<Item> {
    @Override
    public int compare(Item first, Item second) {
        return Integer.compare(first.getId(), second.getId());
    }
}
