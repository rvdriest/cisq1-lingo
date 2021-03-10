package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessException;
import org.junit.jupiter.api.BeforeAll;
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
    private ScoreCalculator defaultScoreCalculator;

    @BeforeEach
    void createNewRound() {
        defaultScoreCalculator = new DefaultScoreCalculator();
        round = new Round("hoofd", defaultScoreCalculator);
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
    @DisplayName("Guess throws error if length of attempt doesn't equal length of wordToGuess")
    void invalidGuessException() {
        assertThrows(InvalidGuessException.class, () -> round.guess("appeltje"));
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
            round.guess("haard");
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
        round.guess("helpt");

        assertEquals(RoundState.ACTIVE, round.getState());
    }

    @Test
    @DisplayName("Round is active after wrongly guessing 4 times")
    void roundActiveAfterWordNotGuessedFourTimes() {
        for(int i = 0; i < 4; i++) {
            round.guess("helpt");
        }

        assertEquals(RoundState.ACTIVE, round.getState());
    }
    @Test
    @DisplayName("Starting a new round will reveal first letter")
    void startingNewRoundRevealFirstLetter() {
        assertEquals(round.giveHint(), new Hint(List.of('h','.','.','.','.')));
    }

    //
    @ParameterizedTest
    @MethodSource("provideAllWrongHintExamples")
    @DisplayName("Hint only contains dots if all letters are different except first letter")
    void hintOnlyContainsDots(Round round, Hint expectedHint) {
        assertEquals(expectedHint, round.giveHint());
    }

    @ParameterizedTest
    @MethodSource("provideCorrectHintExamples")
    @DisplayName("Hint doesn't contain any dots if all letters are the same")
    void hintContainsNoDots(Round round) {
        assertTrue(round.giveHint().getValue().stream().noneMatch(hint -> hint.equals('.')));
    }

    @Test
    @DisplayName("Generated hint is based on last hint")
    void hintLooksBackRounds() {
        round.guess("horde");
        round.guess("helpt");
        assertEquals("HO...", round.giveHint().toString());
    }

    @Test
    @DisplayName("Can't guess a word where first letter isn't correct")
    void firstCharacterException() {
        assertThrows(InvalidGuessException.class, () -> round.guess("appel"));
    }

    static Stream<Arguments> provideWordsWithStartingHint() {
        return Stream.of(
                Arguments.of("appel", new Hint(List.of('a','.','.','.','.'))),
                Arguments.of("Deur", new Hint(List.of('d','.','.','.'))),
                Arguments.of("dE", new Hint(List.of('d','.')))
        );
    }

    static Stream<Arguments> provideAllWrongHintExamples() {
        Round round1 = new Round("woord", new DefaultScoreCalculator());
        Round round2 = new Round("deur", new DefaultScoreCalculator());
        Round round3 = new Round("de", new DefaultScoreCalculator());

        round1.guess("water");
        round2.guess("dook");
        round3.guess("dl");

        return Stream.of(
                Arguments.of(round1, new Hint(List.of('w','.','.','.','.'))),
                Arguments.of(round2, new Hint(List.of('d','.','.','.'))),
                Arguments.of(round3,new Hint(List.of('d','.')))
        );
    }

    static Stream<Arguments> provideCorrectHintExamples() {
        Round round1 = new Round("woord", new DefaultScoreCalculator());
        Round round2 = new Round("deur", new DefaultScoreCalculator());
        Round round3 = new Round("de", new DefaultScoreCalculator());

        round1.guess("woord");
        round2.guess("deur");
        round3.guess("de");

        return Stream.of(
                Arguments.of(round1),
                Arguments.of(round2),
                Arguments.of(round3)
        );
    }

    static Stream<Arguments> provideRoundHintExamples() {
        return Stream.of(
                Arguments.of(new Feedback("woord", "appel"), "a....", null),
                Arguments.of(new Feedback("anger", "appel"), "a..e.", new Hint(List.of('.', '.', '.', '.', '.'))),
                Arguments.of(new Feedback("kupus", "appel"), "a.pe.", new Hint(List.of('a', '.', '.', 'e', '.'))),
                Arguments.of(new Feedback("appel", "appel"), "appel", new Hint(List.of('a', '.', 'p', 'e', '.')))
        );
    }
}