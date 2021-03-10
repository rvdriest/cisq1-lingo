package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
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
    @MethodSource("provideFeedbackMarks")
    void testMarks(Feedback feedback, List<Mark> expectedMarks) {
        assertEquals(expectedMarks, feedback.getMarks());
    }

    static Stream<Arguments> provideFeedbackMarks() {
        return Stream.of(
                Arguments.of(new Feedback("BONJE", "BAARD"), List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of(new Feedback("BARST", "BAARD"), List.of(Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of(new Feedback("DRAAD", "BAADD"), List.of(Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT)),
                Arguments.of(new Feedback("ZWAAR", "BAROK"), List.of(Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT)),
                Arguments.of(new Feedback("APAALM", "AARDEN"), List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of(new Feedback("BAARD", "BAARD"), List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of(new Feedback("ARARA", "BAROK"), List.of(Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of(new Feedback("BAROK", "ARARA"), List.of(Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT))
        );
    }
}