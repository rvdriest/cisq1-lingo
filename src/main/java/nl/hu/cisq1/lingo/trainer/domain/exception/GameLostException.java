package nl.hu.cisq1.lingo.trainer.domain.exception;

public class GameLostException extends RuntimeException{
    public GameLostException(String message) {
        super(message);
    }
}
