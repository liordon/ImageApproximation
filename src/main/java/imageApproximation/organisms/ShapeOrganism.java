package imageApproximation.organisms;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;

import java.util.*;

public abstract class ShapeOrganism implements OrganismInterface {
    static Random random = new Random();
    final List<BasicShape> genome = new ArrayList<>();
    final ShapeBoundaries shapeBoundaries;
    private int maximumMutantVariation = (int) Math.round(ExerciseConstants.MAX_ALLOWED_SHAPES * .6);
    private int minimumMutantVariation = (int) Math.round(ExerciseConstants.MAX_ALLOWED_SHAPES * .1);

    public ShapeOrganism(ShapeBoundaries shapeBoundaries) {
        this.shapeBoundaries = shapeBoundaries;
    }

    public ShapeOrganism(ShapeBoundaries shapeBoundaries, int numberOfGenes) {
        this.shapeBoundaries = shapeBoundaries;
        for (int i = 0; i < numberOfGenes; i++) {
            addShapeToGenome(getGenome());
        }
    }

    abstract ShapeOrganism siblingWithSpecificGenome(List<BasicShape> genome, ShapeBoundaries shapeBoundaries);

    public static void setRandom(Random random) {
        ShapeOrganism.random = random;
    }

    @Override
    public OrganismInterface spawnMutant() {
        List<BasicShape> newGenome = new ArrayList<>(genome.size() + 1);
        newGenome.addAll(genome);

        final int mutationRatio = minimumMutantVariation + random.nextInt(maximumMutantVariation);
        final boolean initiallyFullGenome = genome.size() >= ExerciseConstants.MAX_ALLOWED_SHAPES;

        for (int i = 0; i < mutationRatio; i++) {
            if (initiallyFullGenome) {
                newGenome.remove(random.nextInt(newGenome.size()));
            } else if (genome.size() < ExerciseConstants.MAX_ALLOWED_SHAPES) {
                addShapeToGenome(newGenome);
            } else {
                break;
            }
        }
        Collections.shuffle(newGenome);
        return siblingWithSpecificGenome(newGenome, shapeBoundaries);
    }

    abstract void addShapeToGenome(List<BasicShape> newGenome);

    @Override
    public List<BasicShape> getGenome() {
        return genome;
    }

    @Override
    public OrganismInterface cloneOrganism() {
        return siblingWithSpecificGenome(this.getGenome(), shapeBoundaries);
    }

    @Override
    public OrganismInterface crossBreed(OrganismInterface... mates) {
        List<BasicShape> offspringGenome = new ArrayList<>(genome.size());
        List<BasicShape> previousGenome = new LinkedList<>(genome);
        for (OrganismInterface mate : mates) {
            offspringGenome.addAll(previousGenome.subList(0, previousGenome.size() / 2));
            offspringGenome.addAll(mate.getGenome().subList(mate.getGenome().size() / 2, mate.getGenome().size()));
            Collections.shuffle(offspringGenome);
            previousGenome = new LinkedList<>(offspringGenome.subList(0, offspringGenome.size() / 2));
        }
        return siblingWithSpecificGenome(offspringGenome, shapeBoundaries);
    }
}
