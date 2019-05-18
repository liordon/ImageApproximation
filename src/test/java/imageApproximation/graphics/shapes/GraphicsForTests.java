package imageApproximation.graphics.shapes;

import imageApproximation.graphics.ImageWrapper;

import java.awt.*;

public class GraphicsForTests {
    public static ImageWrapper WHITE_PIXEL;
    public static ImageWrapper BLACK_PIXEL;

    static {
        GraphicsForTests.WHITE_PIXEL = new ImageWrapper(1, 1);
        GraphicsForTests.WHITE_PIXEL.setRGB(0, 0, Color.WHITE);
        GraphicsForTests.BLACK_PIXEL = new ImageWrapper(1, 1);
        GraphicsForTests.BLACK_PIXEL.setRGB(0, 0, Color.BLACK);
    }
}
