package controller;

import model.GridNumber;
import util.ColorMap;
import util.FileUtil;
import util.GlobalConstNumbers;
import view.GameFrame;
import view.GamePanel;
import view.LoginFrame;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is used for interactive with JButton in GameFrame.
 */
public class GameController {
    private GamePanel gamePanel;
    private GameFrame gameFrame;
    private LoginFrame loginFrame;
    private GridNumber model;
    private final String workingDirectory;
    private final String userInfoFileName = "userInfo.txt";
    private Map userInfoMap;
    private String currentUserName;

    public GameController() {
        this.workingDirectory = System.getProperty("user.dir");

        this.userInfoMap = new HashMap<String, String>();
        loadUserInfo();

        ColorMap.InitialColorMap();

        this.gamePanel = new GamePanel(
                GlobalConstNumbers.gamePanelSize
        );

        this.model = this.gamePanel.getModel();

        this.gameFrame = new GameFrame(
                this,
                GlobalConstNumbers.gameFrameWidth,
                GlobalConstNumbers.gameFrameHeight,
                this.gamePanel
        );

        this.loginFrame = new LoginFrame(
                this,
                GlobalConstNumbers.loginFrameWidth,
                GlobalConstNumbers.loginFrameHeight
        );
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("background_sound.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void mainGame() {
        this.playSound();
        this.loginFrame.setVisible(true);
    }

    public void restartGame() {
        System.out.println("Do restart game here");
        model.initialNumbers();//重新定义二维数组里的数据的值
        gamePanel.clearSteps();//在view里,清空步数
        gamePanel.restartGame();
    }

    public void loadUserInfo() {
        String path = this.userInfoFileName;
        File f = new File(path);
        if (!f.exists()) {
            return;
        }
        List<String> lines = FileUtil.readFileToList(path);
        for (String line : lines) {
            String[] strArray = line.split(" ");
            if (strArray.length == 2) {
                this.userInfoMap.put(strArray[0], strArray[1]);
            }
        }
    }

    public void saveUserInfo() {
        String path = this.userInfoFileName;
        List<String> lines = new ArrayList<>();
        for (Object userName : this.userInfoMap.keySet()) {
            String userPassword = (String) this.userInfoMap.get(userName);
            lines.add(userName + " " + userPassword);
        }
        FileUtil.writeFiLeFromList(path, lines);
    }

    public void loadGame(String path) {
        if (!path.equals("guest")) {
            System.out.println("Path is not 'guest'");
            File f = new File(path);
            if (f.exists()) {
                System.out.println("Game data file exists");
                List<String> lines = FileUtil.readFileToList(path);
                if (lines != null) {
                    System.out.println("Game data is not empty, so we load it");
                    model.loadGame(lines.subList(0, 3 * GlobalConstNumbers.gridNumberCount));
                    gamePanel.setWinCondition(Integer.parseInt(lines.get(lines.size() - 3)));
                    gamePanel.setSteps(Integer.parseInt(lines.get(lines.size() - 2)));
                    gamePanel.setTime(Integer.parseInt(lines.get(lines.size() - 1)));
                    gamePanel.updateGridsNumber();
                    return;
                }
            }
        }
        model.initialNumbers();
        gamePanel.clearSteps();
        gamePanel.restartGame();
        System.out.println("Initialize game here");
    }

    public void saveGame(String path) {
        List<String> lines = model.convertGameToList();
        lines.add(Integer.toString(gamePanel.getWinCondition()));
        lines.add(Integer.toString(gamePanel.getSteps()));
        lines.add(Integer.toString(gamePanel.getTime()));
        FileUtil.writeFiLeFromList(path, lines);
        System.out.println("Do save game here, file path is " + path);
    }

    public boolean matchUserWithPassword(String userName, String userPassword) {
        return this.userInfoMap.get(userName).equals(userPassword);
    }

    public void loadUserGame(String userName) {
        this.loadGame(userName + ".txt");
        this.currentUserName = userName;
        this.loginFrame.setVisible(false);
        this.gameFrame.setVisible(true);
    }

    public void saveCurrentUserGame() {
        if (this.currentUserName.isEmpty()) {
            JOptionPane.showMessageDialog(this.gameFrame, "Cannot save in guest mode");
        } else {
            this.saveGame(this.currentUserName + ".txt");
        }
    }

    public void exitCurrentUserGame() {
        if (!this.currentUserName.isEmpty()) {
            int result = JOptionPane.showConfirmDialog(this.gameFrame, "Do you want to save data before exiting?", "Exit", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                this.saveCurrentUserGame();
            }
        }
        this.currentUserName = "";
        this.gameFrame.setVisible(false);
        this.loginFrame.setVisible(true);
    }

    public void loadGuestGame() {
        this.loadGame("guest");
        this.currentUserName = "";
        this.loginFrame.setVisible(false);
        this.gameFrame.setVisible(true);
    }

    public boolean userExists(String userName) {
        return this.userInfoMap.containsKey(userName);
    }

    public void signupUser(String userName, String userPassword) {
        this.userInfoMap.put(userName, userPassword);
        this.saveUserInfo();
    }

    public void deleteUser(String userName) {
        this.userInfoMap.remove(userName);
        File f = new File(userName + ".txt");
        f.delete();
        this.saveUserInfo();
    }
}
