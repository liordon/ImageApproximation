package imageApproximation.errorCalculators;

import imageApproximation.ExerciseConstants;
import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.ShapeDrawer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static imageApproximation.DifficultTestSubjects.LARGE_BLANK_IMAGE;
import static imageApproximation.DifficultTestSubjects.LARGE_SHAPES_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;


class ApproximationScoreCalculatorITest {


    private final int SPARSITY = 20;
    private ApproximationScoreCalculator inspected;

    @BeforeEach
    void setUp() {
        inspected = new ApproximationScoreCalculator(new MeanSquareErrorCalculator(SPARSITY), new ImageWrapper(
                ExerciseConstants.MAX_IMAGE_SIZE, ExerciseConstants.MAX_IMAGE_SIZE), SPARSITY);
    }

    @Test
    void scoreCalculationIsEqualToMSEComparisonOfBothImages() {
        assertEquals(new MeanSquareErrorCalculator(SPARSITY).applyAsDouble(
                ShapeDrawer.drawSparsely(LARGE_SHAPES_LIST, ExerciseConstants.MAX_IMAGE_SIZE,
                        ExerciseConstants.MAX_IMAGE_SIZE, SPARSITY), LARGE_BLANK_IMAGE),
                inspected.applyAsDouble(LARGE_SHAPES_LIST));
    }

    @Test
    void canCalculateTheSameScore100TimesInLessThan1Second() {
        assertTimeout(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < 100; i++) {
                inspected.applyAsDouble(LARGE_SHAPES_LIST);
            }
        });
    }
}