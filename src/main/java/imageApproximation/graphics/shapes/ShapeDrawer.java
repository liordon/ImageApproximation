package imageApproximation.graphics.shapes;

import imageApproximation.graphics.ImageWrapper;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static imageApproximation.graphics.ColorChannel.*;

public class ShapeDrawer {
    public static ImageWrapper drawOne(BasicShape shape, ImageWrapper canvas) {
        ImageWrapper result = ImageWrapper.deepCopy(canvas);

        List<Point> relevantPixels = shape.getAffectedPixels().stream()
                .filter(p -> p.x >= 0 && p.y >= 0)
                .filter(p -> p.x < canvas.getWidth() && p.y < canvas.getHeight())
                .collect(Collectors.toList());
        for (Point point : relevantPixels) {
            drawSinglePixelFromShape(shape, result, point);
        }
        return result;
    }

    private static void drawSinglePixelFromShape(BasicShape shape, ImageWrapper canvas, Point point) {
        drawSinglePixelFromShape(shape, canvas, point.x, point.y);
    }

    private static void drawSinglePixelFromShape(BasicShape shape, ImageWrapper canvas, int i, int j) {
        int redDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(i, j), RED);
        int greenDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(i, j), GREEN);
        int blueDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(i, j), BLUE);
        canvas.setRGB(i, j,
                new Color(
                        (int) Math.round(canvas.getColor(i, j).getRed() + redDifference * shape.getOpacity()),
                        (int) Math.round(canvas.getColor(i, j).getGreen() + greenDifference * shape.getOpacity()),
                        (int) Math.round(canvas.getColor(i, j).getBlue() + blueDifference * shape.getOpacity()))

        );
    }

    public static ImageWrapper drawMany(List<BasicShape> manyShapes, ImageWrapper canvas) {
        ImageWrapper result = ImageWrapper.deepCopy(canvas);

        for (int i = 0; i < result.getWidth(); i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                for (BasicShape shape : manyShapes) {
                    if (shape.contains(i, j)) {
                        drawSinglePixelFromShape(shape, result, i, j);
                    }
                }
            }
        }

        return result;
    }
}

