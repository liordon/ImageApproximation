package imageApproximation.ApproximationAlgorithms;

import imageApproximation.organisms.OrganismInterface;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class GeneticAlgorithm {

    private final int generationSize;
    private final int survivorsSize;
    private final ToDoubleFunction<OrganismInterface> fitnessFunction;
    private final Comparator<OrganismInterface> comparativeFitnessFunction;
    private List<OrganismInterface> survivors;
    private OrganismInterface fittestOrganism, lowliestMutant;
    private double highestFitness, lowestFitness;

    GeneticAlgorithm(List<OrganismInterface> initialPopulation, int generationSize,
                     ToDoubleFunction<OrganismInterface> fitnessFunction) {
        survivors = new ArrayList<>(initialPopulation);
        this.generationSize = generationSize;
        survivorsSize = initialPopulation.size();
        this.fitnessFunction = fitnessFunction;
        this.comparativeFitnessFunction = (a, b) -> (int) Math.signum(fitnessFunction.applyAsDouble((a)) - fitnessFunction.applyAsDouble(b));
        survivors.sort(comparativeFitnessFunction);
        fittestOrganism = survivors.get(0);
        lowliestMutant = survivors.get(survivorsSize - 1);
        highestFitness = fitnessFunction.applyAsDouble(fittestOrganism);
        lowestFitness = fitnessFunction.applyAsDouble(lowliestMutant);
    }

    public GeneticAlgorithm(int generationSize, int survivalSize, OrganismInterface progenitor,
                            ToDoubleFunction<OrganismInterface> fitnessFunction) {
        this.generationSize = generationSize;
        this.survivorsSize = survivalSize;
        this.fitnessFunction = fitnessFunction;
        this.comparativeFitnessFunction = (a, b) -> (int) Math.signum(fitnessFunction.applyAsDouble((a)) - fitnessFunction.applyAsDouble(b));

        survivors = new ArrayList<>(survivorsSize);
        survivors.add(progenitor);
        fittestOrganism = progenitor;
        lowliestMutant = progenitor;
        highestFitness = fitnessFunction.applyAsDouble(progenitor);
        lowestFitness = fitnessFunction.applyAsDouble(progenitor);
        for (int i = 0; i < survivorsSize - 1; i++) {
            OrganismInterface mutant = progenitor.spawnMutant();
            survivors.add(mutant);
            double mutantFitness = fitnessFunction.applyAsDouble(mutant);
            if (mutantFitness > highestFitness) {
                fittestOrganism = mutant;
                highestFitness = mutantFitness;
            }
            if (mutantFitness < lowestFitness) {
                lowliestMutant = mutant;
                lowestFitness = mutantFitness;
            }
        }
    }

    public List<OrganismInterface> getPopulation() {
        return survivors;
    }

    public OrganismInterface getFittestOrganism() {
        return fittestOrganism;
    }

    public void advanceGeneration() {

        //step 1: survival of the fittest
        List<OrganismInterface> newGeneration = new LinkedList<>();
        List<OrganismInterface> misfits = new LinkedList<>();
        for (OrganismInterface survivor : survivors) {
            OrganismInterface mutant = survivor.spawnMutant();
            if (fitnessFunction.applyAsDouble(mutant) > lowestFitness) {
                newGeneration.add(mutant);
            } else {
                misfits.add(mutant);
            }
        }

        //step 2: crossbreeds
        crossbreeding:
        for (int i = 0; i < survivorsSize; i++) {
            for (int j = i; j < survivorsSize; j++) {
                if (newGeneration.size() == generationSize) {
                    break crossbreeding;
                }
                OrganismInterface crossBreed;
                if (i != j) {
                    crossBreed = survivors.get(i).crossBreed(survivors.get(j));
                } else if (misfits.size() > 0) {
                    crossBreed = survivors.get(i).crossBreed(misfits.get(new Random().nextInt(misfits.size())));
                } else {
                    crossBreed = survivors.get(i).crossBreed(newGeneration.get(new Random().nextInt(newGeneration.size())));
                }
                if (fitnessFunction.applyAsDouble(crossBreed) > lowestFitness) {
                    newGeneration.add(crossBreed);
                } else {
                    misfits.add(crossBreed);
                }
            }
        }

        survivors = new ArrayList<>(survivorsSize);
        newGeneration.addAll(survivors);
        newGeneration.sort(comparativeFitnessFunction);
        fittestOrganism = newGeneration.get(0);
        highestFitness = fitnessFunction.applyAsDouble(fittestOrganism);
        lowliestMutant = newGeneration.get(survivorsSize - 1);
        lowestFitness = fitnessFunction.applyAsDouble(lowliestMutant);
        survivors.addAll(newGeneration.subList(0, survivorsSize));
    }
}
