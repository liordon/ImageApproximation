package imageApproximation.steppers.shapes;

import static imageApproximation.steppers.shapes.ShapesForTests.BLACK_PIXEL;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;


class ShapeDrawerTest {
    private static final int DECENT_CANVAS_SIZE = 10;
    private static final int MID_CANVAS_INDEX = 4;

    private static BasicCircle makeOpaqueCircle(int scale, Color color, int x, int y) {
        return new BasicCircle(scale, color, 1,x,y);
    }

    @Test
    void canDrawSinglePixelCircleOnSinglePixelCanvas() {
        BufferedImage result = ShapeDrawer.draw(makeOpaqueCircle(0, Color.GREEN, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN.getRGB(), result.getRGB(0, 0));
    }


    @Test
    void canDrawImageThatOverflowsFromCanvas() {
        BufferedImage result = ShapeDrawer.draw(makeOpaqueCircle(5, Color.RED, 0, 0), BLACK_PIXEL);
        assertEquals(Color.RED.getRGB(), result.getRGB(0, 0));
    }

    @Test
    void canDrawSinglePixelOnLargeCanvas() {
        BufferedImage canvas = new BufferedImage(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = ShapeDrawer.draw(makeOpaqueCircle(0, Color.BLUE, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getRGB(i, j), Color.BLUE.getRGB());
                } else {
                    assertEquals(result.getRGB(i, j), canvas.getRGB(i,j));
                }

            }
        }
    }

    @Test
    void canDrawSmallCircleOnLargeCanvas() {
        BufferedImage canvas = new BufferedImage(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE, BufferedImage.TYPE_INT_RGB);
        BufferedImage result = ShapeDrawer.draw(makeOpaqueCircle(1, Color.BLUE, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getRGB(i, j), Color.BLUE.getRGB());
                } else {
                    assertEquals(result.getRGB(i, j), canvas.getRGB(i,j));
                }

            }
        }
    }
}