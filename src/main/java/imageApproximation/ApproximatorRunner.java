package imageApproximation;

import imageApproximation.ApproximationAlgorithms.GeneticAlgorithm;
import imageApproximation.errorCalculators.MeanSquareErrorCalculator;
import imageApproximation.graphics.ImageWrapper;
import imageApproximation.graphics.shapes.ShapeBounderies;
import imageApproximation.graphics.shapes.ShapeDrawer;
import imageApproximation.organisms.CircleOrganism;
import imageApproximation.organisms.OrganismInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.function.ToDoubleFunction;

public class ApproximatorRunner {

    public static void main2(String[] args) {
        ImageWrapper image = new ImageWrapper(1,1);
    }

    public static void main(String[] args) {

        try {
            System.out.println("reading image");
            ImageWrapper targetImage = new ImageWrapper(ImageIO.read(new File("src/main/resources/golden_bell-300x225.jpg")));
            System.out.println("defining fitness and boundries");
            ToDoubleFunction<OrganismInterface> fitnessFunction = (o) -> {
                ImageWrapper state = ShapeDrawer.drawMany(o.getGenome(), new ImageWrapper(300, 225));
                return -new MeanSquareErrorCalculator().applyAsDouble(state, targetImage);
            };

            ShapeBounderies bounderies = new ShapeBounderies(300 * 2, 225 * 2, 300 * 2);
            OrganismInterface progenitor = new CircleOrganism(bounderies);

            System.out.println("creating initial population");
            GeneticAlgorithm algorithm = new GeneticAlgorithm(100, 0.1, progenitor, fitnessFunction);

            System.out.println("starting evolution process");
            OrganismInterface fittestSpecimen = algorithm.getFittestOrganism();
            for (int i = 0; i < 10; i++) {
                algorithm.advanceGeneration();
                fittestSpecimen = algorithm.getFittestOrganism();
                System.out.println("generation " + i + " fittest score: " + fitnessFunction.applyAsDouble(fittestSpecimen));
            }
            System.out.println("evolution finished");

            ImageIO.write(ShapeDrawer.drawMany(fittestSpecimen.getGenome(), new ImageWrapper(300, 225)).getInnerImage(), "jpg", new File("out.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
