package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.*;
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

    //TODO: Let erop dat een letter niet ten onrechte als present wordt aangegeven als deze al eerder is aangemerkt als present of correct!
    private void setMarks() {
        List<Mark> marks = new ArrayList<>(attempt.length());
        for(int i = 0; i < attempt.length(); i++) {
            marks.add(null);
        }
        Word attemptWord = new Word(attempt);
        Word wordToGuessWord = new Word(wordToGuess);

        List<Integer> attemptPositionsDone = new ArrayList<>();

        for(int i = 0; i < attemptWord.getLetters().size(); i++){
            Letter letter = attemptWord.getLetters().get(i);
            if(attemptPositionsDone.contains(letter.getPosition())) continue;
            List<Integer> wordToGuessLetterPositions = wordToGuessWord.getPositions(letter.getLetter());
            if (wordToGuessLetterPositions.size() > 0){
                List<Integer> attemptLetterPositions = attemptWord.getPositions(letter.getLetter());
                attemptLetterPositions.removeAll(attemptPositionsDone);
                if(attemptLetterPositions.size() > wordToGuessLetterPositions.size()) {
                    Collections.reverse(attemptLetterPositions);
                    for(int attemptLetterPosition : attemptLetterPositions) {
                        if(!wordToGuessLetterPositions.contains(attemptLetterPosition)) {
                            marks.set(attemptLetterPosition, Mark.ABSENT);
                            attemptPositionsDone.add(attemptLetterPosition);
                            i--;
                            break;
                        }
                    }
                }
                else if (wordToGuessLetterPositions.stream().anyMatch(x -> x == letter.getPosition())) {
                    marks.set(letter.getPosition(), Mark.CORRECT);
                } else {
                    marks.set(letter.getPosition(), Mark.PRESENT);
                }
            } else {
                marks.set(letter.getPosition(), Mark.ABSENT);
            }

        }

        this.marks = marks;
    }

//    private HashMap<Integer, Character> sortHashMapByValues(
//            HashMap<Integer, String> passedMap) {
//        return passedMap.entrySet().stream()
//                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue())).;
//    }

    public String getAttempt() {
        return attempt;
    }

    public List<Mark> getMarks() {
        return marks;
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
