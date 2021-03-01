package nl.hu.cisq1.lingo.trainer.domain.exception;

public class InvalidWordToGuessException extends RuntimeException{
    public InvalidWordToGuessException(String message) {
        super(message);
    }
}
