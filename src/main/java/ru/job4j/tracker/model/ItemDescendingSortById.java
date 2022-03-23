package ru.job4j.tracker.model;

import java.util.Comparator;

public class ItemDescendingSortById implements Comparator<Item> {
    @Override
    public int compare(Item first, Item second) {
        return Integer.compare(second.getId(), first.getId());
    }
}
