package imageApproximation.ApproximationAlgorithms;

import imageApproximation.organisms.OrganismInterface;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class GeneticAlgorithm {

    private final int generationSize;
    private final int survivorsSize;
    private final ToDoubleFunction<OrganismInterface> fitnessFunction;
    private final Comparator<OrganismInterface> comparativeFitnessFunction;
    private final Random random = new Random();
    private List<OrganismInterface> survivors;
    private OrganismInterface fittestOrganism, lowliestMutant;
    private double highestFitness, lowestFitness;

    private int compareFitness(OrganismInterface a, OrganismInterface b) {
        return (int) Math.signum(fitnessFunction.applyAsDouble(b) - fitnessFunction.applyAsDouble(a));
    }

    GeneticAlgorithm(List<OrganismInterface> initialPopulation, int generationSize,
                     ToDoubleFunction<OrganismInterface> fitnessFunction) {
        survivors = new ArrayList<>(initialPopulation);
        this.generationSize = generationSize;
        survivorsSize = initialPopulation.size();
        this.fitnessFunction = fitnessFunction;
        this.comparativeFitnessFunction = this::compareFitness;
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
        this.comparativeFitnessFunction = this::compareFitness;

        survivors = new ArrayList<>(survivorsSize);
        survivors.add(progenitor);
        fittestOrganism = progenitor;
        lowliestMutant = progenitor;
        highestFitness = fitnessFunction.applyAsDouble(progenitor);
        lowestFitness = fitnessFunction.applyAsDouble(progenitor);
        seedInitialPopulation(progenitor, fitnessFunction);
    }

    private void seedInitialPopulation(OrganismInterface progenitor,
                                       ToDoubleFunction<OrganismInterface> fitnessFunction) {
        List<OrganismInterface> seededPopulation = new ArrayList<>(generationSize);
        OrganismInterface mutant = progenitor.spawnMutant();
        seededPopulation.add(mutant);
        for (int i = 0; i < generationSize - 2; i++) {
            Collections.shuffle(mutant.getGenome());
            mutant = mutant.spawnMutant();
            seededPopulation.add(mutant);
        }
        seededPopulation.sort(comparativeFitnessFunction);
        survivors.addAll(seededPopulation.subList(0, survivorsSize));
        fittestOrganism = survivors.get(0);
        highestFitness = fitnessFunction.applyAsDouble(fittestOrganism);
        lowliestMutant = survivors.get(survivorsSize - 1);
        lowestFitness = fitnessFunction.applyAsDouble(lowliestMutant);

    }

    public List<OrganismInterface> getPopulation() {
        return survivors;
    }

    public OrganismInterface getFittestOrganism() {
        return fittestOrganism;
    }

    public void advanceGeneration() {

        //step 1: survival of the fittest
        List<OrganismInterface> newGeneration = new LinkedList<>(survivors);
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
                    System.out.println("generation size reached");
                    break crossbreeding;
                }
                OrganismInterface crossBreed;
                if (i != j) {
                    crossBreed = survivors.get(i).crossBreed(survivors.get(j));
                } else if (misfits.size() > 0) {
                    crossBreed = survivors.get(i).crossBreed(misfits.get(random.nextInt(misfits.size())));
                } else {
                    crossBreed = survivors.get(i).crossBreed(newGeneration.get(random.nextInt(newGeneration.size())));
                }
                if (fitnessFunction.applyAsDouble(crossBreed) > lowestFitness) {
                    newGeneration.add(crossBreed);
                } else {
                    misfits.add(crossBreed);
                }
            }
        }

//        System.out.println("new generation size: " + newGeneration.size());
//        System.out.println("the number of misfits is: " + misfits.size());
        newGeneration.sort(comparativeFitnessFunction);
        fittestOrganism = newGeneration.get(0);
        highestFitness = fitnessFunction.applyAsDouble(fittestOrganism);
        lowliestMutant = newGeneration.get(survivorsSize - 1);
        lowestFitness = fitnessFunction.applyAsDouble(lowliestMutant);
        survivors.clear();
        survivors.addAll(newGeneration.subList(0, survivorsSize));
    }
}
