package imageApproximation;

import imageApproximation.ApproximationAlgorithms.GeneticAlgorithm;
import imageApproximation.errorCalculators.ApproximationScoreCalculator;
import imageApproximation.errorCalculators.MeanSquareErrorCalculator;
import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.ShapeBoundaries;
import imageApproximation.graphics.shapes.ShapeDrawer;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.function.ToDoubleFunction;

public class ApproximatorRunner {

    private static final int sparsity = 15;

    public static void main2(String[] args) {
        ImageWrapper image = new ImageWrapper(1,1);
    }

    public static void main(String[] args) {

        try {
            System.out.println("reading image");
            ImageWrapper targetImage = new ImageWrapper(ImageIO.read(new File("src/main/resources/golden_bell-300x225.jpg")));
            System.out.println("defining fitness and boundries");
            MeanSquareErrorCalculator meanSquareErrorCalculator = new MeanSquareErrorCalculator(sparsity);
            ApproximationScoreCalculator scoreCalculator =
                    new ApproximationScoreCalculator(meanSquareErrorCalculator, targetImage, sparsity);
            ToDoubleFunction<OrganismInterface> fitnessFunction =
                    (o) -> -scoreCalculator.applyAsDouble(o.getGenome());


            ShapeBoundaries boundaries = new ShapeBoundaries(300 + 2, 225 + 2, 300 * 2);
            long startEvolutionTime = System.currentTimeMillis();
            OrganismInterface progenitor = new CircleOrganism(boundaries);
            for (int i = 0; i < ExerciseConstants.MAX_ALLOWED_SHAPES; i++) {
                progenitor = progenitor.spawnMutant();
            }

            System.out.println("creating initial population");
            GeneticAlgorithm algorithm = new GeneticAlgorithm(100, 10, progenitor, fitnessFunction);

            System.out.println("starting evolution process");
            OrganismInterface fittestSpecimen = algorithm.getFittestOrganism();
            for (int i = 0; i < 200; i++) {
                long epochStartTime = System.currentTimeMillis();
                algorithm.advanceGeneration();
                fittestSpecimen = algorithm.getFittestOrganism();
                if (i % 100 == 0) {
                    System.out.println("generation " + i + " took " +
                            (System.currentTimeMillis() - epochStartTime) / 1000 + " seconds.\nfittest score: " +
                            fitnessFunction.applyAsDouble(fittestSpecimen));
                }
            }
            System.out.println(
                    "evolution finished! ant it only took " +
                            (System.currentTimeMillis() - startEvolutionTime) / 60_000 + " minutes!");

            ImageIO.write(ShapeDrawer.drawMany(fittestSpecimen.getGenome(), new ImageWrapper(300, 225)).getInnerImage(), "jpg", new File("out.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
