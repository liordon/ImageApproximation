package imageApproximation.graphics.shapes;

import imageApproximation.graphics.ImageWrapper;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static imageApproximation.graphics.ColorChannel.*;

public class ShapeDrawer {
    public static ImageWrapper drawOneShape(BasicShape shape, ImageWrapper canvas) {
        ImageWrapper result = ImageWrapper.deepCopy(canvas);

        List<Point> relevantPixels = shape.getAffectedPixels().stream()
                .filter(p -> p.x >= 0 && p.y >= 0)
                .filter(p -> p.x < canvas.getWidth() && p.y < canvas.getHeight())
                .collect(Collectors.toList());
        for (Point point : relevantPixels) {
            drawSinglePixelFromShape(shape, result, point.x, point.y);
        }
        return result;
    }

    public static ImageWrapper drawManyShapes(List<BasicShape> manyShapes, int width, int height) {
        ImageWrapper result = new ImageWrapper(width, height);

        calculateSparsePixels(manyShapes, result, 1);

        return result;
    }

    public static ImageWrapper drawManyShapesSparsely(List<BasicShape> basicShapes, int width, int height,
                                                      int sparsity) {
        ImageWrapper result = new ImageWrapper(width / sparsity, height / sparsity);
        calculateSparsePixels(basicShapes, result, sparsity);
        return result;
    }

    private static void calculateSparsePixels(List<BasicShape> manyShapes, ImageWrapper result, int sparsity) {
        for (int i = 0; i < result.getWidth(); i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                for (BasicShape shape : manyShapes) {
                    if (shape.contains(i*sparsity, j*sparsity)) {
                        drawSinglePixelFromShape(shape, result, i, j);
                    }
                }
            }
        }
    }

    private static void drawSinglePixelFromShape(BasicShape shape, ImageWrapper canvas, int i, int j) {
        int redDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColorAtPixel(i, j), RED);
        int greenDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColorAtPixel(i, j), GREEN);
        int blueDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColorAtPixel(i, j), BLUE);
        canvas.setColor(i, j,
                new Color(
                        (int) Math.round(canvas.getColorAtPixel(i, j).getRed() + redDifference * shape.getOpacity()),
                        (int) Math
                                .round(canvas.getColorAtPixel(i, j).getGreen() + greenDifference * shape.getOpacity()),
                        (int) Math.round(canvas.getColorAtPixel(i, j).getBlue() + blueDifference * shape.getOpacity()))

        );
    }
}

