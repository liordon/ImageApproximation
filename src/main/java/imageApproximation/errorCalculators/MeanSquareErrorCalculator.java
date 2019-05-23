package imageApproximation.errorCalculators;

import imageApproximation.graphics.ColorChannel;
import imageApproximation.graphics.ImageWrapper;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.ColorChannel.getColorDifferenceOfChannel;

public class MeanSquareErrorCalculator implements ToDoubleBiFunction<ImageWrapper, ImageWrapper> {
    private final int sparsity;

    public MeanSquareErrorCalculator(int sparsity) {
        this.sparsity = sparsity;
    }

    public double applyAsDouble(ImageWrapper original, ImageWrapper target) {
        if (original.getWidth() != target.getWidth()
                || original.getHeight() != target.getHeight()) {
            throw new IllegalArgumentException(String.format("different image dimensions: (%d,%d) vs (%d,%d)",
                    original.getWidth(), original.getHeight(),
                    target.getWidth(), target.getHeight()));
        }
        double sum = 0;
        for (int i = 0; i < original.getWidth(); i+= sparsity) {
            for (int j = 0; j < original.getHeight(); j+=sparsity) {
                for (ColorChannel channel : ColorChannel.values()) {
                    int channelDiff = getColorDifferenceOfChannel(original.getColor(i, j), target.getColor(i, j), channel);
                    sum += Math.pow(channelDiff, 2.);
                }
            }
        }
        return sum / 3. / (original.getWidth() * original.getHeight());
    }

}
