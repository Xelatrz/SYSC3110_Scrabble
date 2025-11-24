import java.util.*;
import java.io.*;

/**
 * A Dictionary models the accepted English words from a standard scrabble game. It loads
 * the accepted words from an external .csv file, allowing for updates to the accepted word list.
 *
 * @author Taylor Brumwell
 * @author Cole Galway
 * @version 11/10/2025
 */
public class Dictionary {
    /**
     * A list of strings which are the accepted words for the game.
     */
    ArrayList<String> acceptedWords;

    /**
     * Constructs a new Dictionary, taking no parameters.
     */
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

    /**
     * Manually adds words into the accepted word list.
     * @param word A String containing the word to be added.
     */
    public void addWord(String word) {
        acceptedWords.add(word.toLowerCase());
    }

    /**
     * Finds the size of the dictionary
     * @return Dictionary size.
     */
    public int size() {
        return acceptedWords.size();
    }

    /**
     * Finds the word at the give index
     * @param index The index of the desired word.
     * @return The desired word at the given index
     */
    public String getWord(int index) {
        return acceptedWords.get(index);
    }
}