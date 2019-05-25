package imageApproximation.organisms;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;

import java.awt.*;
import java.util.List;

public class CircleOrganism extends ShapeOrganism {

    public CircleOrganism(ShapeBoundaries shapeBoundaries) {
        super(shapeBoundaries);
    }

    public CircleOrganism(ShapeBoundaries shapeBoundaries, int numberOfGenes) {
        super(shapeBoundaries, numberOfGenes);
    }

    public CircleOrganism(List<BasicShape> circles, ShapeBoundaries shapeBoundaries) {
        super(shapeBoundaries);
        this.genome.addAll(circles);
    }

    @Override
    ShapeOrganism siblingWithSpecificGenome(List<BasicShape> genome, ShapeBoundaries shapeBoundaries) {
        return new CircleOrganism(genome, shapeBoundaries);
    }

    @Override
    void addShapeToGenome(List<BasicShape> newGenome) {
        BasicCircle circle = new BasicCircle(
                random.nextInt(shapeBoundaries.getMaxSize()),
                new Color(
                        random.nextInt(ExerciseConstants.MAX_COLOR_VALUE),
                        random.nextInt(ExerciseConstants.MAX_COLOR_VALUE),
                        random.nextInt(ExerciseConstants.MAX_COLOR_VALUE)
                ),
                random.nextDouble(),
                random.nextInt(shapeBoundaries.getMaxWidth()),
                random.nextInt(shapeBoundaries.getMaxHeight()));
        newGenome.add(circle);
    }
}
