package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Hint {
    private final List<Character> value;

    public Hint(List<Character> value) {
        this.value = new ArrayList<>();
        for(Character character : value) {
            this.value.add(character.toString().toUpperCase().charAt(0));
        }
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

    @Override
    public String toString() {
        return value.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
