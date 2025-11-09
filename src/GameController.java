import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String [] coord = e.getActionCommand().split(",");
        int x = Integer.parseInt(coord[0]);
        int y = Integer.parseInt(coord[1]);

    }
}
