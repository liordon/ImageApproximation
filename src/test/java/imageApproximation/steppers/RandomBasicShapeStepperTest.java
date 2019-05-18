package imageApproximation.steppers;

import static imageApproximation.steppers.shapes.ShapesForTests.WHITE_PIXEL;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.function.ToDoubleBiFunction;


class RandomBasicShapeStepperTest {

    private RandomBasicShapeStepper inspected;
    private ToDoubleBiFunction<BufferedImage, BufferedImage> mockScoreFunction;

    @BeforeEach
    void setUp() {
        mockScoreFunction = mock(ToDoubleBiFunction.class);
        when(mockScoreFunction.applyAsDouble(any(), any())).thenReturn(255 * 255.).thenReturn(0.);
        inspected = new RandomBasicShapeStepper(WHITE_PIXEL, mockScoreFunction);
    }

    @Test
    void canCalculateDistanceBetweenCurrentStateAndTargetImage() {
        assertEquals(255 * 255, inspected.getCurrentError());
    }

    @Test
    void canReturnCurrentStateForInspection() {
        assertEquals(BufferedImage.class, inspected.getCurrentState().getClass());
    }

    @Test
    void stateImageDimensionsEqualTargetImageDimensions() {
        BufferedImage someTarget = new BufferedImage(4, 2, TYPE_INT_RGB);
        inspected = new RandomBasicShapeStepper(someTarget, mockScoreFunction);
        assertEquals(someTarget.getWidth(), inspected.getCurrentState().getWidth());
        assertEquals(someTarget.getHeight(), inspected.getCurrentState().getHeight());
    }

    @Test
    void whenMakingAStepTowardsTargetImageScoreIsRecalculated() {
        double initialScore = inspected.getCurrentError();
        verify(mockScoreFunction, times(1)).applyAsDouble(any(), eq(WHITE_PIXEL));

        inspected.step();

        assertNotEquals(initialScore, inspected.getCurrentError());
        verify(mockScoreFunction, times(2)).applyAsDouble(any(), eq(WHITE_PIXEL));
    }

    @Test
    void whenMakingAStepTowardsTargetImageCurrentStateIsChanged() {
        BufferedImage initialState = inspected.getCurrentState();

        inspected.step();

        assertNotEquals(initialState, inspected.getCurrentState());
    }

}