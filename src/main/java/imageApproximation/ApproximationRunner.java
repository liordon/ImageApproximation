package imageApproximation;

import imageApproximation.ApproximationAlgorithms.AnytimeAlgorithm;
import imageApproximation.ApproximationAlgorithms.GeneticAlgorithm;
import imageApproximation.dmonstrationUI.DemonstrationFrame;
import imageApproximation.errorCalculators.CachingScoreCalculator;
import imageApproximation.errorCalculators.MeanSquareErrorCalculator;
import imageApproximation.errorCalculators.ShapeListScoreCalculator;
import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.BasicShape;
import imageApproximation.graphics.shapes.ShapeBoundaries;
import imageApproximation.graphics.shapes.ShapeDrawer;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.ToDoubleFunction;

public class ApproximationRunner {
    private static final Logger LOGGER = LogManager.getLogger(ApproximationRunner.class);

    private static final int SPARSITY = 20;
    private static final int REFRESH_RATE = 500;
    private static final int MILLIS_IN_MINUTE = 60_000;
    private static final int GENERATION_SIZE = 1000;
    private static final int SURVIVAL_SIZE = 50;
    private static final int STARTING_NUMBER_OF_GENES = 0; //ExerciseConstants.MAX_ALLOWED_SHAPES / 2;
    private static DemonstrationFrame demonstrationFrame;
    private static int imageWidth;
    private static int imageHeight;
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        try {
            LOGGER.info("reading image");
            final String pathToTargetImage = "src/main/resources/golden_bell-200x200.jpg";
//            final String pathToTargetImage = "src/main/resources/purity-400x400.jpg";
            final BufferedImage imageReading = ImageIO.read(new File(pathToTargetImage));
            demonstrationFrame = new DemonstrationFrame(imageReading);
            imageWidth = imageReading.getWidth();
            imageHeight = imageReading.getHeight();
            demonstrationFrame.updateApproximatedImage(new ImageWrapper(imageWidth, imageHeight).toBufferedImage());
            demonstrationFrame.pack();
            demonstrationFrame.setVisible(true);
            Thread.sleep(5000);
            ImageWrapper targetImage = new ImageWrapper(imageReading);

            LOGGER.info("defining fitness and boundaries");
            ToDoubleFunction<List<BasicShape>> scoreCalculator =
                    new CachingScoreCalculator(
                            new ShapeListScoreCalculator(new MeanSquareErrorCalculator(SPARSITY), targetImage,
                                    SPARSITY));

            ShapeBoundaries boundaries =
                    new ShapeBoundaries(imageWidth + 2, imageHeight + 2, Math.max(imageWidth, imageHeight) * 2);
            long startEvolutionTime = System.currentTimeMillis();
            OrganismInterface progenitor = new CircleOrganism(boundaries, STARTING_NUMBER_OF_GENES);

            LOGGER.info("creating initial population");
            GeneticAlgorithm algorithm = new GeneticAlgorithm(GENERATION_SIZE,
                    SURVIVAL_SIZE, progenitor, scoreCalculator);
            updateUIApproximatedShape(algorithm);

            LOGGER.info("starting evolution process");

            Future<List<BasicShape>> finalResult = runAlgorithmForSetTime(algorithm, Duration.ofMinutes(5));

            while (!finalResult.isDone()) {
                try {
                    Thread.sleep(REFRESH_RATE);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
                updateUIApproximatedShape(algorithm);
            }

            LOGGER.info(
                    "evolution finished! and it only took " +
                            (System.currentTimeMillis() - startEvolutionTime) / MILLIS_IN_MINUTE + " minutes!");

            ImageIO.write(
                    ShapeDrawer.drawManyShapes(algorithm.getBestResultSoFar(), imageWidth, imageHeight).toBufferedImage(),
                    "jpg",
                    new File("out.jpg"));
            System.out.println("best result got a score of " + scoreCalculator.applyAsDouble(
                    algorithm.getBestResultSoFar()) + " and contained " +
                    algorithm.getBestResultSoFar().size() + " shapes.");
        } catch (Exception e) {
            LOGGER.catching(e);
            demonstrationFrame.setVisible(false);
        }
    }

    private static Future<List<BasicShape>> runAlgorithmForSetTime(GeneticAlgorithm algorithm, Duration duration) {
        return executorService.submit(() -> algorithm.runSetTime(duration));
    }

    private static void updateUIApproximatedShape(AnytimeAlgorithm algorithm) {
        executorService.execute(() -> {
                    demonstrationFrame
                            .updateApproximatedImage(
                                    ShapeDrawer.drawManyShapes(algorithm.getBestResultSoFar(), imageWidth, imageHeight)
                                            .toBufferedImage());
                    demonstrationFrame.updateProgress(algorithm.getNumberOfIterationsSoFar(),
                            algorithm.getBestScoreSoFar());
                }
        );
    }

}
