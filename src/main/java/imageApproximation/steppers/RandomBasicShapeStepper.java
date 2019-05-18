package imageApproximation.steppers;

import imageApproximation.steppers.shapes.BasicCircle;
import imageApproximation.steppers.shapes.ShapeDrawer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.ToDoubleBiFunction;

public class RandomBasicShapeStepper {
    private final ToDoubleBiFunction<BufferedImage, BufferedImage> scoreFunction;
    private final BufferedImage targetImage;
    double score;
    private BufferedImage currentState;

    public RandomBasicShapeStepper(BufferedImage targetImage, ToDoubleBiFunction<BufferedImage, BufferedImage> scoreFunction) {
        this.targetImage = targetImage;
        this.currentState = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_RGB);
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

    public BufferedImage getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BufferedImage currentState) {
        this.currentState = currentState;
    }
}
