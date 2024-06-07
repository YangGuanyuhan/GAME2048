import controller.GameController;
import view.GameFrame;
import util.GlobalConstNumbers;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 以下是修改后的代码
            GameController gameController = new GameController();
            gameController.mainGame();
        });
    }
}
