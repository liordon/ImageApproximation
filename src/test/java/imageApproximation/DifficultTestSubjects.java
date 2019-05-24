package imageApproximation;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public enum DifficultTestSubjects {
    ;
    public static final ShapeBoundaries LARGEST_BOUNDARIES =
            new ShapeBoundaries(ExerciseConstants.MAX_IMAGE_SIZE + 1, ExerciseConstants.MAX_IMAGE_SIZE + 1,
                    ExerciseConstants.MAX_IMAGE_SIZE * 2);
    public static final ImageWrapper LARGE_CIRCLE_IMAGE;
    public static final ImageWrapper LARGE_BLANK_IMAGE =
            new ImageWrapper(ExerciseConstants.MAX_IMAGE_SIZE, ExerciseConstants.MAX_IMAGE_SIZE);

    public static final List<BasicShape> LARGE_SHAPES_LIST;

    static {
        LARGE_SHAPES_LIST = new LinkedList<>();
        for (int i = 0; i < ExerciseConstants.MAX_ALLOWED_SHAPES; i++) {
            LARGE_SHAPES_LIST.add(new BasicCircle(10*i, Color.WHITE, 0.5, i, i));
        }
        LARGE_CIRCLE_IMAGE = ShapeDrawer.drawMany(LARGE_SHAPES_LIST, LARGE_BLANK_IMAGE);
    }
}
