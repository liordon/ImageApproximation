package imageApproximation.steppers;

import imageApproximation.graphics.ImageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.shapes.GraphicsForTests.WHITE_PIXEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class RandomBasicShapeStepperTest {

    private RandomBasicShapeStepper inspected;
    private ToDoubleBiFunction<ImageWrapper, ImageWrapper> mockScoreFunction;

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
        assertEquals(ImageWrapper.class, inspected.getCurrentState().getClass());
    }

    @Test
    void stateImageDimensionsEqualTargetImageDimensions() {
        ImageWrapper someTarget = new ImageWrapper(4, 2);
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
        ImageWrapper initialState = inspected.getCurrentState();

        inspected.step();

        assertNotEquals(initialState, inspected.getCurrentState());
    }

}