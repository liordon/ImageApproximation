package imageApproximation.ApproximationAlgorithms;

import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.organisms.OrganismInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class GeneticAlgorithm extends AnytimeAlgorithm {

    private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithm.class);

    private final int generationSize;
    private final int survivorsSize;
    private final int diversityFactor = 5;
    private final ToDoubleFunction<List<BasicShape>> fitnessFunction;
    private final Random random = new Random();
    private List<OrganismInterface> survivors;
    private OrganismInterface fittestOrganism, lowliestMutant;
    private double highestFitness, lowestFitness;


    private double calculateFitness(OrganismInterface o) {
        return fitnessFunction.applyAsDouble(o.getGenome());
    }

    private int compareFitness(OrganismInterface a, OrganismInterface b) {
        return -(int) Math.signum(calculateFitness(a) - calculateFitness(b));
    }

    GeneticAlgorithm(List<OrganismInterface> initialPopulation, int generationSize,
                     ToDoubleFunction<List<BasicShape>> fitnessFunction) {
        survivors = new ArrayList<>(initialPopulation);
        this.generationSize = generationSize;
        survivorsSize = initialPopulation.size();
        this.fitnessFunction = fitnessFunction;
        survivors.sort(this::compareFitness);
        fittestOrganism = survivors.get(0);
        lowliestMutant = survivors.get(survivorsSize - 1);
        highestFitness = calculateFitness(fittestOrganism);
        lowestFitness = calculateFitness(lowliestMutant);
    }

    public GeneticAlgorithm(int generationSize, int survivalSize, OrganismInterface progenitor,
                            ToDoubleFunction<List<BasicShape>> fitnessFunction) {
        this.generationSize = generationSize;
        this.survivorsSize = survivalSize;
        this.fitnessFunction = fitnessFunction;
        survivors = new ArrayList<>(survivorsSize);
        survivors.add(progenitor);
        fittestOrganism = progenitor;
        lowliestMutant = progenitor;
        highestFitness = calculateFitness(progenitor);
        lowestFitness = calculateFitness(progenitor);
        seedInitialPopulation(progenitor);
    }

    private void seedInitialPopulation(OrganismInterface progenitor) {
        List<OrganismInterface> seededPopulation = new ArrayList<>(generationSize);
        OrganismInterface mutant = progenitor.spawnMutant();
        seededPopulation.add(mutant);
        for (int i = 0; i < generationSize - 2; i++) {
            Collections.shuffle(mutant.getGenome());
            mutant = mutant.spawnMutant();
            seededPopulation.add(mutant);
        }
        seededPopulation.sort(this::compareFitness);
        survivors.addAll(seededPopulation.subList(0, survivorsSize));
        fittestOrganism = survivors.get(0);
        highestFitness = calculateFitness(fittestOrganism);
        lowliestMutant = survivors.get(survivorsSize - 1);
        lowestFitness = calculateFitness(lowliestMutant);

    }

    List<OrganismInterface> getPopulation() {
        return survivors;
    }

    OrganismInterface getFittestOrganism() {
        return fittestOrganism;
    }

    void advanceGeneration() {

        //step 1: survival of the fittest
        List<OrganismInterface> newGeneration = new LinkedList<>(survivors);
        List<OrganismInterface> misfits = new LinkedList<>();
        for (OrganismInterface survivor : survivors) {
            OrganismInterface mutant = survivor.spawnMutant();
            if (calculateFitness(mutant) > lowestFitness) {
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
                    LOGGER.debug("generation size reached");
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
                if (calculateFitness(crossBreed) > lowestFitness / 2) {
                    newGeneration.add(crossBreed);
                } else {
                    misfits.add(crossBreed);
                }
            }
        }

        // step 3: downBreeding for variance
        for (OrganismInterface crossBreed : misfits) {
            crossBreed.crossBreed(lowliestMutant, newGeneration.get(random.nextInt(survivorsSize)));
            if (calculateFitness(crossBreed) > lowestFitness / 2) {
                newGeneration.add(crossBreed);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("highest fitness: " + highestFitness + " lowest fitnes: " + lowestFitness);
            LOGGER.debug("new generation size: " + newGeneration.size());
            LOGGER.debug("the number of misfits is: " + misfits.size());
        }
        newGeneration.sort(this::compareFitness);
        fittestOrganism = newGeneration.get(0);
        highestFitness = calculateFitness(fittestOrganism);
        lowliestMutant = newGeneration.get(survivorsSize - 1);
        lowestFitness = calculateFitness(lowliestMutant);
        survivors.clear();
        ensureDiversity(newGeneration, misfits);
    }

    private void ensureDiversity(List<OrganismInterface> newGeneration, List<OrganismInterface> misfits) {
        int minPossibleDiversityIndex =
                Math.min(((survivorsSize * (diversityFactor + 1)) / diversityFactor), misfits.size());

        survivors.addAll(newGeneration.subList(0, survivorsSize - minPossibleDiversityIndex));
        survivors.addAll(misfits.subList(0, minPossibleDiversityIndex));
//        survivors.addAll(newGeneration.subList(0, survivorsSize));
    }

    @Override
    public void iterateOnce() {
        advanceGeneration();
    }

    @Override
    public List<BasicShape> getBestResultSoFar() {
        return fittestOrganism.getGenome();
    }

    @Override
    public double getBestScoreSoFar() {
        return highestFitness;
    }
}
