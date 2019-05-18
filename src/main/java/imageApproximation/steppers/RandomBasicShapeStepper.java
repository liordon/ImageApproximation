package imageApproximation.steppers;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.awt.*;
import java.util.function.ToDoubleBiFunction;

public class RandomBasicShapeStepper {
    private final ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction;
    private final ImageWrapper targetImage;
    private double score;
    private ImageWrapper currentState;

    public RandomBasicShapeStepper(ImageWrapper targetImage, ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction) {
        this.targetImage = targetImage;
        this.currentState = new ImageWrapper(targetImage.getWidth(), targetImage.getHeight());
        this.scoreFunction = scoreFunction;
        this.score = scoreFunction.applyAsDouble(currentState, targetImage);
    }

    public double getCurrentError() {
        return score;
    }

    public void step() {
        currentState = ShapeDrawer.draw(new BasicCircle(1, Color.GREEN, 1, 0, 0), currentState);
        score = scoreFunction.applyAsDouble(currentState, targetImage);
    }

    public ImageWrapper getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ImageWrapper currentState) {
        this.currentState = currentState;
    }
}
