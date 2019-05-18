package imageApproximation.steppers.shapes;

import static imageApproximation.steppers.shapes.ShapesForTests.BLACK_PIXEL;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;


class ShapeDrawerTest {

    @Test
    void canDrawSinglePixelCircleOnSinglePixelCanvas() {
        BufferedImage result = ShapeDrawer.draw(new BasicCircle(0,1, Color.GREEN, 1), BLACK_PIXEL, 0,0);
        assertEquals(Color.GREEN.getRGB(), result.getRGB(0,0));
    }

    @Test
    void canDrawImageThatOverflowsFromCanvas(){
        BufferedImage result = ShapeDrawer.draw(new BasicCircle(0,5, Color.RED, 1), BLACK_PIXEL, 0,0);
        assertEquals(Color.RED.getRGB(), result.getRGB(0,0));
    }
}