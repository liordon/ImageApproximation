package imageApproximation.ApproximationAlgorithms;

import imageApproximation.ExerciseConstants;
import imageApproximation.errorCalculators.ApproximationScoreCalculator;
import imageApproximation.errorCalculators.MeanSquareErrorCalculator;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.ToDoubleFunction;

import static imageApproximation.DifficultTestSubjects.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;


class GeneticAlgorithmITest {
    private static final int SIZABLE_POPULATION = 100;
    private static final int SURVIVAL_SIZE = 10;
    private static final int sparsity = 10;

    private static OrganismInterface circleProgenitor;
    private static ToDoubleFunction<OrganismInterface> mseFitnessFunction;

    private GeneticAlgorithm inspected;

    @BeforeAll
    static void setupInitialPopulations() {
        circleProgenitor = new CircleOrganism(LARGE_SHAPES_LIST, LARGEST_BOUNDARIES);

        MeanSquareErrorCalculator meanSquareErrorCalculator = new MeanSquareErrorCalculator(sparsity);
        ApproximationScoreCalculator scoreCalculator =
                new ApproximationScoreCalculator(meanSquareErrorCalculator, LARGE_BLANK_IMAGE, sparsity);
        mseFitnessFunction =
                (o) -> -scoreCalculator.applyAsDouble(o.getGenome());
    }

    @BeforeEach
    void setUp() {
        inspected = new GeneticAlgorithm(SIZABLE_POPULATION, SURVIVAL_SIZE, circleProgenitor, mseFitnessFunction);
    }

    @Test
    void aSingleGenerationShouldTakeLessThan1Second() {
        assertTimeout(Duration.ofSeconds(1), () -> inspected.advanceGeneration());
    }

}