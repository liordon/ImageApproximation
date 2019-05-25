package imageApproximation.graphics.shapes;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.ImageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static imageApproximation.DifficultTestSubjects.LARGE_CIRCLE_IMAGE;
import static imageApproximation.DifficultTestSubjects.LARGE_SHAPES_LIST;
import static imageApproximation.graphics.shapes.GraphicsForTests.BLACK_PIXEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;


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
        ImageWrapper result = ShapeDrawer.drawOneShape(makeOpaqueGreenCircle(0, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN, result.getColorAtPixel(0, 0));
    }


    @Test
    void canDrawImageThatOverflowsFromCanvas() {
        ImageWrapper result = ShapeDrawer.drawOneShape(makeOpaqueGreenCircle(5, 0, 0), BLACK_PIXEL);
        assertEquals(Color.GREEN, result.getColorAtPixel(0, 0));
    }

    @Test
    void canDrawSinglePixelOnLargeCanvas() {
        ImageWrapper result = ShapeDrawer.drawOneShape(makeOpaqueGreenCircle(0, MID_CANVAS_INDEX, MID_CANVAS_INDEX), emptyCanvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColorAtPixel(i, j), Color.GREEN);
                } else {
                    assertEquals(result.getColorAtPixel(i, j), emptyCanvas.getColorAtPixel(i, j));
                }

            }
        }
    }

    @Test
    void canDrawSmallCircleOnLargeCanvas() {
        ImageWrapper canvas = new ImageWrapper(DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE);
        ImageWrapper result = ShapeDrawer.drawOneShape(makeOpaqueGreenCircle(0, MID_CANVAS_INDEX, MID_CANVAS_INDEX), canvas);
        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (i == MID_CANVAS_INDEX && j == MID_CANVAS_INDEX) {
                    assertEquals(result.getColorAtPixel(i, j), Color.GREEN);
                } else {
                    assertEquals(result.getColorAtPixel(i, j), canvas.getColorAtPixel(i, j));
                }

            }
        }
    }

    @Test
    void drawingHalfOpaquePixelChangesEachChannelByHalfDifference(){
        ImageWrapper result = ShapeDrawer.drawOneShape(new BasicCircle(0, Color.WHITE, 0.5, 0,0), BLACK_PIXEL);
        assertEquals(Math.round(255. / 2), result.getColorAtPixel(0, 0).getRed());
        assertEquals(Math.round(255. / 2), result.getColorAtPixel(0, 0).getGreen());
        assertEquals(Math.round(255. / 2), result.getColorAtPixel(0, 0).getBlue());
    }

    @Test
    void drawingACircleAffectsOnlyItsAffectedPixels(){
        BasicCircle redCircleAt00 = new BasicCircle(0, Color.RED, 1, 0, 0);
        ImageWrapper result = ShapeDrawer.drawOneShape(redCircleAt00, emptyCanvas);
        BasicCircle greenCircleAt22 = new BasicCircle(1, Color.GREEN, 1, 2, 2);
        result = ShapeDrawer.drawOneShape(greenCircleAt22, result);
        BasicCircle blueCircleAt66 = new BasicCircle(2, Color.BLUE, 1, 6, 6);
        result = ShapeDrawer.drawOneShape(blueCircleAt66, result);

        for (int i = 0; i < DECENT_CANVAS_SIZE; i++) {
            for (int j = 0; j < DECENT_CANVAS_SIZE; j++) {
                if (redCircleAt00.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.RED, result.getColorAtPixel(i, j));
                } else if (greenCircleAt22.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.GREEN, result.getColorAtPixel(i, j));
                } else if (blueCircleAt66.getAffectedPixels().contains(new Point(i,j))){
                    assertEquals(Color.BLUE, result.getColorAtPixel(i, j));
                } else {
                    assertEquals(emptyCanvas.getColorAtPixel(i, j), result.getColorAtPixel(i, j));
                }
            }
        }
    }

    @Test
    void canDrawManyShapesAsBatchOperation(){
        ImageWrapper serialResult = emptyCanvas;
        List<BasicShape> allAppliedShapes = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            BasicCircle circle = new BasicCircle(0, Color.WHITE, 1, i, i);
            serialResult = ShapeDrawer.drawOneShape(circle, serialResult);
            allAppliedShapes.add(circle);
        }

        assertEquals(serialResult, ShapeDrawer.drawManyShapes(allAppliedShapes, DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE));
    }

    @Test
    void canDrawASparseVersionOfLargeImage() {
        final int sparsity = 20;
        ImageWrapper sparseImage = ShapeDrawer
                .drawManyShapesSparsely(LARGE_SHAPES_LIST, ExerciseConstants.MAX_IMAGE_SIZE, ExerciseConstants.MAX_IMAGE_SIZE,
                        sparsity);
        for (int i = 0; i < ExerciseConstants.MAX_IMAGE_SIZE / sparsity; i++) {
            for (int j = 0; j < ExerciseConstants.MAX_IMAGE_SIZE / sparsity; j++) {
                assertEquals(LARGE_CIRCLE_IMAGE.getColorAtPixel(i * sparsity, j * sparsity),
                        sparseImage.getColorAtPixel(i, j));
            }
        }
    }

    @Test
    void drawing100ShapesShouldBeLessThan100millisLong() {
        assertTimeout(Duration.ofMillis(100),
                () -> ShapeDrawer.drawManyShapes(LARGE_SHAPES_LIST, DECENT_CANVAS_SIZE, DECENT_CANVAS_SIZE));
    }
}