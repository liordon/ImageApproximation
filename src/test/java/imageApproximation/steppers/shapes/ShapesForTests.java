package imageApproximation.steppers.shapes;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class ShapesForTests {
    public static BufferedImage WHITE_PIXEL;
    public static BufferedImage BLACK_PIXEL;

    static {
        ShapesForTests.WHITE_PIXEL = new BufferedImage(1, 1, TYPE_INT_RGB);
        ShapesForTests.WHITE_PIXEL.setRGB(0, 0, Color.WHITE.getRGB());
        ShapesForTests.BLACK_PIXEL = new BufferedImage(1, 1, TYPE_INT_RGB);
        ShapesForTests.BLACK_PIXEL.setRGB(0, 0, Color.BLACK.getRGB());
    }
}
