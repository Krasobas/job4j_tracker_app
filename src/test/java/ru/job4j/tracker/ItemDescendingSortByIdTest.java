package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.model.ItemDescendingSortById;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemDescendingSortByIdTest {

    @Test
    public void whenDescendingSortById() {
        Item first = new Item();
        Item second = new Item();
        Item third = new Item();
        Item fourth = new Item();
        first.setId(1);
        second.setId(2);
        third.setId(3);
        fourth.setId(4);
        List<Item> items = Arrays.asList(
                third,
                second,
                fourth,
                first
        );
        List<Item> expected = Arrays.asList(
                fourth,
                third,
                second,
                first
        );
        Collections.sort(items, new ItemDescendingSortById());
        assertThat(items, is(expected));
    }
}