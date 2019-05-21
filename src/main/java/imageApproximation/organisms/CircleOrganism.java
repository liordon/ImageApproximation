package imageApproximation.organisms;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleBiFunction;

public class CircleOrganism implements OrganismInterface {
    private final static int MAX_COLOR_VALUE = 255;
    private final ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction;
    private final ImageWrapper targetImage;
    private final Random random = new Random();
    private double score;
    private ImageWrapper currentState;
    private final List<BasicShape> listOfAllCircles = new ArrayList<>();

    public CircleOrganism(ImageWrapper targetImage, ToDoubleBiFunction<ImageWrapper, ImageWrapper> scoreFunction) {
        this.targetImage = targetImage;
        this.currentState = new ImageWrapper(targetImage.getWidth(), targetImage.getHeight());
        this.scoreFunction = scoreFunction;
        this.score = scoreFunction.applyAsDouble(currentState, targetImage);
    }

    public CircleOrganism(CircleOrganism other, List<BasicShape> circles) {
        this(other.targetImage, other.scoreFunction);
        this.listOfAllCircles.addAll(circles);
        currentState = ShapeDrawer.drawMany(circles, currentState);
    }

    public double getCurrentError() {
        return score;
    }

    @Override
    public OrganismInterface spawnMutant() {
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
        List<BasicShape> currentSteps = getGenome();
        currentSteps.add(circle);
        return new CircleOrganism(this, currentSteps);
    }

    public ImageWrapper getCurrentState() {
        return currentState;
    }

    @Override
    public java.util.List<BasicShape> getGenome() {
        return listOfAllCircles;
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return new CircleOrganism(targetImage, scoreFunction);
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface mate) {
        return null;
    }
}
