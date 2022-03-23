package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.tracker.action.*;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.input.StubInput;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class StartUITest {

    @Test
    public void whenCreateItem() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[] {"0", "Item name", "1"}
        );
        MemTracker tracker = new MemTracker();
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new CreateAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(tracker.findAll().get(0).getName(), is("Item name"));
    }

    @Test
    public void whenReplaceItem() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        /* Добавим в tracker новую заявку */
        Item item = tracker.add(new Item("Replaced item"));
        /* Входные данные должны содержать ID добавленной заявки item.getId() */
        String replacedName = "New item name";
        Input in = new StubInput(
                new String[] {"0", String.valueOf(item.getId()), replacedName, "1"}
        );
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new ReplaceAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(tracker.findById(item.getId()).getName(), is(replacedName));
    }

    @Test
    public void whenDeleteItem() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        /* Добавим в tracker новую заявку */
        Item item = tracker.add(new Item("Deleted item"));
        /* Входные данные должны содержать ID добавленной заявки item.getId() */
        Input in = new StubInput(
                new String[] {"0", String.valueOf(item.getId()), "1"}
        );
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new DeleteAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(tracker.findById(item.getId()), is(nullValue()));
    }

    @Test
    public void whenFindAll() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        Item item = tracker.add(new Item("An item"));
        String expected = "Menu." + System.lineSeparator()
                + "0. Show all items" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== All Items of the Tracker ====" + System.lineSeparator()
                + item.toString() + System.lineSeparator()
                + "Menu." + System.lineSeparator()
                + "0. Show all items" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== Goodbye! ====" + System.lineSeparator();
        Input in = new StubInput(
                new String[] {"0", "1"}
        );
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new ShowAllAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(out.toString(), is(expected));
    }

    @Test
    public void whenFindById() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        Item item = tracker.add(new Item("An item"));
        String expected = "Menu." + System.lineSeparator()
                + "0. Find item by Id" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== Find item by Id ====" + System.lineSeparator()
                + item.toString() + System.lineSeparator()
                + "Menu." + System.lineSeparator()
                + "0. Find item by Id" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== Goodbye! ====" + System.lineSeparator();
        Input in = new StubInput(
                new String[] {"0", "1", "1"}
        );
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new SearchByIdAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(out.toString(), is(expected));
    }

    @Test
    public void whenFindByName() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        Item item = tracker.add(new Item("An item"));
        String expected = "Menu." + System.lineSeparator()
                + "0. Find items by name" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== Find items by name ====" + System.lineSeparator()
                + item.toString() + System.lineSeparator()
                + "Menu." + System.lineSeparator()
                + "0. Find items by name" + System.lineSeparator()
                + "1. Exit Program" + System.lineSeparator()
                + "=== Goodbye! ====" + System.lineSeparator();
        Input in = new StubInput(
                new String[] {"0", "An item", "1"}
        );
        List<UserAction> actions = new ArrayList<>(Arrays.asList(
                new SearchByNameAction(out),
                new ExitAction(out)
        ));
        new StartUI(out).init(in, tracker, actions);
        assertThat(out.toString(), is(expected));
    }

    @Test
    public void whenInvalidExit() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        List<UserAction> actions = new ArrayList<>();
        actions.add(new ExitAction(out));
        Input input = new StubInput(new String[] {"1", "0"});
        String expected = "Menu." + System.lineSeparator()
                + "0. Exit Program" + System.lineSeparator()
                + "Wrong input, you can select: 0 .. " + (actions.size() - 1)
                + System.lineSeparator()
                + "Menu." + System.lineSeparator()
                + "0. Exit Program" + System.lineSeparator()
                + "=== Goodbye! ====" + System.lineSeparator();
        new StartUI(out).init(input, tracker, actions);
        assertThat(out.toString(), is(expected));
    }
}