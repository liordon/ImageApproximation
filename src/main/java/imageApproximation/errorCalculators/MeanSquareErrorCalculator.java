package imageApproximation.errorCalculators;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.ColorChannel;
import imageApproximation.graphics.ImageWrapper;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.ColorChannel.getColorDifferenceOfChannel;

public class MeanSquareErrorCalculator implements ToDoubleBiFunction<ImageWrapper, ImageWrapper> {
    private final int sparsity;
    static final int MAX_POSSIBLE_SCORE = ExerciseConstants.MAX_COLOR_VALUE * ExerciseConstants.MAX_COLOR_VALUE;

    public MeanSquareErrorCalculator(int sparsity) {
        this.sparsity = sparsity;
    }

    @Override
    public double applyAsDouble(ImageWrapper verified, ImageWrapper target) {
        if (verified.getWidth() * sparsity != target.getWidth()
                || verified.getHeight() * sparsity != target.getHeight()) {
            throw new IllegalArgumentException(
                    String.format("different image dimensions: (%d,%d) with sparsity %d vs (%d,%d)",
                            verified.getWidth(), verified.getHeight(), sparsity,
                    target.getWidth(), target.getHeight()));
        }
        double sum = 0;
        for (int i = 0; i < verified.getWidth(); i++) {
            for (int j = 0; j < verified.getHeight(); j++) {
                for (ColorChannel channel : ColorChannel.values()) {
                    int channelDiff =
                            getColorDifferenceOfChannel(verified.getColorAtPixel(i, j),
                                    target.getColorAtPixel(i * sparsity, j * sparsity), channel);
                    sum += Math.pow(channelDiff, 2.);
                }
            }
        }
        return MAX_POSSIBLE_SCORE - (sum / 3. / (verified.getWidth() * verified.getHeight()));
    }

}
