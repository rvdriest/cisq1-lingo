package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Hint {
    private final List<Character> value;

    public Hint(List<Character> value) {
        this.value = value;
    }

    public List<Character> getValue() {
        return value;
    }
}
