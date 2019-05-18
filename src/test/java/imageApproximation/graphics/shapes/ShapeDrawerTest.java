package imageApproximation.graphics.shapes;

import static imageApproximation.graphics.shapes.GraphicsForTests.BLACK_PIXEL;
import static org.junit.jupiter.api.Assertions.*;

import imageApproximation.graphics.ImageWrapper;
import org.junit.jupiter.api.Test;

import java.awt.*;


class ShapeDrawerTest {
    private static final int DECENT_CANVAS_SIZE = 10;
    private static final int MID_CANVAS_INDEX = 4;

    private static BasicCircle makeOpaqueCircle(int scale, Color color, int x, int y) {
        return new BasicCircle(scale, color, 1,x,y);
    }

    @Test
    void canDrawSinglePixelCircleOnSinglePixelCanvas() {
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueCircle(0, Color.GREEN, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN, result.getColor(0, 0));
    }


    @Test
    void canDrawImageThatOverflowsFromCanvas() {
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueCircle(5, Color.RED, 0, 0), BLACK_PIXEL);
        assertEquals(Color.RED, result.getColor(0, 0));
    }

    @Test
    void canDrawSinglePixelOnLargeCanvas() {
        ImageWrapper canvas = new ImageWrapper(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE);
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueCircle(0, Color.BLUE, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColor(i, j), Color.BLUE);
                } else {
                    assertEquals(result.getColor(i, j), canvas.getColor(i,j));
                }

            }
        }
    }

    @Test
    void canDrawSmallCircleOnLargeCanvas() {
        ImageWrapper canvas = new ImageWrapper(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE);
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueCircle(1, Color.BLUE, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColor(i, j), Color.BLUE);
                } else {
                    assertEquals(result.getColor(i, j), canvas.getColor(i,j));
                }

            }
        }
    }
}