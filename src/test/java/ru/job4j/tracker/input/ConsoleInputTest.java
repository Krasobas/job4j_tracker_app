package ru.job4j.tracker.input;


import org.junit.Test;
import ru.job4j.tracker.action.*;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConsoleInputTest {

    @Test
    public void whenItemWasReplacedSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("Replaced item"));
        String replacedName = "New item name";
        ReplaceAction replaceAction = new ReplaceAction(output);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(1);
        when(input.askStr(any(String.class))).thenReturn(replacedName);

        replaceAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Edit item ====" + ln
                        + "Item was successfully edited!" + ln
        );
    }

    @Test
    public void whenItemWasNotReplaced() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("Replaced item"));
        ReplaceAction replaceAction = new ReplaceAction(output);

        Input input = mock(Input.class);
        doReturn("").when(input).askStr(anyString());

        replaceAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Edit item ====" + ln
                        + "Something went wrong, try again!" + ln
        );
    }

    @Test
    public void whenItemWasCreatedSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "Created item";
        CreateAction createAction = new CreateAction(output);

        Input input = mock(Input.class);

        when(input.askStr(any(String.class))).thenReturn(name);

        createAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Create a new Item ====" + ln
                        + "Item was successfully created!" + ln
        );
        assertThat(tracker.findAll().get(0).getName()).isEqualTo(name);
    }

    @Test
    public void whenItemWasDeletedSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "Created item";
        tracker.add(new Item(name));
        DeleteAction deleteAction = new DeleteAction(output);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(1);

        deleteAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Delete item ====" + ln
                        + "Item was successfully deleted!" + ln
        );
        assertThat(tracker.findAll().isEmpty()).isTrue();
    }

    @Test
    public void whenItemWasNotDeleted() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "Created item";
        tracker.add(new Item(name));
        DeleteAction deleteAction = new DeleteAction(output);

        Input input = mock(Input.class);

        deleteAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Delete item ====" + ln
                        + "Something went wrong, try again!" + ln
        );
        assertThat(tracker.findAll().isEmpty()).isFalse();
    }

    @Test
    public void whenExitWasCalledSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        ExitAction exitAction = new ExitAction(output);

        Input input = mock(Input.class);

        exitAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo("=== Goodbye! ====" + ln);
    }

    @Test
    public void whenItemWasFoundByIdSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "New item name";
        Item item = new Item(name);
        tracker.add(item);
        SearchByIdAction searchByIdAction = new SearchByIdAction(output);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(1);

        searchByIdAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Find item by Id ====" + ln
                        + item + ln
        );
    }

    @Test
    public void whenItemWasNotFoundById() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        SearchByIdAction searchByIdAction = new SearchByIdAction(output);

        Input input = mock(Input.class);

        searchByIdAction.execute(input, tracker);

        verify(input).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Find item by Id ====" + ln
                        + "There is no any Item with this Id in the Tracker." + ln
        );
    }

    @Test
    public void whenItemsWereFoundByNameSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "New item name";
        Item item = new Item(name);
        Item item2 = new Item(name);
        tracker.add(item);
        tracker.add(item2);
        SearchByNameAction searchByNameAction = new SearchByNameAction(output);

        Input input = mock(Input.class);

        when(input.askStr(any(String.class))).thenReturn(name);

        searchByNameAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Find items by name ====" + ln
                        + item + ln + item2 + ln
        );
    }

    @Test
    public void whenItemWasNotFoundByName() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        SearchByNameAction searchByNameAction = new SearchByNameAction(output);

        Input input = mock(Input.class);

        searchByNameAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== Find items by name ====" + ln
                        + "There is no any Item with this name in the Tracker." + ln
        );
    }

    @Test
    public void whenItemsWereListedSuccessfully() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        String name = "New item name";
        Item item = new Item(name);
        Item item2 = new Item(name);
        tracker.add(item);
        tracker.add(item2);
        ShowAllAction showAllAction = new ShowAllAction(output);

        Input input = mock(Input.class);

        showAllAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== All Items of the Tracker ====" + ln
                        + item + ln + item2 + ln
        );
    }

    @Test
    public void whenNoItemsWereFound() {
        Output output = new StubOutput();
        MemTracker tracker = new MemTracker();
        ShowAllAction showAllAction = new ShowAllAction(output);

        Input input = mock(Input.class);

        showAllAction.execute(input, tracker);

        verify(input, never()).askInt(any(String.class));
        verify(input, never()).askStr(any(String.class));

        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo(
                "=== All Items of the Tracker ====" + ln
                        + "There is no any Item in the Tracker." + ln
        );
    }


}