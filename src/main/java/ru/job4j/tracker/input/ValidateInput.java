package ru.job4j.tracker.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.output.Output;

public class ValidateInput implements Input {
    private static final Logger LOG = LoggerFactory.getLogger(ValidateInput.class.getName());
    private Input in;
    private Output out;

    public ValidateInput(Output out, Input in) {
        this.in = in;
        this.out = out;
    }

    @Override
    public String askStr(String question) {
        return in.askStr(question);
    }

    @Override
    public int askInt(String question) {
        int value = -1;
        boolean invalid = true;
        do {
            try {
                value = in.askInt(question);
                invalid = false;
            } catch (NumberFormatException nfe) {
                LOG.info("Only integer value expected. Please enter validate data again.");
            }
        } while (invalid);
        return value;
    }
}
