package model;

import util.GlobalConstNumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GridNumber {
    private final int X_COUNT;
    private final int Y_COUNT;
    public int winCondition = GlobalConstNumbers.initialWinCondition;

    private int[][] numbers;
    private int[][] verticalObstacle;
    private int[][] horizontalObstacle;

    static Random random = GlobalConstNumbers.random;

    public GridNumber(int xCount, int yCount) {
        this.X_COUNT = xCount;
        this.Y_COUNT = yCount;
        this.numbers = new int[this.X_COUNT][this.Y_COUNT];
        this.verticalObstacle = new int[this.X_COUNT][this.Y_COUNT];
        this.horizontalObstacle = new int[this.X_COUNT][this.Y_COUNT];
        this.initialNumbers();
    }

    public void initialNumbers() {
        // Clear the board
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                numbers[i][j] = verticalObstacle[i][j] = horizontalObstacle[i][j] = 0;
            }
        }

        // Place a 2 at a random position
        int i = random.nextInt(numbers.length);
        int j = random.nextInt(numbers[0].length);
        numbers[i][j] = 2;

        // Place a 4 at a different random position
        int newI = i;
        int newJ = j;
        while (newI == i && newJ == j) {
            newI = random.nextInt(numbers.length);
            newJ = random.nextInt(numbers[0].length);
        }
        numbers[newI][newJ] = 4;
        this.winCondition = GlobalConstNumbers.initialWinCondition;
    }

    /**
     * 这个函数用来计算每一个格子在移动之后应该放在哪个位置，这个函数是不考虑格子之间的挡板的
     * 这是为了方便（如果有需要的话）制作移动的动画去使用
     * @param line
     */
    public int[] calcFinalPositionsOfEachNumber(int[] line) {
        int n = line.length;

        int lastNumber = -1; // -1 表示不可以合并
        int currentPosition = -1; // 最开始 -1 将会加成 0

        int[] result = new int[n];

        for (int i = 0; i < n; ++i) {
            if (line[i] == 0) {
                result[i] = -1; // -1 表示这一个格子是数字 0，也就是空格子
            } else { // line[i] != 0 表示这是一个非空的格子
                if (lastNumber != line[i]) {
                    // 包括：lastNumber == -1 或者 lastNumber 不是 -1，但是数字不同，所以不能合并
                    // 如果是第一次，那么 currentPosition == -1，所以会加一变成 0，就是正确的位置
                    ++currentPosition;
                    // 问题：如果整行都是 0 怎么办？
                    // 回答：返回所有 result 都是 -1
                    result[i] = currentPosition;
                    lastNumber = line[i];
                } else { // 否则 lastNumber == line[i] 表示可以合并，那么放在同一个位置，而且 lastNumber 清空到 -1 状态
                    result[i] = currentPosition;
                    lastNumber = -1;
                }
            }
        }

        return result;
    }

    /**
     * 返回移动之后的结果列表，注意这里 line 不会被改变
     * @param line
     * @return
     */
    public int[] moveSingleLineNumbers(int[] line) {
        int n = line.length;
        int[] finalPositionsOfEachNumber = this.calcFinalPositionsOfEachNumber(line);
        int[] resultLine = new int[n];

        for (int i = 0; i < n; ++i) {
            int pos = finalPositionsOfEachNumber[i];
            if (pos != -1) { // 这个格子非空
                assert line[i] == 0;
                resultLine[pos] += line[i];
            }
        }

        return resultLine;
    }

    public int[] moveSingleLineNumbers(int[] line, int[] haveObstacleHere) {
        // 如果 haveObstacleHere[i] == 1 说明在 i, i + 1 之间有一个挡板
        int n = line.length;
        int lastPosition = 0;

        int[] resultLine = new int[n];

        for (int i = 0; i < n; ++i) {
            if (i == n - 1 || haveObstacleHere[i] == 1) {
                int[] newPartOfLine = this.moveSingleLineNumbers(Arrays.copyOfRange(line, lastPosition, i + 1));
                int partLength = i - lastPosition + 1;

                assert partLength == newPartOfLine.length;

                System.arraycopy(newPartOfLine, 0, resultLine, lastPosition, partLength);

                lastPosition = i + 1;
            }
        }

        assert lastPosition == n;

        return resultLine;
    }

    public void reverseArray(int[] array) {
        int n = array.length;
        for (int i = 0; i < n / 2; ++i) {
            int t = array[i];
            array[i] = array[n - 1 - i];
            array[n - 1 - i] = t;
        }
    }

    public int[][] tryToMoveLeft() {
        int height = this.X_COUNT;
        int width = this.Y_COUNT;

        int[][] newNumbers = new int[height][width];
        for (int i = 0; i < height; ++i) {
            int[] newLine = this.moveSingleLineNumbers(numbers[i], horizontalObstacle[i]);
            System.arraycopy(newLine, 0, newNumbers[i], 0, width);
        }

        return newNumbers;
    }

    public int[][] tryToMoveRight() {
        int height = this.X_COUNT;
        int width = this.Y_COUNT;

        int[][] newNumbers = new int[height][width];
        for (int i = 0; i < height; ++i) {
            int[] newLine = numbers[i].clone();
            int[] newObstacle = new int[width];

            for (int j = 0; j < width - 1; ++j) {
                newObstacle[j] = this.horizontalObstacle[i][width - j - 2];
            }

            this.reverseArray(newLine);
            newLine = this.moveSingleLineNumbers(newLine, newObstacle);
            this.reverseArray(newLine);

            System.arraycopy(newLine, 0, newNumbers[i], 0, width);
        }

        return newNumbers;
    }

    public int[][] tryToMoveUp() {
        int height = this.X_COUNT;
        int width = this.Y_COUNT;

        int[][] newNumbers = new int[height][width];
        for (int i = 0; i < width; ++i) {
            int[] newLine = new int[height];

            for (int j = 0; j < height; ++j)
                newLine[j] = numbers[j][i];

            newLine = this.moveSingleLineNumbers(newLine, verticalObstacle[i]);

            for (int j = 0; j < height; ++j)
                newNumbers[j][i] = newLine[j];
        }

        return newNumbers;
    }

    public int[][] tryToMoveDown() {
        int height = this.X_COUNT;
        int width = this.Y_COUNT;

        int[][] newNumbers = new int[height][width];
        for (int i = 0; i < width; ++i) {
            int[] newLine = new int[height];
            int[] newObstacle = new int[height];

            for (int j = 0; j < height - 1; ++j) {
                newObstacle[j] = this.verticalObstacle[i][height - j - 2];
            }

            for (int j = 0; j < height; ++j)
                newLine[j] = numbers[j][i];

            this.reverseArray(newLine);
            newLine = this.moveSingleLineNumbers(newLine, newObstacle);
            this.reverseArray(newLine);

            for (int j = 0; j < height; ++j)
                newNumbers[j][i] = newLine[j];
        }

        return newNumbers;
    }

    public boolean isFullOfNumbers() {
        for (int i = 0; i < numbers.length; ++i)
            for (int j = 0; j < numbers[i].length; ++j)
                if (numbers[i][j] == 0)
                    return false;
        return true;
    }

    public void moveLeft() {
        numbers = tryToMoveLeft();
        if (!this.isFullOfNumbers())
            putRandomNumber();
    }

    public void moveRight() {
        numbers = tryToMoveRight();
        if (!this.isFullOfNumbers())
            putRandomNumber();
    }

    public void moveUp() {
        numbers = tryToMoveUp();
        if (!this.isFullOfNumbers())
            putRandomNumber();
    }

    public void moveDown() {
        numbers = tryToMoveDown();
        if (!this.isFullOfNumbers())
            putRandomNumber();
    }

    public int getNumber(int i, int j) {
        return numbers[i][j];
    }

    public void printNumber() {
        for (int[] line : numbers) {
            System.out.println(Arrays.toString(line));
        }
    }

    public void putRandomNumber() {
        int i = random.nextInt(numbers.length);
        int j = random.nextInt(numbers[0].length);
        while (numbers[i][j] != 0) {
            i = random.nextInt(numbers.length);
            j = random.nextInt(numbers[0].length);
        }
        numbers[i][j] = random.nextInt(2) == 0 ? 2 : 4;
    }
    public boolean areEqual2DArrays(int[][] a, int[][] b) {
        if (a.length != b.length)
            return false;

        for (int i = 0; i < a.length; ++i) {
            if (!Arrays.equals(a[i], b[i]))
                return false;
        }

        return true;
    }
    public boolean isMovable() {
        boolean flag = false;
        int[][] newNumber;

        newNumber = tryToMoveLeft();
        if (!areEqual2DArrays(newNumber, numbers))
            flag = true;
        newNumber = tryToMoveRight();
        if (!areEqual2DArrays(newNumber, numbers))
            flag = true;
        newNumber = tryToMoveUp();
        if (!areEqual2DArrays(newNumber, numbers))
            flag = true;
        newNumber = tryToMoveDown();
        if (!areEqual2DArrays(newNumber, numbers))
            flag = true;

        System.out.println("isMovable() returns " + (flag ? "true" : "false"));

        return flag;
    }
    public boolean isWin() {
        System.out.println("winCondition = " + winCondition);
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers[i].length; j++) {
                if (numbers[i][j] == winCondition) {
                    return true; // If there is a cell with the value of winCondition, we win
                }
            }
        }
        return false; // If none of the cells have the value of winCondition, we haven't won yet
    }
    public void increaseWinCondition() {
        winCondition *= 2; // Double the win condition
    }

    public List<String> convertGameToList() {
        List<String> lines = new ArrayList<>();
        for (int[] row : this.numbers) {
            StringBuilder line = new StringBuilder();
            for (int number : row) {
                line.append(number).append(" ");
            }
            lines.add(line.toString());
        }
        for (int[] row : this.horizontalObstacle) {
            StringBuilder line = new StringBuilder();
            for (int number : row) {
                line.append(number).append(" ");
            }
            lines.add(line.toString());
        }
        for (int[] row : this.verticalObstacle) {
            StringBuilder line = new StringBuilder();
            for (int number : row) {
                line.append(number).append(" ");
            }
            lines.add(line.toString());
        }
        return lines;
    }

    public void loadGame(List<String> lines) {
        int n = GlobalConstNumbers.gridNumberCount;
        for (int i = 0; i < n; i++) {
            String[] numbers = lines.get(i).split(" ");
            for (int j = 0; j < this.numbers.length; j++) {
                this.numbers[i][j] = Integer.parseInt(numbers[j]);
            }
        }
        for (int i = 0; i < n; i++) {
            String[] numbers = lines.get(i + n).split(" ");
            for (int j = 0; j < this.horizontalObstacle.length; j++) {
                this.horizontalObstacle[i][j] = Integer.parseInt(numbers[j]);
            }
        }
        for (int i = 0; i < n; i++) {
            String[] numbers = lines.get(i + n * 2).split(" ");
            for (int j = 0; j < this.verticalObstacle.length; j++) {
                this.verticalObstacle[i][j] = Integer.parseInt(numbers[j]);
            }
        }
    }

    public void setNumberAt(int i, int j, int num) {
        int n = GlobalConstNumbers.gridNumberCount;
        if (i < 0 || j < 0 || i >= n || j >= n)
            return;
        this.numbers[i][j] = num;
    }

    public void setVerticalObstacleAt(int i, int j) {
        if (i < 0 || j < 0 || i >= GlobalConstNumbers.gridNumberCount - 1 || j >= GlobalConstNumbers.gridNumberCount)
            return;

        this.verticalObstacle[j][i] = 1 - this.verticalObstacle[j][i];
    }

    public void setHorizontalObstacleAt(int i, int j) {
        if (i < 0 || j < 0 || i >= GlobalConstNumbers.gridNumberCount || j >= GlobalConstNumbers.gridNumberCount - 1)
            return;

        this.horizontalObstacle[i][j] = 1 - this.horizontalObstacle[i][j];
    }

    public int getVerticalObstacle(int i, int j) {
        return this.verticalObstacle[j][i];
    }

    public int getHorizontalObstacle(int i, int j) {
        return this.horizontalObstacle[i][j];
    }

}
