package ru.job4j.tracker.action;

import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.Store;

public class DeleteAction implements UserAction {
    private final Output out;

    public DeleteAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "Delete item";
    }

    @Override
    public boolean execute(Input input, Store tracker) {
        out.println("=== Delete item ====");
        int id = input.askInt("Enter Id of Item you would like to delete: ");
        if (tracker.delete(id)) {
            out.println("Item was successfully deleted!");
        } else {
            out.println("Something went wrong, try again!");
        }
        return true;
    }
}
