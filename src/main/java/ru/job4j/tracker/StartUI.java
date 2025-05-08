package ru.job4j.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.action.*;
import ru.job4j.tracker.input.ConsoleInput;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.input.ValidateInput;
import ru.job4j.tracker.output.ConsoleOutput;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.HbmTracker;
import ru.job4j.tracker.store.Store;

import java.util.List;

public class StartUI {
    private static final Logger LOG = LoggerFactory.getLogger(StartUI.class.getName());
    private final Output out;

    public StartUI(Output out) {
        this.out = out;
    }

    public void init(Input input, Store tracker, List<UserAction> actions) {
        boolean run = true;
        while (run) {
            this.showMenu(actions);
            int select = input.askInt("Select: ");
            if (select < 0 || select >= actions.size()) {
                out.println("Wrong input, you can select: 0 .. " + (actions.size() - 1));
                continue;
            }
            run = actions.get(select).execute(input, tracker);
        }
    }

    private void showMenu(List<UserAction> actions) {
        out.println("Menu.");
        for (int index = 0; index < actions.size(); index++) {
            out.println(index + ". " + actions.get(index).name());
        }
    }

    public static void main(String[] args) {
        Output output = new ConsoleOutput();
        try (ConsoleInput ci = new ConsoleInput(output); HbmTracker tracker = new HbmTracker()) {
            Input input = new ValidateInput(output, ci);
            List<UserAction> actions = List.of(
                    new CreateAction(output),
                    new ReplaceAction(output),
                    new DeleteAction(output),
                    new ShowAllAction(output),
                    new SearchByIdAction(output),
                    new SearchByNameAction(output),
                    new ExitAction(output)
            );
            new StartUI(output).init(input, tracker, actions);
        } catch (Exception e) {
            LOG.error("Tracker couldn't be initialised.", e);
        }
    }
}