/**
 * The view interface for the Scrabble game.
 *
 * @author Cole Galway
 * @version 11/10/2025
 */
public interface GameView {
    public void update(GameModel model);
    public void showError(String error);
}
