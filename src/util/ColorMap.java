package util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorMap {
    static Map<Integer, Color> colorMap = new HashMap<>();

    //todo: complete the method to add other color
    public static void InitialColorMap() {
        colorMap.put(2, Color.ORANGE);
        colorMap.put(4, Color.RED);
        colorMap.put(8, Color.GREEN);
        colorMap.put(16, Color.CYAN);
        colorMap.put(32, Color.BLUE);
        colorMap.put(64, Color.MAGENTA);
        colorMap.put(128, Color.PINK);
        colorMap.put(256, Color.LIGHT_GRAY);
        colorMap.put(512, Color.DARK_GRAY);
        colorMap.put(1024, Color.darkGray);
        colorMap.put(2048, Color.WHITE);

    }

    public static Color getColor(int i) {
        return colorMap.getOrDefault(i, Color.black);
    }
}
