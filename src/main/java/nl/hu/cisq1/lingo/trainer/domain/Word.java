package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Word {
    private String value;
    private List<Letter> _letters;

    public Word(String value){
        this.value = value;
        _letters = new ArrayList<Letter>();
        setLetters(value);
    }
    private void setLetters(String value){
        List<Character> characters = value.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        for(int i = 0; i < characters.size(); i++) {
            Letter letter = new Letter(characters.get(i), i);
            _letters.add(letter);
        }
    }

    public List<Letter> getLetters(){
        return _letters;
    }

    public List<Integer> getPositions(Character character){
        List<Integer> clist = new ArrayList<>();
        for(Letter letter : _letters){
            if (letter.getLetter().equals(character)){
                clist.add(letter.getPosition());
            }
        }
        return clist;
    }
}
