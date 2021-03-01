package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;
import java.util.Objects;

public class Hint {
    private final List<Character> value;

    public Hint(List<Character> value) {
        this.value = value;
    }

    public List<Character> getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint = (Hint) o;
        return Objects.equals(value, hint.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
