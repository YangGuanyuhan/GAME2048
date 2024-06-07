package view;

import controller.GameController;
import model.GridNumber;
import util.GlobalConstNumbers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GamePanel extends ListenerPanel {
    private GridComponent[][] grids;

    private GridNumber model;
    private JLabel stepLabel;
    private int steps;
    public final int GRID_SIZE;
    private Timer timer;
    private int time;
    private JLabel timeLabel;

    public GamePanel(int size) {
        int COUNT = GlobalConstNumbers.gridNumberCount;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        this.setSize(size, size);
        this.GRID_SIZE = size / COUNT;
        this.grids = new GridComponent[COUNT][COUNT];

        this.model = new GridNumber(
                GlobalConstNumbers.gridNumberCount,
                GlobalConstNumbers.gridNumberCount);

        time = 0;
        timer = new Timer(1000, e -> {
            time++;
            timeLabel.setText("Time: " + time);
        });

        initialGame();

    }

    public GridNumber getModel() {
        return model;
    }

    public void initialGame() {
        this.steps = 0;
        timer.start();
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = new GridComponent(i, j, model.getNumber(i, j), this.GRID_SIZE);
                grids[i][j].setLocation(j * GRID_SIZE, i * GRID_SIZE);
                this.add(grids[i][j]);
            }
        }
//        model.printNumber();//check the 4*4 numbers in game
        this.updateGridsNumber();
    }

    public void updateGridsNumber() {
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j].setNumber(model.getNumber(i, j));
                grids[i][j].setObstacle(model.getHorizontalObstacle(i, j), model.getVerticalObstacle(i, j));
            }
        }
        this.repaint();
        this.requestFocusInWindow();
    }


    /**
     * Implement the abstract method declared in ListenerPanel.
     * Do move right.
     */
    @Override
    public void doMoveRight() {
        this.model.moveRight();
        this.updateGridsNumber();
        this.afterMove();
    }

    public void doMoveLeft() {
        this.model.moveLeft();
        this.updateGridsNumber();
        this.afterMove();
    }

    public void doMoveUp() {
        this.model.moveUp();
        this.afterMove();
        this.updateGridsNumber();
    }

    public void doMoveDown() {
        this.model.moveDown();
        this.afterMove();
        this.updateGridsNumber();
    }

    public void doRandomMove() {
        int direction = GlobalConstNumbers.random.nextInt(4);
        switch (direction) {
            case 0:
                doMoveUp();
                break;
            case 1:
                doMoveDown();
                break;
            case 2:
                doMoveLeft();
                break;
            case 3:
                doMoveRight();
                break;
        }
    }

    public void clearSteps() {
        this.steps = 0;
        this.stepLabel.setText(String.format("Step: %d", this.steps));
    }

    public void afterMove() {

        if (model.isWin()) {
            if (model.winCondition == 2048) {
                JOptionPane.showMessageDialog(this, "Congratulations, you reached " + model.winCondition + ", and you WIN the game!!!");
            } else {
                JOptionPane.showMessageDialog(this, "You reached " + model.winCondition + "!");
            }
            model.increaseWinCondition();
        } else if (!model.isMovable()) {
            JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
        this.steps++;
        this.stepLabel.setText(String.format("Step: %d", this.steps));

        if (!model.isMovable()) {
            timer.stop();
        }
    }


    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }
    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }
    public void restartGame() {
        this.model.initialNumbers();
        this.steps = 0; // Reset the steps
        time = 0;
        timeLabel.setText("Time: 0");
        timer.restart(); // Restart the timer
        this.updateGridsNumber(); // Update the grids
    }

    public void eraseNumber(int i, int j) {
        this.model.setNumberAt(i, j, 0);
    }

    public int getSteps() {
        return steps;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setVerticalObstacleAt(int i, int j) {
        this.model.setVerticalObstacleAt(i, j);
    }

    public void setHorizontalObstacleAt(int i, int j) {
        this.model.setHorizontalObstacleAt(i, j);
    }

    public int getWinCondition() {
        return this.model.winCondition;
    }

    public void setWinCondition(int winCondition) {
        this.model.winCondition = winCondition;
    }

    // The following are AIs
    /**
    public void doSmartMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // Try all possible moves
        for (int direction = 0; direction < 4; direction++) {
            // Clone the current game state
            GridNumber clonedModel = (GridNumber) this.model.clone();

            // Try to move in the current direction
            boolean canMove = clonedModel.isMovable();

            // If the move is valid, use the MiniMax algorithm to find the best move
            if (canMove) {
                int score = minimax(clonedModel, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = direction;
                }
            }
        }

        // Make the best move
        switch (bestMove) {
            case 0:
                doMoveUp();
                break;
            case 1:
                doMoveDown();
                break;
            case 2:
                doMoveLeft();
                break;
            case 3:
                doMoveRight();
                break;
        }
    }

    private int minimax(GridNumber model, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || model.isGameOver()) {
            return heuristicScore(model);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int direction = 0; direction < 4; direction++) {
                GridNumber clonedModel = (GridNumber) model.clone();
                if (clonedModel.move(direction)) {
                    int eval = minimax(clonedModel, depth - 1, alpha, beta, false);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int direction = 0; direction < 4; direction++) {
                GridNumber clonedModel = (GridNumber) model.clone();
                if (clonedModel.move(direction)) {
                    int eval = minimax(clonedModel, depth - 1, alpha, beta, true);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    private int heuristicScore(GridNumber model) {
        // Implement your own heuristic function here
        // For example, you can return the current score of the game
        return model.getScore();
    }
     
     */

}