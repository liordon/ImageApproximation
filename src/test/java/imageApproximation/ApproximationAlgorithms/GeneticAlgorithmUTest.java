package imageApproximation.ApproximationAlgorithms;

import imageApproximation.organisms.MockOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.ToDoubleFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GeneticAlgorithmUTest {
    private static final int SIZABLE_POPULATION = 100;
    private static final int SURVIVAL_SIZE = 10;

    private static OrganismInterface mockProgenitor;
    private static ToDoubleFunction<OrganismInterface> mockFitnessFunction;
    private GeneticAlgorithm inspected;
    private static List<OrganismInterface> fittestSpecimen;

    @BeforeAll
    static void setupInitialPopulations() {
        mockProgenitor = makeOneGenerationMock();

        fittestSpecimen = new ArrayList<>(SURVIVAL_SIZE);
        for (int i = 0; i < SURVIVAL_SIZE; i++) {
            fittestSpecimen.add(makeOneGenerationMock());
        }
        mockFitnessFunction = value -> (fittestSpecimen.contains(value)) ? 100 : 0;

    }

    private static OrganismInterface makeOneGenerationMock() {
        OrganismInterface mock = mock(OrganismInterface.class);
        when(mock.cloneOrganism()).thenReturn(mock);
        when(mock.spawnMutant()).thenReturn(mock(OrganismInterface.class));
        when(mock.crossBreed(any())).thenReturn(mock(OrganismInterface.class));
        when(mock.getGenome()).thenReturn(Collections.emptyList());
        return mock;
    }

    @BeforeEach
    void setUp() {
        inspected = new GeneticAlgorithm(fittestSpecimen, 100, mockFitnessFunction);
    }

    @Test
    void initiallyMutatesProgenitorIntoSurvivorsArray() {
        OrganismInterface selfReturningMock = mock(OrganismInterface.class);
        when(selfReturningMock.getGenome()).thenReturn(Collections.emptyList());
        when(selfReturningMock.spawnMutant()).thenReturn(selfReturningMock);
        new GeneticAlgorithm(SIZABLE_POPULATION, SURVIVAL_SIZE, selfReturningMock, x -> 0);
        verify(selfReturningMock, times(SIZABLE_POPULATION - 1)).spawnMutant();
    }

    @Test
    void canReturnSurvivorPopulationWhenRequested() {
        assertEquals(SURVIVAL_SIZE, inspected.getPopulation().size());
    }

    @Test
    void canReturnBestOffspringSoFarAccordingToFitnessFunction() {
        OrganismInterface multiGenerationMock = new MockOrganism();
        ToDoubleFunction<OrganismInterface> fitnessFunction = organism -> organism == multiGenerationMock ? 100 : 0;
        GeneticAlgorithm inspected = new GeneticAlgorithm(SIZABLE_POPULATION, 10, multiGenerationMock, fitnessFunction);

        assertSame(multiGenerationMock, inspected.getFittestOrganism());
    }

    @Test
    void whenAdvancingAGenerationTheFittestSpecimenSurvive() {
        inspected.advanceGeneration();
        List<OrganismInterface> newPopulation = inspected.getPopulation();

        for (OrganismInterface organism : newPopulation) {
            assertTrue(fittestSpecimen.contains(organism));
        }
    }

    @Test
    void whenAdvancingAGenerationTheFittestSpecimenAreCrossBred() {
        inspected.advanceGeneration();

        for (OrganismInterface organism : fittestSpecimen) {
            assertTrue(inspected.getPopulation().contains(organism));
            verify(organism, atLeastOnce()).crossBreed(any());
        }
    }

    @Test
    void whenAdvancingAGenerationTheFittestSpecimenAreMutated() {
        inspected.advanceGeneration();

        for (OrganismInterface organism : fittestSpecimen.subList(0, SURVIVAL_SIZE - 1)) {
            verify(organism, atLeastOnce()).spawnMutant();
        }
    }

    @Test
    void whenAdvancingAGenerationOnlyTheSurvivorsAreKept() {
        inspected.advanceGeneration();
        assertEquals(SURVIVAL_SIZE, inspected.getPopulation().size());
    }

    @Test
    void whenAdvancingAGenerationTheNewPopulationHasNoClones() {
        //this test uses MockOrganism instead of regular mocks since mockito starts recycling mocks at some point
        GeneticAlgorithm inspected =
                new GeneticAlgorithm(SIZABLE_POPULATION, SURVIVAL_SIZE, new MockOrganism(), x -> 0);
        inspected.advanceGeneration();
        Set<OrganismInterface> populationSet = new HashSet<>(inspected.getPopulation());
        assertEquals(SURVIVAL_SIZE, populationSet.size());
    }
}