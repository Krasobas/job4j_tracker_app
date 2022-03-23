package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

public class SearchByIdAction implements UserAction {
    private final Output out;

    public SearchByIdAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Find item by Id";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Find item by Id ====");
        int id = input.askInt("Enter Id of Item you would like to find: ");
        Item item = tracker.findById(id);
        if (item != null) {
            out.println(item);
        } else {
            out.println("There is no any Item with this Id in the Tracker.");
        }
        return true;
    }
}

