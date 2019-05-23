package imageApproximation.organisms;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.shapes.BasicCircle;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class CircleOrganismTest {

    private CircleOrganism inspected;
    private List<BasicShape> fullGenome1, fullGenome2;
    private ShapeBoundaries shapeBoundaries = new ShapeBoundaries(10, 10, 10);


    @BeforeEach
    void setUp() {
        inspected = new CircleOrganism(shapeBoundaries);
        fullGenome1 = new ArrayList<>(ExerciseConstants.MAX_ALLOWED_SHAPES);
        fullGenome2 = new ArrayList<>(ExerciseConstants.MAX_ALLOWED_SHAPES);
        for (int i = 0; i < ExerciseConstants.MAX_ALLOWED_SHAPES; i++) {
            fullGenome1.add(new BasicCircle(0, Color.BLACK, 0, 0, 0));
            fullGenome2.add(new BasicCircle(0, Color.BLACK, 0, 0, 0));
        }

        CircleOrganism.setRandom(new Random(0));
    }

    @Test
    void canReportInitiallyEmptyListOfStepsTaken(){
        assertEquals(0, inspected.getGenome().size());
    }

    @Test
    void eachMutationAddsUpToTakenGenesList() {
        inspected = (CircleOrganism) inspected.spawnMutant();
        inspected = (CircleOrganism) inspected.spawnMutant();
        inspected = (CircleOrganism) inspected.spawnMutant();

        assertEquals(3, inspected.getGenome().size());
    }

    @Test
    void mutantOrganismHasDifferentGenomeThanOriginal() {
        assertNotEquals(inspected.getGenome(), inspected.spawnMutant().getGenome());
    }

    @Test
    void fullGenomeMutantOrganismHasDifferentGenomeThanOriginal() {
        CircleOrganism fullGenomeOrganism = new CircleOrganism(fullGenome1, shapeBoundaries);
        assertNotEquals(fullGenomeOrganism.getGenome(), fullGenomeOrganism.spawnMutant().getGenome());
    }

    @Test
    void mutantOfParentWithMaxSizedGenomeWillNotExceedThisSize() {
        OrganismInterface fullGenomeOrganism = new CircleOrganism(fullGenome1, shapeBoundaries).spawnMutant();
        assertNotEquals(fullGenomeOrganism.getGenome(), fullGenomeOrganism.spawnMutant().getGenome());
        assertTrue(fullGenomeOrganism.getGenome().size() <= ExerciseConstants.MAX_ALLOWED_SHAPES,
                () -> "The maximum genome size is " + ExerciseConstants.MAX_ALLOWED_SHAPES
                        + " but mutant's genome is sized " + fullGenomeOrganism.getGenome().size());
    }

    @Test
    void crossBredChildRetainsHalfGenomeFromEachParent() {
        List<BasicShape> genome1 = new ArrayList<>(2);
        BasicCircle parent1gene1 = mock(BasicCircle.class);
        genome1.add(parent1gene1);
        BasicCircle parent1gene2 = mock(BasicCircle.class);
        genome1.add(parent1gene2);

        List<BasicShape> genome2 = new ArrayList<>(2);
        BasicCircle parent2gene1 = mock(BasicCircle.class);
        genome2.add(parent2gene1);
        BasicCircle parent2gene2 = mock(BasicCircle.class);
        genome2.add(parent2gene2);

        CircleOrganism parent1 = new CircleOrganism(genome1, shapeBoundaries);
        CircleOrganism parent2 = new CircleOrganism(genome2, shapeBoundaries);

        OrganismInterface offspring1 = parent1.crossBreed(parent2);

        List<BasicShape> offspring1Genome = offspring1.getGenome();
        assertTrue(offspring1Genome.contains(parent1gene1) || offspring1Genome.contains(parent1gene2));
        assertTrue(offspring1Genome.contains(parent2gene1) || offspring1Genome.contains(parent2gene2));
        assertEquals(2, offspring1Genome.size());
    }

    @Test
    void offspringOfMaxGenomeOrganismsDoesNotExceedMaxGenome() {
        CircleOrganism parent1 = new CircleOrganism(fullGenome1, shapeBoundaries);
        CircleOrganism parent2 = new CircleOrganism(fullGenome2, shapeBoundaries);

        OrganismInterface offspring = parent1.crossBreed(parent2);

        assertEquals(ExerciseConstants.MAX_ALLOWED_SHAPES, offspring.getGenome().size());
    }

    @Test
    void mutantOfFullGenomeOrganismHasBetween10and50PercentMutations() {
        CircleOrganism parent1 = new CircleOrganism(fullGenome1, shapeBoundaries);
        OrganismInterface mutant = parent1.spawnMutant();

        List<BasicShape> parentGenome = parent1.getGenome();
        List<BasicShape> mutantGenome = mutant.getGenome();

        int numberOfInheritedGenes = 0;
        for (BasicShape gene :
                parentGenome) {
            if (mutantGenome.contains(gene)) {
                numberOfInheritedGenes++;
            }
        }

        assertTrue(numberOfInheritedGenes < ExerciseConstants.MAX_ALLOWED_SHAPES * .9);
        assertTrue(numberOfInheritedGenes > ExerciseConstants.MAX_ALLOWED_SHAPES * .5);
    }

    @Test
    void crossbreedsHaveShuffledGenomes() {
        CircleOrganism parent1 = new CircleOrganism(fullGenome1, shapeBoundaries);
        CircleOrganism parent2 = new CircleOrganism(fullGenome2, shapeBoundaries);

        OrganismInterface crossbreed = parent1.crossBreed(parent2);

        int numberOfGenesInPlace = 0;
        for (int i = 0; i < ExerciseConstants.MAX_ALLOWED_SHAPES / 2; i++) {
            if (parent1.getGenome().get(i) == crossbreed.getGenome().get(i) ||
                    parent2.getGenome().get(i) == crossbreed.getGenome().get(i)) {
                numberOfGenesInPlace++;
            }
        }
        assertTrue(numberOfGenesInPlace < 0.1 * ExerciseConstants.MAX_ALLOWED_SHAPES);
    }
}