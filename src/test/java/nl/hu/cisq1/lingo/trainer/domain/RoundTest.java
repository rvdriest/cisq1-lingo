package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    private Round round;

    @BeforeEach
    void createNewRound() {
            round = new Round("hoofd");
    }

    @Test
    @DisplayName("Guess throws exception when round is already won")
    void roundAlreadyWonException() {
        round.guess("hoofd");

        assertThrows(InvalidGuessException.class, () -> round.guess("hallo"));
    }
    @Test
    @DisplayName("Guess throws exception when round is already lost")
    void roundAlreadyLostException() {
        for(int i = 0; i < 5; i++) {
            round.guess("hallo");
        }
        assertThrows(InvalidGuessException.class, () -> round.guess("hallo"));
    }

    @Test
    @DisplayName("Round is won when word is guessed within 5 rounds")
    void roundWon() {
        round.guess("hoofd");

        assertEquals(RoundState.WON, round.getState());
    }

    @Test
    @DisplayName("Round is lost after 5 wrong guesses")
    void roundLost() {
        for(int i = 0; i < 5; i++) {
            round.guess("appel");
        }

        assertEquals(RoundState.LOST, round.getState());
    }

    @Test
    @DisplayName("Round is active when started")
    void roundActiveWhenStarted() {
        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Round is active when word not guessed")
    void roundActiveWhenWordNotGuessed() {
        round.guess("appel");

        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Round is active after wrongly guessing 4 times")
    void roundActiveAfterWordNotGuessedFourTimes() {
        for(int i = 0; i < 4; i++) {
            round.guess("appel");
        }

        assertEquals(RoundState.ACTIVE, round.getState());
    }
    @ParameterizedTest
    @MethodSource("provideWordsWithStartingHint")
    @DisplayName("Starting a new round will reveal first letter")
    void startingNewRoundRevealFirstLetter(String wordToGuess, Hint expectedHint) {
        Round round = new Round(wordToGuess);
        assertEquals(expectedHint, round.getInitialHint());
    }

    static Stream<Arguments> provideWordsWithStartingHint() {
        return Stream.of(
                Arguments.of("appel", new Hint(List.of('a','.','.','.','.'))),
                Arguments.of("Deur", new Hint(List.of('d','.','.','.'))),
                Arguments.of("dE", new Hint(List.of('d','.')))
        );
    }
}