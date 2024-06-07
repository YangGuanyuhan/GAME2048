package view;

import controller.GameController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private GameController controller;
    private JButton loginBtn, signupBtn, deleteUserBtn, guestBtn;
    public LoginFrame(GameController controller, int width, int height) {
        this.controller = controller;

        this.setTitle("2024 CS109 Project: 2048 Game");
        this.setSize(width, height);
        this.setLayout(null);

        int picWidth = 400, picHeight = 483 * picWidth / 1023;

        this.addImage("title.png", picWidth, picHeight, width / 2 - picWidth / 2, height * 2 / 8 - picHeight / 2);

        int buttonWidth = 140;
        int buttonHeight = 60;

        this.loginBtn = this.createButton(
                "Login",
                new Point(width * 3 / 8 - buttonWidth / 2, height * 4 / 8 - buttonHeight / 2),
                buttonWidth, buttonHeight
        );

        this.signupBtn = this.createButton(
                "Sign Up",
                new Point(width * 5 / 8 - buttonWidth / 2, height * 4 / 8 - buttonHeight / 2),
                buttonWidth, buttonHeight
        );

        this.guestBtn = this.createButton(
                "Guest Play",
                new Point(width * 3 / 8 - buttonWidth / 2, height * 5 / 8 - buttonHeight / 2),
                buttonWidth, buttonHeight
        );

        this.deleteUserBtn = this.createButton(
                "Delete User",
                new Point(width * 5 / 8 - buttonWidth / 2, height * 5 / 8 - buttonHeight / 2),
                buttonWidth, buttonHeight
        );

        this.loginBtn.addActionListener(e -> {
            String userName = JOptionPane.showInputDialog(this, "Input user name:");
            if (!this.controller.userExists(userName)) {
                JOptionPane.showMessageDialog(this, "The user does not exist.");
            } else {
                String userPassword = JOptionPane.showInputDialog(this, "Input user password:");
                if (this.controller.matchUserWithPassword(userName, userPassword)) {
                    this.controller.loadUserGame(userName);
                } else {
                    JOptionPane.showMessageDialog(this, "User password error!", "Login failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        this.guestBtn.addActionListener(e -> {
            this.controller.loadGuestGame();
        });

        this.signupBtn.addActionListener(e -> {
            String userName = JOptionPane.showInputDialog(this, "Input user name:");

            if (this.controller.userExists(userName)) {
                JOptionPane.showMessageDialog(this, "The user exists currently.");
            } else {
                String userPassword = JOptionPane.showInputDialog(this, "Input user password:");
                String userPassword2 = JOptionPane.showInputDialog(this, "Check user password again:");

                if (userPassword.equals(userPassword2)) {
                    this.controller.signupUser(userName, userPassword);
                } else {
                    JOptionPane.showMessageDialog(this, "The 2 passwords do not match.");
                }
            }
        });

        this.deleteUserBtn.addActionListener(e -> {
            String userName = JOptionPane.showInputDialog(this, "Input user name:");
            if (!this.controller.userExists(userName)) {
                JOptionPane.showMessageDialog(this, "The user does not exist.");
            } else {
                String userPassword = JOptionPane.showInputDialog(this, "Input user password:");
                if (this.controller.matchUserWithPassword(userName, userPassword)) {
                    this.controller.deleteUser(userName);
                } else {
                    JOptionPane.showMessageDialog(this, "User password error!", "Login failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
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
}
