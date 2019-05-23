package imageApproximation.organisms;

import imageApproximation.ExcerciseConstants;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBounderies;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CircleOrganism implements OrganismInterface {
    private static Random random = new Random();
    private final List<BasicShape> genome = new ArrayList<>();
    private final ShapeBounderies shapeBounderies;
    private int maximumMutantVariation = (int) Math.round(ExcerciseConstants.MAX_ALLOWED_SHAPES * .4);
    private int minimumMutantVariation = (int) Math.round(ExcerciseConstants.MAX_ALLOWED_SHAPES * .1);

    public CircleOrganism(ShapeBounderies shapeBounderies) {
        this.shapeBounderies = shapeBounderies;
    }

    public CircleOrganism(CircleOrganism other, ShapeBounderies shapeBounderies) {
        this.shapeBounderies = shapeBounderies;
        this.genome.addAll(other.genome);
    }

    public CircleOrganism(List<BasicShape> circles, ShapeBounderies shapeBounderies) {
        this.shapeBounderies = shapeBounderies;
        this.genome.addAll(circles);
    }

    public static void setRandom(Random random) {
        CircleOrganism.random = random;
    }

    @Override
    public OrganismInterface spawnMutant() {
        List<BasicShape> newGenome = new ArrayList<>(genome.size() + 1);
        newGenome.addAll(genome);

        if (genome.size() < ExcerciseConstants.MAX_ALLOWED_SHAPES) {
            addCircleToGenome(newGenome);
        } else {
            for (int i = 0; i < minimumMutantVariation + random.nextInt(maximumMutantVariation); i++) {
                newGenome.remove(random.nextInt(newGenome.size()));
                addCircleToGenome(newGenome);
            }
        }

        return new CircleOrganism(newGenome, shapeBounderies);
    }

    private void addCircleToGenome(List<BasicShape> newGenome) {
        BasicCircle circle = new BasicCircle(
                random.nextInt(shapeBounderies.getMaxSize()),
                new Color(
                        random.nextInt(ExcerciseConstants.MAX_COLOR_VALUE),
                        random.nextInt(ExcerciseConstants.MAX_COLOR_VALUE),
                        random.nextInt(ExcerciseConstants.MAX_COLOR_VALUE)
                ),
                random.nextDouble(),
                random.nextInt(shapeBounderies.getMaxWidth()),
                random.nextInt(shapeBounderies.getMaxHeight()));
        newGenome.add(circle);
    }

    @Override
    public List<BasicShape> getGenome() {
        return genome;
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return new CircleOrganism(this, shapeBounderies);
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface mate) {
        List<BasicShape> offspringGenome = new ArrayList<>(genome.size() + mate.getGenome().size());
        offspringGenome.addAll(genome.subList(0, genome.size() / 2));
        offspringGenome.addAll(mate.getGenome().subList(0, mate.getGenome().size() / 2));
        Collections.shuffle(offspringGenome);
        return new CircleOrganism(offspringGenome, shapeBounderies);
    }
}
