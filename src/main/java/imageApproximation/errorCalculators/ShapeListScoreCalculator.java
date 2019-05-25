package imageApproximation.errorCalculators;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.util.List;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public class ShapeListScoreCalculator implements ToDoubleFunction<List<BasicShape>> {
    private final ToDoubleBiFunction<ImageWrapper, ImageWrapper> errorCalculator;
    private final ImageWrapper targetImage;
    private final int sparsity;

    public ShapeListScoreCalculator(
            ToDoubleBiFunction<ImageWrapper, ImageWrapper> errorCalculator,
            ImageWrapper targetImage, int sparsity) {
        this.errorCalculator = errorCalculator;
        this.targetImage = targetImage;
        this.sparsity = sparsity;
    }

    @Override
    public double applyAsDouble(List<BasicShape> value) {
        return errorCalculator.applyAsDouble(
                ShapeDrawer.drawManyShapesSparsely(value, targetImage.getWidth(), targetImage.getHeight(), sparsity),
                targetImage);
    }
}
