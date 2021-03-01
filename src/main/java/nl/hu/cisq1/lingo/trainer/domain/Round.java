package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Round {
    private String wordToGuess;
    private List<Feedback> guesses;
    private RoundState state;

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess.toLowerCase();
        this.state = RoundState.ACTIVE;
        this.guesses = new ArrayList<>();
    }

    public Hint getInitialHint() {
        List<Character> hintChars = new ArrayList(List.of(wordToGuess.charAt(0)));
        for(int i = 0; i < wordToGuess.length() - 1; i++) {
            hintChars.add('.');
        }
        return new Hint(hintChars);
    }

    public Feedback guess(String guess) throws InvalidGuessException{
        if(this.state != RoundState.ACTIVE) throw new InvalidGuessException("Deze ronde is al afgelopen");
        Feedback feedback = new Feedback(guess, wordToGuess);
        this.guesses.add(feedback);
        if(feedback.isWordGuessed()) {
            this.state = RoundState.WON;
        }else if(!feedback.isWordGuessed() && this.guesses.size() == 5) {
            this.state = RoundState.LOST;
        }
        return feedback;
    }

    public RoundState getState() {
        return state;
    }
}
