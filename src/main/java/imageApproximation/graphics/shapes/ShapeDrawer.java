package imageApproximation.graphics.shapes;

import imageApproximation.graphics.ImageWrapper;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static imageApproximation.graphics.ColorChannel.*;

public class ShapeDrawer {
    public static ImageWrapper draw(BasicShape shape, ImageWrapper canvas) {
        ImageWrapper result = ImageWrapper.deepCopy(canvas);

        List<Point> relevantPixels = shape.getAffectedPixels().stream()
                .filter(p -> p.x >= 0 && p.y >= 0)
                .filter(p -> p.x < canvas.getWidth() && p.y < canvas.getHeight())
                .collect(Collectors.toList());
        for (Point point : relevantPixels) {

            int redDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(point.x, point.y), RED);
            int greenDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(point.x, point.y), GREEN);
            int blueDifference = getColorDifferenceOfChannel(shape.getColor(), canvas.getColor(point.x, point.y), BLUE);
            result.setRGB(point.x, point.y,
                    new Color(
                            (int) Math.round(canvas.getColor(point.x, point.y).getRed() + redDifference * shape.getOpacity()),
                            (int) Math.round(canvas.getColor(point.x, point.y).getGreen() + greenDifference * shape.getOpacity()),
                            (int) Math.round(canvas.getColor(point.x, point.y).getBlue() + blueDifference * shape.getOpacity()))

            );
        }
        return result;
    }
}
