package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;

    @BeforeEach
    void setGame() {
        game = new Game(new DefaultScoreCalculator());
    }

    @Test
    @DisplayName("Game starts with no starting round")
    void gameStartsWithNoStartingRound() {
        assertNull(game.getCurrentRound());
    }

    @Test
    @DisplayName("Game starts with score of 0")
    void gameStartsWithNoScore() {
        assertEquals(0, game.getScore());
    }

    @Test
    @DisplayName("Game can't start with word with length other than 5")
    void gameCantStartWithIncorrectStartingWordLength() {
        assertThrows(InvalidWordToGuessException.class, () -> game.startNewRound("ABC"));
    }

    @Test
    @DisplayName("Game can start with word of length 5")
    void gameStartWithWordLengthFive() {
        assertDoesNotThrow(() -> game.startNewRound("APPEL"));
    }

    @Test
    @DisplayName("Every game word length increments by 1")
    void gameWordLengthIncrements() {
        String FIVE_LETTER_WORD = "APPEL";
        String SIX_LETTER_WORD = "APPELS";
        String SEVEN_LETTER_WORD = "APPELSS";

        game.startNewRound(FIVE_LETTER_WORD);
        game.guess(FIVE_LETTER_WORD);

        assertThrows(InvalidWordToGuessException.class, () -> game.startNewRound(FIVE_LETTER_WORD));
        assertDoesNotThrow(() -> game.startNewRound(SIX_LETTER_WORD));

        game.guess(SIX_LETTER_WORD);

        assertThrows(InvalidWordToGuessException.class, () -> game.startNewRound(FIVE_LETTER_WORD));
        assertDoesNotThrow(() -> game.startNewRound(SEVEN_LETTER_WORD));

        game.guess(SEVEN_LETTER_WORD);

        assertThrows(InvalidWordToGuessException.class, () -> game.startNewRound(SIX_LETTER_WORD));
        assertDoesNotThrow(() -> game.startNewRound(FIVE_LETTER_WORD));
    }

    @Test
    @DisplayName("Game can't start if a round is still active")
    void roundStillActive() {
        game.startNewRound("APPEL");
        assertThrows(RoundIsActiveException.class, () -> game.startNewRound("KUBUS"));
    }

    @Test
    @DisplayName("Game can't start if game is lost")
    void gameIsLostException() {
        game.startNewRound("APPEL");

        for(int i = 0; i < 5; i++) {
            game.guess("ANGEL");
        }

        assertThrows(GameLostException.class, () -> game.startNewRound("KUBUS"));
    }

    @Test
    @DisplayName("Get correct starting hint when starting new round")
    void startingHint() {
        Hint hint = game.startNewRound("APPEL");

        assertEquals(new Hint(List.of('A', '.', '.', '.', '.')), hint);
    }

    @Test
    @DisplayName("First character of guess must equal word to guess")
    void guessFirstCharacterMustEqualWTG() {
        game.startNewRound("KUBUS");

        assertThrows(InvalidGuessException.class, () -> game.guess("APPEL"));
    }

    @Test
    @DisplayName("Length of guess must equal length of word to guess")
    void guessLengthMustEqualWTG() {
        game.startNewRound("KUBUS");

        assertThrows(InvalidGuessException.class, () -> game.guess("KEUR"));
    }

    @Test
    @DisplayName("Can't guess without starting round")
    void guessWithoutStartingRound() {
        assertThrows(RoundIsNullException.class, () -> game.guess("APPEL"));
    }

    @ParameterizedTest
    @MethodSource("provideGuessFeedbacks")
    @DisplayName("Guess gives correct feedback")
    void guessCorrectFeedback(String attempt, String wordToGuess, Feedback expectedFeedback) {
        game.startNewRound(wordToGuess);

        Feedback actualFeedback = game.guess(attempt);
        assertEquals(expectedFeedback, actualFeedback);
    }

    @Test
    @DisplayName("Can't guess if game is lost")
    void guessWhenGameIsLost() {
        game.startNewRound("APPEL");

        for(int i = 0; i < 5; i++) {
            game.guess("APPEN");
        }

        assertThrows(InvalidGuessException.class, () -> game.guess("APPEN"));
    }


    //Test score
    @ParameterizedTest
    @MethodSource("provideRoundForScore")
    @DisplayName("Winning a round gives back correct score")
    void correctScore(int amountOfWrongAttempts, int expectedScore) {
        game.startNewRound("APPEL");
        for(int i = 0; i < amountOfWrongAttempts; i++) {
            game.guess("APPEN");
        }
        game.guess("APPEL");

        assertEquals(expectedScore, game.getScore());

    }

    static Stream<Arguments> provideGuessFeedbacks() {
        return Stream.of(
                Arguments.of("BARST", "BAARD", new Feedback("BARST", "BAARD")),
                Arguments.of("DRAAD", "DAADD", new Feedback("DRAAD", "DAADD")),
                Arguments.of("ZWAAR", "ZAROK", new Feedback("ZWAAR", "ZAROK"))
        );
    }

    static Stream<Arguments> provideRoundForScore() {
        // 5*(5-amountOfAttempts)+5
        return Stream.of(
                Arguments.of(0, 25),
                Arguments.of(1, 20),
                Arguments.of(2, 15),
                Arguments.of(3, 10),
                Arguments.of(4, 5)
        );
    }
}