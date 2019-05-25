package imageApproximation;

import imageApproximation.ApproximationAlgorithms.GeneticAlgorithm;
import imageApproximation.errorCalculators.ApproximationScoreCalculator;
import imageApproximation.errorCalculators.MeanSquareErrorScoreCalculator;
import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;
import imageApproximation.graphics.shapes.ShapeDrawer;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class ApproximationRunner {
    private static final Logger LOGGER = LogManager.getLogger(ApproximationRunner.class);

    private static final int sparsity = 20;

    public static void main2(String[] args) {
        ImageWrapper image = new ImageWrapper(1,1);
    }

    public static void main(String[] args) {

        try {
            LOGGER.info("reading image");
            ImageWrapper targetImage =
                    new ImageWrapper(ImageIO.read(new File("src/main/resources/golden_bell-200x200.jpg")));
            LOGGER.info("defining fitness and boundaries");

            ToDoubleFunction<List<BasicShape>> scoreCalculator =
                    new ApproximationScoreCalculator(new MeanSquareErrorScoreCalculator(sparsity), targetImage,
                            sparsity);

            ShapeBoundaries boundaries = new ShapeBoundaries(200 + 2, 200 + 2, 200 * 2);
            long startEvolutionTime = System.currentTimeMillis();
            OrganismInterface progenitor = new CircleOrganism(boundaries, ExerciseConstants.MAX_ALLOWED_SHAPES/2);

            LOGGER.info("creating initial population");
            GeneticAlgorithm algorithm = new GeneticAlgorithm(1000, 50, progenitor, scoreCalculator);
            LOGGER.info("starting evolution process");

            List<BasicShape> bestResult = algorithm.runSetTime(Duration.ofMinutes(5));
            LOGGER.info(
                    "evolution finished! and it only took " +
                            (System.currentTimeMillis() - startEvolutionTime) / 60_000 + " minutes!");

            ImageIO.write(ShapeDrawer.drawMany(bestResult, new ImageWrapper(300, 225)).toBufferedImage(), "jpg",
                    new File("out.jpg"));
            System.out.println("best result got a score of " + scoreCalculator.applyAsDouble(bestResult) + " and contained " +
                    bestResult.size() + " shapes.");
        } catch (IOException e) {
            LOGGER.fatal(e);
        }
    }
}
