package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.model.ItemAscendingSortById;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ItemAscendingSortByIdTest {

    @Test
    public void whenAscendingSortById() {
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
                first,
                second,
                third,
                fourth
        );
        Collections.sort(items, new ItemAscendingSortById());
        assertThat(items, is(expected));
    }
}