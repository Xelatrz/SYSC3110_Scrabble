import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

    @Test
    @DisplayName ("Testing checkWord")
    void checkWord() {
        Dictionary d = new Dictionary();
        d.acceptedWords.add("about");
        String testWord = "about";
        org.junit.jupiter.api.Assertions.assertTrue(d.checkWord(testWord));

        //word isn't in the acceptedwords
        org.junit.jupiter.api.Assertions.assertFalse(d.checkWord("pizza"));
    }
}