package imageApproximation.errorCalculators;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.ToDoubleBiFunction;

public class MeanSquareErrorCalculator implements ToDoubleBiFunction<BufferedImage, BufferedImage> {
    private enum ColorChannel {
        RED,
        GREEN,
        BLUE
    }

    public double applyAsDouble(BufferedImage original, BufferedImage target) {
        if (original.getWidth() != target.getWidth()
                || original.getHeight() != target.getHeight()) {
            throw new IllegalArgumentException(String.format("different image dimensions: (%d,%d) vs (%d,%d)",
                    original.getWidth(), original.getHeight(),
                    target.getWidth(), target.getHeight()));
        }
        double sum = 0;
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                int originalRGB = original.getRGB(i, j);
                int targetRGB = target.getRGB(i, j);
                for (ColorChannel channel : ColorChannel.values()) {
                    int channelDiff = getColorOfChannel(originalRGB, targetRGB, channel);
                    sum += Math.pow(channelDiff, 2.);
                }
            }
        }
        return sum / 3. / (original.getWidth() * original.getHeight());
    }

    private int getColorOfChannel(int originalRGB, int targetRGB, ColorChannel channel) {
        Color originalColor = new Color(originalRGB);
        Color targetColor = new Color(targetRGB);
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
