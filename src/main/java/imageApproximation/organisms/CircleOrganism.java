package imageApproximation.organisms;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CircleOrganism implements OrganismInterface {
    private static Random random = new Random();
    private final List<BasicShape> genome = new ArrayList<>();
    private final ShapeBoundaries shapeBoundaries;
    private int maximumMutantVariation = (int) Math.round(ExerciseConstants.MAX_ALLOWED_SHAPES * .6);
    private int minimumMutantVariation = (int) Math.round(ExerciseConstants.MAX_ALLOWED_SHAPES * .1);

    public CircleOrganism(ShapeBoundaries shapeBoundaries) {
        this.shapeBoundaries = shapeBoundaries;
    }

    public CircleOrganism(CircleOrganism other, ShapeBoundaries shapeBoundaries) {
        this.shapeBoundaries = shapeBoundaries;
        this.genome.addAll(other.genome);
    }

    public CircleOrganism(List<BasicShape> circles, ShapeBoundaries shapeBoundaries) {
        this.shapeBoundaries = shapeBoundaries;
        this.genome.addAll(circles);
    }

    public static void setRandom(Random random) {
        CircleOrganism.random = random;
    }

    @Override
    public OrganismInterface spawnMutant() {
        List<BasicShape> newGenome = new ArrayList<>(genome.size() + 1);
        newGenome.addAll(genome);

        if (genome.size() < ExerciseConstants.MAX_ALLOWED_SHAPES) {
            addCircleToGenome(newGenome);
        } else {
            for (int i = 0; i < minimumMutantVariation + random.nextInt(maximumMutantVariation); i++) {
                newGenome.remove(random.nextInt(newGenome.size()));
                addCircleToGenome(newGenome);
            }
        }

        return new CircleOrganism(newGenome, shapeBoundaries);
    }

    private void addCircleToGenome(List<BasicShape> newGenome) {
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

    @Override
    public List<BasicShape> getGenome() {
        return genome;
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return new CircleOrganism(this, shapeBoundaries);
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface... mates) {
        List<BasicShape> offspringGenome = new ArrayList<>(genome.size());
        List<BasicShape> previousGenome = new LinkedList<>(genome);
        for (OrganismInterface mate : mates) {
            offspringGenome.addAll(previousGenome.subList(0, previousGenome.size() / 2));
            offspringGenome.addAll(mate.getGenome().subList(0, mate.getGenome().size() / 2));
            Collections.shuffle(offspringGenome);
            previousGenome = new LinkedList<>(offspringGenome.subList(0,offspringGenome.size()/2));
        }
        return new CircleOrganism(offspringGenome, shapeBoundaries);
    }
}
