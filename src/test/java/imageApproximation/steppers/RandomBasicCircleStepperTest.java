package imageApproximation.steppers;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.ShapeDrawer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.shapes.GraphicsForTests.WHITE_PIXEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class RandomBasicCircleStepperTest {

    private RandomBasicCircleStepper inspected;
    private ToDoubleBiFunction<ImageWrapper, ImageWrapper> mockScoreFunction;

    @BeforeEach
    void setUp() {
        mockScoreFunction = mock(ToDoubleBiFunction.class);
        when(mockScoreFunction.applyAsDouble(any(), any())).thenReturn(255 * 255.).thenReturn(0.);
        inspected = new RandomBasicCircleStepper(WHITE_PIXEL, mockScoreFunction);
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
        inspected = new RandomBasicCircleStepper(someTarget, mockScoreFunction);
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

    @Test
    void canReportInitiallyEmptyListOfStepsTaken(){
        assertEquals(0, inspected.getListOfStepsTaken().size());
    }

    @Test
    void eachStepAddsUpToTakenStepsList(){
        inspected.step();
        inspected.step();
        inspected.step();

        assertEquals(3, inspected.getListOfStepsTaken().size());
    }

    @Test
    void drawingAllTakenStepsResultsInCurrentState(){
        for (int i = 0; i < 10; i++) {
            inspected.step();
        }
        ImageWrapper blank_canvas = new ImageWrapper(inspected.getCurrentState().getWidth(), inspected.getCurrentState().getHeight());
        assertEquals(inspected.getCurrentState(), ShapeDrawer.drawMany(inspected.getListOfStepsTaken(), blank_canvas));
    }


}