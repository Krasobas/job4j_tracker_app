package ru.job4j.tracker.store;

import ru.job4j.tracker.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MemTracker implements Store {
    private final List<Item> items = new ArrayList<>();
    private int ids = 1;

    @Override
    public Item add(Item item) {
        item.setId(ids++);
        items.add(item);
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean rsl;
        int index = indexOf(id);
        rsl = index >= 0;
        if (rsl) {
            item.setId(id);
            items.set(index, item);
        }
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl;
        int index = indexOf(id);
        rsl = index >= 0;
        if (rsl) {
            items.remove(index);
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> keys = new ArrayList<>();
        for (Item item : items) {
            String temp = item.getName();
            if (temp.equals(key)) {
                keys.add(item);
            }
        }
        return keys;
    }

    private int indexOf(int id) {
        int rsl = -1;
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getId() == id) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }

    @Override
    public Item findById(int id) {
        int index = indexOf(id);
        return index != -1 ? items.get(index) : null;
    }
}