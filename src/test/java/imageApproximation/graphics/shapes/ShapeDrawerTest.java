package imageApproximation.graphics.shapes;

import static imageApproximation.graphics.shapes.GraphicsForTests.BLACK_PIXEL;
import static org.junit.jupiter.api.Assertions.*;

import imageApproximation.graphics.ImageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;


class ShapeDrawerTest {
    private static final int DECENT_CANVAS_SIZE = 10;
    private static final int MID_CANVAS_INDEX = 4;
    private ImageWrapper emptyCanvas;

    private static BasicCircle makeOpaqueGreenCircle(int scale, int x, int y) {
        return new BasicCircle(scale, Color.GREEN, 1,x,y);
    }

    @BeforeEach
    void setUp() {
        emptyCanvas = new ImageWrapper(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE);
    }

    @Test
    void canDrawSinglePixelCircleOnSinglePixelCanvas() {
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueGreenCircle(0, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN, result.getColor(0, 0));
    }


    @Test
    void canDrawImageThatOverflowsFromCanvas() {
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueGreenCircle(5, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN, result.getColor(0, 0));
    }

    @Test
    void canDrawSinglePixelOnLargeCanvas() {
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueGreenCircle(0, MID_CANVAS_INDEX, MID_CANVAS_INDEX), emptyCanvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColor(i, j), Color.GREEN);
                } else {
                    assertEquals(result.getColor(i, j), emptyCanvas.getColor(i,j));
                }

            }
        }
    }

    @Test
    void canDrawSmallCircleOnLargeCanvas() {
        ImageWrapper canvas = new ImageWrapper(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE);
        ImageWrapper result = ShapeDrawer.draw(makeOpaqueGreenCircle(0, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColor(i, j), Color.GREEN);
                } else {
                    assertEquals(result.getColor(i, j), canvas.getColor(i,j));
                }

            }
        }
    }

    @Test
    void drawingHalfOpaquePixelChangesEachChannelByHalfDifference(){
        ImageWrapper result = ShapeDrawer.draw(new BasicCircle(0, Color.WHITE, 0.5, 0,0), BLACK_PIXEL);
        assertEquals(Math.round(255./2), result.getColor(0,0).getRed());
        assertEquals(Math.round(255./2), result.getColor(0,0).getGreen());
        assertEquals(Math.round(255./2), result.getColor(0,0).getBlue());
    }

    @Test
    void drawingACircleAffectsOnlyItsAffectedPixels(){
        BasicCircle redCircleAt00 = new BasicCircle(0, Color.RED, 1, 0, 0);
        ImageWrapper result = ShapeDrawer.draw(redCircleAt00, emptyCanvas);
        BasicCircle greenCircleAt22 = new BasicCircle(1, Color.GREEN, 1, 2, 2);
        result = ShapeDrawer.draw(greenCircleAt22, result);
        BasicCircle blueCircleAt66 = new BasicCircle(2, Color.BLUE, 1, 6, 6);
        result = ShapeDrawer.draw(blueCircleAt66, result);

        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (redCircleAt00.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.RED, result.getColor(i,j));
                } else if (greenCircleAt22.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.GREEN, result.getColor(i,j));
                } else if (blueCircleAt66.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.BLUE, result.getColor(i,j));
                } else {
                    assertEquals(emptyCanvas.getColor(i,j), result.getColor(i,j));
                }
            }
        }
    }
}