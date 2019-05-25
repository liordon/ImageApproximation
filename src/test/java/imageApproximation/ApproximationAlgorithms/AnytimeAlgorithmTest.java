package imageApproximation.ApproximationAlgorithms;

import imageApproximation.graphics.shapes.BasicShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;


class AnytimeAlgorithmTest {
    private int numberOfIterations;

    @BeforeEach
    void setup() {
        numberOfIterations=0;
    }

    private final AnytimeAlgorithm inspected = new AnytimeAlgorithm() {
        @Override
        public void iterateOnce() {
            try {
                Thread.sleep(10);
                numberOfIterations++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public List<BasicShape> getBestResultSoFar() {
            return null;
        }

        @Override
        public double getBestScoreSoFar() {
            return 0;
        }
    };

    @Test
    void alwaysStopsShortOfTimeout() {
        final Duration duration = Duration.ofMillis(100);
        assertTimeout(duration, () -> inspected.runSetTime(duration));
    }

    @Test
    void iteratesAtLeastOnce(){
        inspected.runSetTime(Duration.ofMillis(0));
        assertEquals(1, numberOfIterations);
    }
}