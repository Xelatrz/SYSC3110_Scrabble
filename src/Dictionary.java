import java.util.*;
import java.io.*;

public class Dictionary {
    private List<String> acceptedWords;

    public Dictionary() {
        acceptedWords = new ArrayList<>();
    }

    /**
     * Loads words from a csv file containing one word per line.
     * @param file Path to csv file containing accepted words
     */
    public void load(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { //opens file for reading based on input parameter with path to the desired file
            String word;
            while ((word = br.readLine()) != null) { //reads one line
                word = word.trim(); //remove undesired trailing characters (spaces)
                if (!word.isEmpty()) {

                    //MAY want to check for duplicates first

                    acceptedWords.add(word.toLowerCase()); //add word to dictionary
                }
            }
        } catch (IOException e) { //catch I/O exceptions and print an error message
            e.printStackTrace();
        }
    }

    /**
     * Checks if a word is an accepted word.
     * @param word The word to be checked
     * @return true if the word is an accepted word
     */
    public boolean checkWord(String word) {
        if (word == null) {
            return false;
        }
        return acceptedWords.contains(word.toLowerCase());
    }
}