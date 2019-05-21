package imageApproximation.organisms;

import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.ShapeDrawer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.ToDoubleBiFunction;

import static imageApproximation.graphics.shapes.GraphicsForTests.WHITE_PIXEL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CircleOrganismTest {

    private CircleOrganism inspected;
    private ToDoubleBiFunction<ImageWrapper, ImageWrapper> mockScoreFunction;

    @BeforeEach
    void setUp() {
        mockScoreFunction = mock(ToDoubleBiFunction.class);
        when(mockScoreFunction.applyAsDouble(any(), any())).thenReturn(255 * 255.).thenReturn(0.);
        inspected = new CircleOrganism(WHITE_PIXEL, mockScoreFunction);
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
        inspected = new CircleOrganism(someTarget, mockScoreFunction);
        assertEquals(someTarget.getWidth(), inspected.getCurrentState().getWidth());
        assertEquals(someTarget.getHeight(), inspected.getCurrentState().getHeight());
    }

    @Test
    void whenMakingAStepTowardsTargetImageScoreIsRecalculated() {
        double initialScore = inspected.getCurrentError();
        verify(mockScoreFunction, times(1)).applyAsDouble(any(), eq(WHITE_PIXEL));

        inspected = (CircleOrganism) inspected.spawnMutant();

        assertNotEquals(initialScore, inspected.getCurrentError());
        verify(mockScoreFunction, times(2)).applyAsDouble(any(), eq(WHITE_PIXEL));
    }

    @Test
    void whenMakingAStepTowardsTargetImageCurrentStateIsChanged() {
        ImageWrapper initialState = inspected.getCurrentState();

        inspected = (CircleOrganism) inspected.spawnMutant();

        assertNotEquals(initialState, inspected.getCurrentState());
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
    void drawingAllTakenStepsResultsInCurrentState(){
        for (int i = 0; i < 10; i++) {
            inspected = (CircleOrganism) inspected.spawnMutant();
        }
        ImageWrapper blank_canvas = new ImageWrapper(inspected.getCurrentState().getWidth(), inspected.getCurrentState().getHeight());
        assertEquals(inspected.getCurrentState(), ShapeDrawer.drawMany(inspected.getGenome(), blank_canvas));
    }


}