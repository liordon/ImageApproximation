package imageApproximation.steppers;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleBiFunction;

public class RandomBasicCircleStepper {
    private final static int MAX_COLOR_VALUE = 255;
    private final ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction;
    private final ImageWrapper targetImage;
    private final Random random = new Random();
    private double score;
    private ImageWrapper currentState;
    private final List<BasicShape> allStepsTaken = new ArrayList<>();

    public RandomBasicCircleStepper(ImageWrapper targetImage, ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction) {
        this.targetImage = targetImage;
        this.currentState = new ImageWrapper(targetImage.getWidth(), targetImage.getHeight());
        this.scoreFunction = scoreFunction;
        this.score = scoreFunction.applyAsDouble(currentState, targetImage);
    }

    public double getCurrentError() {
        return score;
    }

    public void step() {
        BasicCircle circle = new BasicCircle(
                random.nextInt(currentState.getWidth() * 2),
                new Color(
                        random.nextInt(MAX_COLOR_VALUE),
                        random.nextInt(MAX_COLOR_VALUE),
                        random.nextInt(MAX_COLOR_VALUE)
                ),
                random.nextDouble(),
                random.nextInt(currentState.getWidth()),
                random.nextInt(currentState.getHeight()));
        allStepsTaken.add(circle);
        currentState = ShapeDrawer.drawOne(circle, currentState);
        score = scoreFunction.applyAsDouble(currentState, targetImage);
    }

    public ImageWrapper getCurrentState() {
        return currentState;
    }

    public java.util.List<BasicShape> getListOfStepsTaken() {
        return allStepsTaken;
    }
}
