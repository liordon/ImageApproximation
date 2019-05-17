package imageApproximation.errorCalculators;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;


class MeanSquareErrorCalculatorTest {

    private static int maxDiffPerPixel = 255 * 255; // color values range from 0 to 255, so max squared difference is 255^2
    private static MeanSquareErrorCalculator inspected;
    private static BufferedImage WHITE;
    private static BufferedImage BLACK;


    private static void assertApplicationSymmetricallyEquals(double expected, BufferedImage arg1, BufferedImage arg2) {
        assertEquals(expected, inspected.applyAsDouble(arg1, arg2));
        assertEquals(expected, inspected.applyAsDouble(arg2, arg1));
    }

    @BeforeAll
    static void setUp() {
        inspected = new MeanSquareErrorCalculator();
        WHITE = new BufferedImage(1, 1, TYPE_INT_RGB);
        WHITE.setRGB(0, 0, Color.WHITE.getRGB());
        BLACK = new BufferedImage(1, 1, TYPE_INT_RGB);
        BLACK.setRGB(0, 0, Color.BLACK.getRGB());
    }

    @Test
    void comparingAnImageToItselfShouldReturnZero() {
        BufferedImage example = new BufferedImage(1, 1, TYPE_INT_RGB);
        assertApplicationSymmetricallyEquals(0, example, example);
    }

    @Test
    void comparingAWhitePixelToABlackPixelShouldReturn255Squared() {
        assertApplicationSymmetricallyEquals(maxDiffPerPixel, BLACK, WHITE);
    }

    @Test
    void comparingABlackPixelToAPixelValued1InASingleColorIsAlwaysOneThird() {
        BufferedImage slightlyRed = new BufferedImage(1, 1, TYPE_INT_RGB);
        BufferedImage slightlyGreen = new BufferedImage(1, 1, TYPE_INT_RGB);
        BufferedImage slightlyBlue = new BufferedImage(1, 1, TYPE_INT_RGB);
        slightlyRed.setRGB(0, 0, new Color(1, 0, 0).getRGB());
        slightlyGreen.setRGB(0, 0, new Color(0, 1, 0).getRGB());
        slightlyBlue.setRGB(0, 0, new Color(0, 0, 1).getRGB());

        assertApplicationSymmetricallyEquals(1. / 3, BLACK, slightlyRed);
        assertApplicationSymmetricallyEquals(1. / 3, BLACK, slightlyGreen);
        assertApplicationSymmetricallyEquals(1. / 3, BLACK, slightlyBlue);
    }

    @Test
    void comparing2ImagesWithDifferentSizesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                inspected.applyAsDouble(new BufferedImage(1, 2, TYPE_INT_RGB),
                        new BufferedImage(1, 1, TYPE_INT_RGB))
        );
        assertThrows(IllegalArgumentException.class, () ->
                inspected.applyAsDouble(new BufferedImage(2, 1, TYPE_INT_RGB),
                        new BufferedImage(1, 1, TYPE_INT_RGB))
        );
    }

    @Test
    void comparingBlackAndWhitePixelsToBlackAndBlackPixelsYieldsHalfOfMaxPossibleDifference() {
        BufferedImage blackAndWhite = new BufferedImage(1, 2, TYPE_INT_RGB);
        BufferedImage blackAndBlack = new BufferedImage(1, 2, TYPE_INT_RGB);
        blackAndWhite.setRGB(0, 0, Color.BLACK.getRGB());
        blackAndWhite.setRGB(0, 1, Color.WHITE.getRGB());
        blackAndBlack.setRGB(0, 0, Color.BLACK.getRGB());
        blackAndBlack.setRGB(0, 1, Color.BLACK.getRGB());

        assertApplicationSymmetricallyEquals(maxDiffPerPixel / 2., blackAndBlack, blackAndWhite);
    }

}