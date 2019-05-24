package imageApproximation.errorCalculators;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public class ApproximationScoreCalculator implements ToDoubleFunction<List<BasicShape>> {

    private final ToDoubleBiFunction<ImageWrapper, ImageWrapper> errorCalculator;
    private final Map<List<BasicShape>, Double> scoreCache = new HashMap<>();
    private final ImageWrapper targetImage;
    private final int sparsity;

    public ApproximationScoreCalculator(ToDoubleBiFunction<ImageWrapper, ImageWrapper> errorCalculator,
                                        ImageWrapper targetImage, int sparsity) {
        this.errorCalculator = errorCalculator;
        this.sparsity = sparsity;
        this.targetImage = targetImage;
    }

    @Override
    public double applyAsDouble(List<BasicShape> value) {
        if (!scoreCache.containsKey(value)) {
            scoreCache.put(value, errorCalculator.applyAsDouble(
                    ShapeDrawer.drawSparsely(value, targetImage.getWidth(), targetImage.getHeight(), sparsity),
                    targetImage));
        }
        return scoreCache.get(value);
    }

    public void clearCache() {
        scoreCache.clear();
    }
}
