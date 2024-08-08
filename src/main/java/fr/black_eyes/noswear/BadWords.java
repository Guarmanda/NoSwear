package fr.black_eyes.noswear;

import java.util.ArrayList;
import java.util.List;

public class BadWords {
    
    List<String> badWords = new ArrayList<>();


    /**
     * Load bad words from data.yml file
     */
    public BadWords() {
        Main.getInstance().getConfigFiles().getData().getStringList("badwords").forEach(word -> {
            if (word != null) {
                badWords.add(word);
            }
        });
    }

    public List<String> getBadWords() {
        return badWords;
    }

    public void addBadWord(String word) {
        badWords.add(word);
        Main.getInstance().getConfigFiles().getData().set("badwords", badWords);
        Main.getInstance().getConfigFiles().saveData();
    }

    public void removeBadWord(String word) {
        badWords.remove(word);
        Main.getInstance().getConfigFiles().getData().set("badwords", badWords);
        Main.getInstance().getConfigFiles().saveData();
    }




}
