package imageApproximation.LearningAlgorithms;

import imageApproximation.organisms.MockOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GeneticAlgorithmTest {
    private static final int SIZABLE_POPULATION = 100;
    private static final double SURVIVAL_RATE = .1;
    private static final int SURVIVAL_SIZE = (int) Math.round(SIZABLE_POPULATION * SURVIVAL_RATE);

    private static OrganismInterface mockProgenitor;
    private static ToDoubleFunction<OrganismInterface> mockFitnessFunction;
    private GeneticAlgorithm inspected;
    private static List<OrganismInterface> predeterminedPopulation;
    private static List<OrganismInterface> fittestSpecimen;

    @BeforeAll
    static void setupInitialPopulations() {
        mockProgenitor = makeOneGenerationMock();

        predeterminedPopulation = new ArrayList<>(SIZABLE_POPULATION);
        fittestSpecimen = new ArrayList<>(SURVIVAL_SIZE);
        for (int i = 0; i < SIZABLE_POPULATION; i++) {
            OrganismInterface organism = makeOneGenerationMock();
            predeterminedPopulation.add(organism);
            if (i < SURVIVAL_SIZE) {
                fittestSpecimen.add(organism);
            }
        }
        mockFitnessFunction = value -> (fittestSpecimen.contains(value)) ? 100 : 0;

    }

    private static OrganismInterface makeOneGenerationMock() {
        OrganismInterface mock = mock(OrganismInterface.class);
        when(mock.cloneOrganism()).thenReturn(mock);
        when(mock.spawnMutant()).thenReturn(mock(OrganismInterface.class));
        when(mock.crossBreed(any())).thenReturn(mock(OrganismInterface.class));
        return mock;
    }

    @BeforeEach
    void setUp() {
        inspected = new GeneticAlgorithm(predeterminedPopulation, SURVIVAL_RATE, mockFitnessFunction);
    }

    @Test
    void initiallyMutatesProgenitorIntoEntirePopulation() {
        new GeneticAlgorithm(SIZABLE_POPULATION, 0, mockProgenitor, x -> 0);
        verify(mockProgenitor, times(SIZABLE_POPULATION - 1)).spawnMutant();
    }

    @Test
    void canReturnEntirePopulationWhenRequested() {
        assertEquals(SIZABLE_POPULATION, inspected.getPopulation().size());
    }

    @Test
    void canReturnBestOffspringSoFarAccordingToFitnessFunction() {
        ToDoubleFunction<OrganismInterface> fitnessFunction = organism -> organism == mockProgenitor ? 100 : 0;
        GeneticAlgorithm inspected = new GeneticAlgorithm(SIZABLE_POPULATION, 0, mockProgenitor, fitnessFunction);

        assertSame(mockProgenitor, inspected.getFittestOrganism());
    }

    @Test
    void whenAdvancingAGenerationTheFittestSpecimenSurvive() {
        inspected.advanceGeneration();
        List<OrganismInterface> newPopulation = inspected.getPopulation();

        for (OrganismInterface organism : newPopulation) {
            if (predeterminedPopulation.contains(organism)) {
                assertTrue(fittestSpecimen.contains(organism));
            }
        }
    }

    @Test
    void whenAdvancingAGenerationTheFittestSpecimenAreCrossBred() {
        inspected.advanceGeneration();

        for (OrganismInterface organism : fittestSpecimen.subList(0, SURVIVAL_SIZE - 1)) {
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
    void whenAdvancingAGenerationTheNewPopulationIsTheSameSizeAsBefore() {
        inspected.advanceGeneration();
        assertEquals(SIZABLE_POPULATION, inspected.getPopulation().size());
    }

    @Test
    void whenAdvancingAGenerationTheNewPopulationHasNoClones() {
        //this test uses MockOrganism instead of regular mocks since mockito starts recycling mocks at some point
        GeneticAlgorithm inspected = new GeneticAlgorithm(SIZABLE_POPULATION, 0.1, new MockOrganism(), x -> 0);
        inspected.advanceGeneration();
        Set<OrganismInterface> populationSet = new HashSet<>(inspected.getPopulation());
        assertEquals(SIZABLE_POPULATION, populationSet.size());
    }
}