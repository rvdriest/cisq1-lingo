package nl.hu.cisq1.lingo.trainer.domain;

public class DefaultScoreCalculator implements ScoreCalculator{
    @Override
    public int calculateScore(int amountOfAttempts) {
        return 5*(5-amountOfAttempts)+5;
    }
}
