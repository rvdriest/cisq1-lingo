package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Feedback {
    private String attempt;
    private String wordToGuess;
    private List<Mark> marks;

    public Feedback(String attempt, String wordToGuess) throws InvalidFeedbackException {
        if(wordToGuess.length() != attempt.length()) throw new InvalidFeedbackException("Length of attempt doesn't equal wordToGuess");
        this.wordToGuess = wordToGuess.toLowerCase();
        this.attempt = attempt.toLowerCase();
        this.marks = new ArrayList<>();
        setMarks();
    }

    public Hint giveHint(Hint previousHint) {
        List<Character> hint = new ArrayList<>();
        hint.add(wordToGuess.charAt(0));
        for(int i = 1; i < attempt.length(); i++) {
            if(this.marks.get(i).equals(Mark.CORRECT)) {
                hint.add(attempt.charAt(i));
            }else{
                hint.add(previousHint.getValue().get(i));
            }
        }
        return new Hint(hint);
    }

    public Hint giveHint() {
        List<Character> hint = new ArrayList<>();
        hint.add(wordToGuess.charAt(0));
        for(int i = 1; i < attempt.length(); i++) {
            if(this.marks.get(i).equals(Mark.CORRECT)) {
                hint.add(attempt.charAt(i));
            } else {
                hint.add('.');
            }
        }
        return new Hint(hint);
    }

    private void setMarks() {
        List<Mark> marks = new ArrayList<>();
        List<Character> attemptCharacters = attempt.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        List<Character> wordToGuessCharacters = wordToGuess.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        for(int i = 0; i < attempt.length(); i++) {
            char attemptChar = attemptCharacters.get(i);
            if(attemptChar == wordToGuessCharacters.get(i)) {
                marks.add(Mark.CORRECT);
            }else if(wordToGuessCharacters.stream().anyMatch(character -> character.equals(attemptChar))) {
                marks.add(Mark.PRESENT);
            } else {
                marks.add(Mark.ABSENT);
            }
        }
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(mark -> mark.equals(Mark.CORRECT));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(marks, feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "marks=" + marks +
                '}';
    }
}
