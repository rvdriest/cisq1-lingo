package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.*;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private int score;
    private int wordLength;
    private Round currentRound;
    private ScoreCalculator scoreCalculator;

    public Game(ScoreCalculator scoreCalculator) {
        this.score = 0;
        this.wordLength = 5;
        this.scoreCalculator = scoreCalculator;
    }

    public Feedback guess(String guess) {
        if(currentRound == null) throw new RoundIsNullException("Er is nog geen ronde gestart");
        if(!currentRound.getState().equals(RoundState.ACTIVE)) throw new InvalidGuessException(String.format("U kunt geen guess doen terwijl de staat van de ronde gelijk is aan %s", currentRound.getState().toString()));
        Feedback feedback = currentRound.guess(guess);
        calculateScore();
        return feedback;
    }

    private void calculateScore() {
        if(currentRound.getState().equals(RoundState.WON)) {
            this.score += currentRound.calculateScore();
        }
    }

    public Hint startNewRound(String wordToGuess) {
        if(currentRound != null) {
            if (currentRound.getState().equals(RoundState.ACTIVE))
                throw new RoundIsActiveException("A round is still active");
            if (currentRound.getState().equals(RoundState.LOST))
                throw new GameLostException("You lost this game. Start a new game");
        }
        if(wordToGuess.length() != this.wordLength) throw new InvalidWordToGuessException(String.format("Word to guess length must equal %d", this.wordLength));
        currentRound = new Round(wordToGuess, scoreCalculator);
        setNewWordLength();
        return currentRound.giveHint();
    }

    private void setNewWordLength() {
        if(this.wordLength == 7) {
            this.wordLength = 5;
        }else {
            this.wordLength += 1;
        }
    }

    public int getScore() {
        return score;
    }

    public Round getCurrentRound() {
        return currentRound;
    }
}
