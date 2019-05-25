package imageApproximation.ApproximationAlgorithms;

import imageApproximation.errorCalculators.CachingScoreCalculator;
import imageApproximation.errorCalculators.MeanSquareErrorCalculator;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static imageApproximation.DifficultTestSubjects.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;


class GeneticAlgorithmITest {
    private static final int SIZABLE_POPULATION = 100;
    private static final int SURVIVAL_SIZE = 10;
    private static final int sparsity = 10;

    private static OrganismInterface circleProgenitor;
    private static ToDoubleFunction<List<BasicShape>> mseFitnessFunction;

    private GeneticAlgorithm inspected;

    @BeforeAll
    static void setupInitialPopulations() {
        circleProgenitor = new CircleOrganism(LARGE_SHAPES_LIST, LARGEST_BOUNDARIES);

        MeanSquareErrorCalculator meanSquareErrorCalculator = new MeanSquareErrorCalculator(sparsity);
        mseFitnessFunction =
                new CachingScoreCalculator(meanSquareErrorCalculator, LARGE_BLANK_IMAGE, sparsity);
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