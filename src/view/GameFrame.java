package view;

import controller.GameController;
import util.GlobalConstNumbers;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private GameController controller;
    private JButton restartBtn, exitBtn, saveBtn, autoBtn, propBtn, obstacleBtn;
    private JButton upButton, downButton, leftButton, rightButton;

    private JLabel stepLabel;
    private JLabel timeLabel;
    private GamePanel gamePanel;
    private MouseListenerInGameFrame mouseListener;
    private int usingProp = GlobalConstNumbers.propEmpty;

    public GameFrame(GameController controller, int width, int height, GamePanel view) {
        this.controller = controller;

        this.setTitle("2024 CS109 Project: 2048 Game");
        this.setSize(width, height);
        this.setLayout(null);

        this.gamePanel = view;
        gamePanel.setLocation(GlobalConstNumbers.gamePanelLocationX, GlobalConstNumbers.gamePanelLocationY);
        this.add(gamePanel);

        this.mouseListener = new MouseListenerInGameFrame(this);

        this.exitBtn = this.createButton("Exit", new Point(500, 100), 110, 50);
        this.restartBtn = this.createButton("Restart", new Point(500, 160), 110, 50);
        this.saveBtn = this.createButton("Save", new Point(500, 220), 110, 50);
        this.autoBtn = this.createButton("Auto Play", new Point(500, 280), 110, 50);
        this.propBtn = this.createButton("Eraser", new Point(475, 340), 80, 50);
        this.obstacleBtn = this.createButton("Barrier", new Point(555, 340), 80, 50);

        this.stepLabel = createLabel("Start", new Font("serif", Font.ITALIC, 22), new Point(480, 50), 180, 50);
        this.timeLabel = createLabel("Time: 0", new Font("serif", Font.ITALIC, 22), new Point(480, 10), 180, 50);

        // Initialize the buttons
        this.upButton = this.createButton("Up", new Point(485, 410), 70, 50);
        this.downButton = this.createButton("Down", new Point(555, 410), 70, 50);
        this.leftButton = this.createButton("Left", new Point(485, 460), 70, 50);
        this.rightButton = this.createButton("Right", new Point(555, 460), 70, 50);

        gamePanel.setStepLabel(stepLabel);
        gamePanel.setTimeLabel(timeLabel);

        this.restartBtn.addActionListener(e -> {
            this.controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        this.saveBtn.addActionListener(e -> {
            this.controller.saveCurrentUserGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        this.exitBtn.addActionListener(e -> {
            this.usingProp = GlobalConstNumbers.propEmpty;
            this.setCursor(Cursor.getDefaultCursor());
            this.controller.exitCurrentUserGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        this.autoBtn.addActionListener(e -> {
            gamePanel.doRandomMove();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        this.propBtn.addActionListener(e -> {
            this.usingProp = GlobalConstNumbers.propEraser;
            System.out.println("Using eraser!");
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            gamePanel.requestFocusInWindow();
        });

        this.obstacleBtn.addActionListener(e -> {
            this.usingProp = GlobalConstNumbers.propObstacle;
            System.out.println("Using barrier");
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            gamePanel.requestFocusInWindow();
        });

        // Direction buttons
        this.upButton.addActionListener(e -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.downButton.addActionListener(e -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.leftButton.addActionListener(e -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.rightButton.addActionListener(e -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        int backgroundWidth = 5000 * height / 3332;
        this.addImage("background_image.jpg", backgroundWidth, height, width / 2 - backgroundWidth / 2, 0);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


    private JButton createButton(String name, Point location, int width, int height) {
        JButton button = new JButton(name);
        button.setLocation(location);
        button.setSize(width, height);
        this.add(button);
        return button;
    }

    private JLabel createLabel(String name, Font font, Point location, int width, int height) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setLocation(location);
        label.setSize(width, height);
        label.setForeground(Color.WHITE);
        this.add(label);
        return label;
    }

    private void addImage(String filename, int picWidth, int picHeight, int locationX, int locationY) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(filename);
        Image scaledImage = image.getScaledInstance(picWidth, picHeight, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
        picLabel.setSize(picWidth, picHeight);
        picLabel.setLocation(locationX, locationY);
        add(picLabel);
    }


    public void mouseClickedAtPoint(float x, float y) {
        if (this.usingProp == GlobalConstNumbers.propEraser) {
            x -= GlobalConstNumbers.gamePanelLocationX;
            y -= GlobalConstNumbers.gamePanelLocationY;

            x /= this.gamePanel.GRID_SIZE;
            y /= this.gamePanel.GRID_SIZE;

            int i = (int) (y), j = (int) (x);

            System.out.println("Used the eraser at (" + i + ", " + j + ")");
            this.gamePanel.eraseNumber(i, j);

            this.usingProp = GlobalConstNumbers.propEmpty;
            this.gamePanel.updateGridsNumber();
            this.setCursor(Cursor.getDefaultCursor());
        } else if (this.usingProp == GlobalConstNumbers.propObstacle) {
            x -= GlobalConstNumbers.gamePanelLocationX;
            y -= GlobalConstNumbers.gamePanelLocationY;

            x /= this.gamePanel.GRID_SIZE;
            y /= this.gamePanel.GRID_SIZE;

            int i = (int) (y), j = (int) (x);
            y -= i;
            x -= j;

            if (x + y < 1 && x - y > 0) { // Top
                this.gamePanel.setVerticalObstacleAt(i - 1, j);
            } else if (x + y < 1 && x - y <= 0) { // Left
                this.gamePanel.setHorizontalObstacleAt(i, j - 1);
            } else if (x + y >= 1 && x - y > 0) { // Right
                this.gamePanel.setHorizontalObstacleAt(i, j);
            } else if (x + y >= 1 && x - y <= 0) { // Bottom
                this.gamePanel.setVerticalObstacleAt(i, j);
            }

            System.out.println("Used barrier at (" + i + ", " + j + ")");
            this.usingProp = GlobalConstNumbers.propEmpty;
            this.gamePanel.updateGridsNumber();
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

}
