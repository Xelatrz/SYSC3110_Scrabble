/**
 * The view interface for the Scrabble game.
 *
 * @author Cole Galway
 * @version 11/24/2025
 */
public interface GameView {
    void update(GameModel model);
    void showError(String error);
    char promptBlankLetter();
}
