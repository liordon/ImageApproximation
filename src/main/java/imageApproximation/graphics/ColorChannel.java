package imageApproximation.graphics;

import java.awt.*;

public enum ColorChannel {
    RED,
    GREEN,
    BLUE;

    public static int getColorDifferenceOfChannel(Color originalColor, Color targetColor, ColorChannel channel) {
        switch (channel) {
            case RED:
                return originalColor.getRed() - targetColor.getRed();
            case GREEN:
                return originalColor.getGreen() - targetColor.getGreen();
            case BLUE:
                return originalColor.getBlue() - targetColor.getBlue();
        }
        return 0;
    }
}
