package util;

import java.util.Random;

public class GlobalConstNumbers {
    public static final int gameFrameWidth = 700;
    public static final int gameFrameHeight = 600;
    public static final int loginFrameWidth = 700;
    public static final int loginFrameHeight = 600;
    public static final int gamePanelSize = 400;
    public static final int gamePanelLocationX = gameFrameWidth / 15;
    public static final int gamePanelLocationY = (gameFrameHeight - gamePanelSize) / 3;
    public static final int gridNumberCount = 4;
    public static Random random = new Random();
    public static final int propEmpty = 0;
    public static final int propEraser = 1;
    public static final int propObstacle = 2;
    public static final int obstacleThickness = 8;
    public static final int initialWinCondition = 64;
}
