package view;

import util.ColorMap;
import util.GlobalConstNumbers;

import javax.swing.*;
import java.awt.*;

public class GridComponent extends JComponent {
    private int row;
    private int col;
    private int number;
    private int size;
    static Font font = new Font("Serif", Font.BOLD, 42);
    private int verticalObstacle;
    private int horizontalObstacle;

    public GridComponent(int row, int col, int gridSize) {
        this.size = gridSize;
        this.setSize(gridSize, gridSize);
        this.row = row;
        this.col = col;
        this.number = 0;
        this.verticalObstacle = this.horizontalObstacle = 0;
    }

    public GridComponent(int row, int col, int number, int gridSize) {
        this.size = gridSize;
        this.setSize(gridSize + GlobalConstNumbers.obstacleThickness / 2, gridSize + GlobalConstNumbers.obstacleThickness / 2);
//        this.setSize(gridSize, gridSize);
        this.row = row;
        this.col = col;
        this.number = number;
        this.verticalObstacle = this.horizontalObstacle = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        if (number > 0) {
            g.setColor(Color.white);
            g.fillRect(1, 1, this.size - 2, this.size - 2);
            g.setColor(ColorMap.getColor(number));
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int textWidth = metrics.stringWidth(String.valueOf(number));
            int x = (this.size - textWidth) / 2;
            int y = (this.size - metrics.getHeight()) / 2 + metrics.getAscent();
            g.drawString(String.valueOf(number), x, y);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(1, 1, this.size - 2, this.size - 2);
        }

        if (this.horizontalObstacle == 1) {
            g.setColor(Color.ORANGE);
            g.fillRect(this.size - GlobalConstNumbers.obstacleThickness / 2, 1, GlobalConstNumbers.obstacleThickness, this.size - 2);
        }
        if (this.verticalObstacle == 1) {
            g.setColor(Color.ORANGE);
            g.fillRect(1, this.size - GlobalConstNumbers.obstacleThickness / 2, this.size - 2, GlobalConstNumbers.obstacleThickness);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setObstacle(int h, int v) {
        this.horizontalObstacle = h;
        this.verticalObstacle = v;
    }
}
