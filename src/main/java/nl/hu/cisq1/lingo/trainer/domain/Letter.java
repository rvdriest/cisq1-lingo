package nl.hu.cisq1.lingo.trainer.domain;

public class Letter {
    private Character letter;
    private int position;

    public Letter(Character letter, int position){
        this.letter = letter;
        this.position = position;
    }

    public Character getLetter() {
        return letter;
    }

    public int getPosition() {
        return position;
    }
}
