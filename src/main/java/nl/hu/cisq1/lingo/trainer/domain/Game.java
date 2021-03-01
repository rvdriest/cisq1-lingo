package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.GameLostException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidWordToGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundIsActiveException;

import java.util.ArrayList;
import java.util.List;


public class Game {
    private int score;
    private int wordLength;
    private Round currentRound;

    public Game() {
        this.score = 0;
        this.wordLength = 5;
    }

    public Hint startGame(String wordToGuess) {
        if(wordToGuess.length() != this.wordLength) throw new InvalidWordToGuessException(String.format("Word to guess length must equal %d", this.wordLength));
        currentRound = new Round(wordToGuess);
        return currentRound.getInitialHint();
    }

    public Feedback guess(String guess) {
        return currentRound.guess(guess);
    }

    public Hint startNewRound(String wordToGuess) {
        if(currentRound.getState().equals(RoundState.ACTIVE)) throw new RoundIsActiveException("A round is still active");
        if(currentRound.getState().equals(RoundState.LOST)) throw new GameLostException("You lost this game. Start a new game");
        setNewWordLength();
        currentRound = new Round(wordToGuess);
        return currentRound.getInitialHint();
    }



    private void setNewWordLength() {
        if(this.wordLength == 7) {
            this.wordLength = 5;
        }else {
            this.wordLength++;
        }
    }
}
