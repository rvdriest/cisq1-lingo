package nl.hu.cisq1.lingo.trainer.domain.exception;

public class RoundIsActiveException extends RuntimeException{
    public RoundIsActiveException(String message) {
        super(message);
    }
}
