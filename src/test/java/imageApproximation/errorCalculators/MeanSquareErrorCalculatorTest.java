package imageApproximation.errorCalculators;

import static org.junit.jupiter.api.Assertions.*;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.GraphicsForTests;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;


class MeanSquareErrorCalculatorTest {

    private static int maxDiffPerPixel = 255 * 255; // color values range from 0 to 255, so max squared difference is 255^2
    private static MeanSquareErrorCalculator inspected;


    private static void assertApplicationSymmetricallyEquals(double expected, ImageWrapper arg1, ImageWrapper arg2) {
        assertEquals(expected, inspected.applyAsDouble(arg1, arg2));
        assertEquals(expected, inspected.applyAsDouble(arg2, arg1));
    }

    @BeforeAll
    static void setUp() {
        inspected = new MeanSquareErrorCalculator(1);
    }

    @Test
    void comparingAnImageToItselfShouldReturnZero() {
        ImageWrapper example = new ImageWrapper(1, 1);
        assertApplicationSymmetricallyEquals(0, example, example);
    }

    @Test
    void comparingAWhitePixelToABlackPixelShouldReturn255Squared() {
        assertApplicationSymmetricallyEquals(maxDiffPerPixel, GraphicsForTests.BLACK_PIXEL, GraphicsForTests.WHITE_PIXEL);
    }

    @Test
    void comparingABlackPixelToAPixelValued1InASingleColorIsAlwaysOneThird() {
        ImageWrapper slightlyRed = new ImageWrapper(1, 1);
        ImageWrapper slightlyGreen = new ImageWrapper(1, 1);
        ImageWrapper slightlyBlue = new ImageWrapper(1, 1);
        slightlyRed.setColor(0, 0, new Color(1, 0, 0));
        slightlyGreen.setColor(0, 0, new Color(0, 1, 0));
        slightlyBlue.setColor(0, 0, new Color(0, 0, 1));

        assertApplicationSymmetricallyEquals(1. / 3, GraphicsForTests.BLACK_PIXEL, slightlyRed);
        assertApplicationSymmetricallyEquals(1. / 3, GraphicsForTests.BLACK_PIXEL, slightlyGreen);
        assertApplicationSymmetricallyEquals(1. / 3, GraphicsForTests.BLACK_PIXEL, slightlyBlue);
    }

    @Test
    void comparing2ImagesWithDifferentSizesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                inspected.applyAsDouble(new ImageWrapper(1, 2),
                        new ImageWrapper(1, 1))
        );
        assertThrows(IllegalArgumentException.class, () ->
                inspected.applyAsDouble(new ImageWrapper(2, 1),
                        new ImageWrapper(1, 1))
        );
    }

    @Test
    void comparingBlackAndWhitePixelsToBlackAndBlackPixelsYieldsHalfOfMaxPossibleDifference() {
        ImageWrapper blackAndWhite = new ImageWrapper(1, 2);
        ImageWrapper blackAndBlack = new ImageWrapper(1, 2);
        blackAndWhite.setColor(0, 0, Color.BLACK);
        blackAndWhite.setColor(0, 1, Color.WHITE);
        blackAndBlack.setColor(0, 0, Color.BLACK);
        blackAndBlack.setColor(0, 1, Color.BLACK);

        assertApplicationSymmetricallyEquals(maxDiffPerPixel / 2., blackAndBlack, blackAndWhite);
    }
}