package imageApproximation.errorCalculators;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.ColorChannel;
import imageApproximation.graphics.ImageWrapper;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.ColorChannel.getColorDifferenceOfChannel;

public class MeanSquareErrorScoreCalculator implements ToDoubleBiFunction<ImageWrapper, ImageWrapper> {
    private final int sparsity;
    static final int MAX_POSSIBLE_SCORE = ExerciseConstants.MAX_COLOR_VALUE * ExerciseConstants.MAX_COLOR_VALUE;

    public MeanSquareErrorScoreCalculator(int sparsity) {
        this.sparsity = sparsity;
    }

    public double applyAsDouble(ImageWrapper verified, ImageWrapper target) {
        if (verified.getWidth() * sparsity != target.getWidth()
                || verified.getHeight() * sparsity != target.getHeight()) {
            throw new IllegalArgumentException(
                    String.format("different image dimensions: (%d,%d) with sparsity %d vs (%d,%d)",
                            verified.getWidth(), verified.getHeight(), sparsity,
                    target.getWidth(), target.getHeight()));
        }
        double sum = 0;
        for (int i = 0; i < verified.getWidth(); i += sparsity) {
            for (int j = 0; j < verified.getHeight(); j += sparsity) {
                for (ColorChannel channel : ColorChannel.values()) {
                    int channelDiff =
                            getColorDifferenceOfChannel(verified.getColor(i, j), target.getColor(i, j), channel);
                    sum += Math.pow(channelDiff, 2.);
                }
            }
        }
        return MAX_POSSIBLE_SCORE - (sum / 3. / (verified.getWidth() * verified.getHeight()));
    }

}
