package imageApproximation.errorCalculators;

import imageApproximation.graphics.ImageWrapper;

import java.awt.*;
import java.util.function.ToDoubleBiFunction;

public class MeanSquareErrorCalculator implements ToDoubleBiFunction<ImageWrapper, ImageWrapper> {
    private enum ColorChannel {
        RED,
        GREEN,
        BLUE
    }

    public double applyAsDouble(ImageWrapper original, ImageWrapper target) {
        if (original.getWidth() != target.getWidth()
                || original.getHeight() != target.getHeight()) {
            throw new IllegalArgumentException(String.format("different image dimensions: (%d,%d) vs (%d,%d)",
                    original.getWidth(), original.getHeight(),
                    target.getWidth(), target.getHeight()));
        }
        double sum = 0;
        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                for (ColorChannel channel : ColorChannel.values()) {
                    int channelDiff = getColorOfChannel(original.getColor(i, j), target.getColor(i, j), channel);
                    sum += Math.pow(channelDiff, 2.);
                }
            }
        }
        return sum / 3. / (original.getWidth() * original.getHeight());
    }

    private int getColorOfChannel(Color originalColor, Color targetColor, ColorChannel channel) {
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
