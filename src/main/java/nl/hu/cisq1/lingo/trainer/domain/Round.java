package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Round {
    private String wordToGuess;
    private List<Feedback> guesses;
    private RoundState state;
    private ScoreCalculator scoreCalculator;
    private Hint lastHint;

    public Round(String wordToGuess, ScoreCalculator scoreCalculator) {
        this.wordToGuess = wordToGuess.toLowerCase();
        this.state = RoundState.ACTIVE;
        this.guesses = new ArrayList<>();
        this.scoreCalculator = scoreCalculator;
        this.lastHint = getInitialHint();
    }

    public Hint giveHint() {
        if(guesses.size() == 0) {
            lastHint = getInitialHint();
        } else {
            Feedback lastFeedback = guesses.get(guesses.size()-1);
            lastHint = getHintFromFeedback(lastFeedback);
        }
        return lastHint;
    }

    private Hint getHintFromFeedback(Feedback feedback) {
        List<Character> hint = new ArrayList<>();
        hint.add(wordToGuess.charAt(0));
        for(int i = 1; i < feedback.getAttempt().length(); i++) {
            if(feedback.getMarks().get(i).equals(Mark.CORRECT)) {
                hint.add(feedback.getAttempt().charAt(i));
            }else{
                hint.add(lastHint.getValue().get(i));
            }
        }
        return new Hint(hint);
    }

    private Hint getInitialHint() {
        List<Character> hintChars = new ArrayList<>(List.of(wordToGuess.charAt(0)));
        for(int i = 0; i < wordToGuess.length() - 1; i++) {
            hintChars.add('.');
        }
        return new Hint(hintChars);
    }

    public Feedback guess(String guess) throws InvalidGuessException{
        if(this.state != RoundState.ACTIVE) throw new InvalidGuessException("Deze ronde is al afgelopen");
        if(guess.length() != wordToGuess.length()) throw new InvalidGuessException("De lengte van uw poging (" + guess.length() + ") komt niet overeen met het te raden woord (" + wordToGuess.length() + ")");
        if(guess.toLowerCase().charAt(0) != wordToGuess.toLowerCase().charAt(0)) throw new InvalidGuessException("Eerste karakter komt niet overeen met het te raden woord");
        Feedback feedback = new Feedback(guess, wordToGuess);
        this.guesses.add(feedback);
        if(feedback.isWordGuessed()) {
            this.state = RoundState.WON;
        }else if(!feedback.isWordGuessed() && this.guesses.size() == 5) {
            this.state = RoundState.LOST;
        }
        this.lastHint = getHintFromFeedback(feedback);

        return feedback;
    }

    public int calculateScore() {
        int amountOfAttempts = guesses.size();
        return scoreCalculator.calculateScore(amountOfAttempts);
    }

    public RoundState getState() {
        return state;
    }

    public List<Feedback> getGuesses() {
        return guesses;
    }
}
