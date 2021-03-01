package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord", "woord");

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord", "appel");

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("feedback is same if values are the same")
    void feedbackIsSame() {
        Feedback feedbackA = new Feedback("en","en");
        Feedback feedbackB = new Feedback("en", "en");

        assertEquals(feedbackA, feedbackB);
    }
    @Test
    @DisplayName("feedback is different if values are different")
    void feedbackIsDifferent() {
        Feedback feedbackA = new Feedback("en", "en");
        Feedback feedbackB = new Feedback("en","nl");

        assertNotEquals(feedbackA, feedbackB);
    }

    @Test
    @DisplayName("hashcode is generated based on values")
    void hashcodeGeneration() {
        Feedback feedbackA = new Feedback("en", "en");
        Feedback feedbackB = new Feedback("en", "en");

        assertEquals(feedbackA.hashCode(), feedbackB.hashCode());
    }

    @Test
    @DisplayName("Feedback throws error if wordToGuess length is not equal to attempt length")
    void markSizeNotEqualWordLength() {
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", "woordje")
        );
    }

    @Test
    @DisplayName("Feedback doesn't throw error if wordToGuess length is equal to attempt length")
    void markSizeEqualWordLength() {
        assertDoesNotThrow(
                () -> new Feedback("woord", "appel")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllWrongHintExamples")
    @DisplayName("Hint only contains dots if all letters are different")
    void hintOnlyContainsDots(Feedback feedback) {
        assertTrue(feedback.giveHint().getValue().stream().allMatch(hint -> hint.equals('.')));
    }

    @ParameterizedTest
    @MethodSource("provideCorrectHintExamples")
    @DisplayName("Hint doesn't contain any dots if all letters are the same")
    void hintNoneContainsDots(Feedback feedback) {
        assertTrue(feedback.giveHint().getValue().stream().noneMatch(hint -> hint.equals('.')));
    }

    @ParameterizedTest
    @MethodSource("provideRoundHintExamples")
    @DisplayName("Hint doesn't contain any dots if all letters are the same")
    void roundHints(Feedback feedback, String hint, Hint previousHint) {
        String newHint;
        if(previousHint != null) {
            newHint = feedback.giveHint(previousHint).getValue().stream().map(String::valueOf).collect(Collectors.joining());
        }else {
            newHint = feedback.giveHint().getValue().stream().map(String::valueOf).collect(Collectors.joining());
        }

        assertEquals(newHint, hint);
    }

    static Stream<Arguments> provideAllWrongHintExamples() {
        return Stream.of(
                Arguments.of(new Feedback("appel", "woord")),
                Arguments.of(new Feedback("deur", "pink")),
                Arguments.of(new Feedback("de", "op"))
        );
    }

    static Stream<Arguments> provideCorrectHintExamples() {
        return Stream.of(
                Arguments.of(new Feedback("appel", "ApPel")),
                Arguments.of(new Feedback("deur", "deur")),
                Arguments.of(new Feedback("de", "de"))
        );
    }

    static Stream<Arguments> provideRoundHintExamples() {
        return Stream.of(
                Arguments.of(new Feedback("woord", "appel"), ".....", null),
                Arguments.of(new Feedback("anger", "appel"), "a..e.", new Hint(List.of('.', '.', '.', '.', '.'))),
                Arguments.of(new Feedback("kupus", "appel"), "a.pe.", new Hint(List.of('a', '.', '.', 'e', '.'))),
                Arguments.of(new Feedback("appel", "appel"), "appel", new Hint(List.of('a', '.', 'p', 'e', '.')))
        );
    }
}