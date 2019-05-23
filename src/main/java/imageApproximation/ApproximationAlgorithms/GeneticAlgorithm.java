package imageApproximation.ApproximationAlgorithms;

import imageApproximation.organisms.OrganismInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class GeneticAlgorithm {

    private final int generationSize;
    private final int survivorsSize;
    private final ToDoubleFunction<OrganismInterface> fitnessFunction;
    private final Comparator<OrganismInterface> comparativeFitnessFunction;
    private List<OrganismInterface> population;
    private List<OrganismInterface> survivors;
    private OrganismInterface fittestOrganism;
    private double highestFitness=-99999;

    GeneticAlgorithm(List<OrganismInterface> initialPopulation, double survivalRate, ToDoubleFunction<OrganismInterface> fitnessFunction) {
        population = initialPopulation;
        generationSize = initialPopulation.size();
        survivorsSize = (int) Math.round(generationSize * survivalRate);
        this.fitnessFunction = fitnessFunction;
        this.comparativeFitnessFunction = (a, b) -> (int) Math.signum(fitnessFunction.applyAsDouble((a)) - fitnessFunction.applyAsDouble(b));
    }

    public GeneticAlgorithm(int generationSize, double survivalRate, OrganismInterface progenitor, ToDoubleFunction<OrganismInterface> fitnessFunction) {
        this.generationSize = generationSize;
        this.survivorsSize = (int) Math.round(generationSize * survivalRate);
        this.fitnessFunction = fitnessFunction;
        this.comparativeFitnessFunction = (a, b) -> (int) Math.signum(fitnessFunction.applyAsDouble((a)) - fitnessFunction.applyAsDouble(b));

        fittestOrganism = progenitor;

        population = new ArrayList<>(generationSize);
        population = new ArrayList<>(survivorsSize);
        population.add(progenitor);
        fittestOrganism = progenitor;
        highestFitness = fitnessFunction.applyAsDouble(progenitor);
        for (int i = 0; i < generationSize - 1; i++) {
            OrganismInterface mutant = progenitor.spawnMutant();
            population.add(mutant);
            double mutantFitness = fitnessFunction.applyAsDouble(mutant);
            if (mutantFitness > highestFitness) {
                fittestOrganism = mutant;
                highestFitness = mutantFitness;
            }
        }
    }

    public List<OrganismInterface> getPopulation() {
        return population;
    }

    public OrganismInterface getFittestOrganism() {
        return fittestOrganism;
    }

    public void advanceGeneration() {
        List<OrganismInterface> survivors = new ArrayList<>(survivorsSize);
        for (OrganismInterface organism : population) {
            if (survivors.size() < survivorsSize
                    || fitnessFunction.applyAsDouble(organism) > fitnessFunction.applyAsDouble(survivors.get(survivorsSize - 1))) {
                survivors.add(organism);
                survivors.sort(comparativeFitnessFunction);
            }
        }

        List<OrganismInterface> newGeneration = new ArrayList<>(generationSize);

        //step 1: survival of the fittest
        newGeneration.addAll(survivors);
        for (OrganismInterface survivor : survivors) {
            newGeneration.add(survivor.spawnMutant());
        }

        //step 2: crossbreeds
        for (int i = 0; i < survivorsSize; i++) {
            for (int j = i; j < survivorsSize; j++) {
                if (newGeneration.size() == generationSize) {
                    break;
                }
                if (i != j) {
                    newGeneration.add(survivors.get(i).crossBreed(survivors.get(j)));
                }
            }
        }

        //step 3: fill up the remainder of the generation
        while (newGeneration.size() < generationSize){
            newGeneration.add(survivors.get(0).spawnMutant());
        }

        for (OrganismInterface organism : population) {
            double organismFitness = fitnessFunction.applyAsDouble(organism);
            if (organismFitness > highestFitness) {
                fittestOrganism = organism;
                highestFitness = organismFitness;
            }
        }

        population = newGeneration;
    }
}
