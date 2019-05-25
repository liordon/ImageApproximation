package imageApproximation.ApproximationAlgorithms;

import imageApproximation.graphics.shapes.BasicShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Observable;

public abstract class AnytimeAlgorithm extends Observable {
    private static final Logger LOGGER = LogManager.getLogger(AnytimeAlgorithm.class);
    private int iterationsSoFar;

    public abstract void iterateOnce();

    public List<BasicShape> runSetTime(Duration duration) {
        iterationsSoFar = 0;
        long avarageIterationTime = 0;
        long runStartTime = System.currentTimeMillis();
        while (true) {
            long iterationStart = System.currentTimeMillis();
            iterateOnce();
            long iterationTime = System.currentTimeMillis() - iterationStart;
            avarageIterationTime = (avarageIterationTime * (iterationsSoFar++) + iterationTime) / iterationsSoFar;
            final long runTimeSoFar = System.currentTimeMillis() - runStartTime;
            LOGGER.debug("Iteration " + iterationsSoFar + " took " +
                    iterationTime + " milliseconds, compared to avarage " + avarageIterationTime +
                    " millis. Total of " +
                    runTimeSoFar / 1000 +
                    " seconds so far. Best score so far: " +
                    getBestScoreSoFar());
            if (System.currentTimeMillis() + avarageIterationTime > runStartTime + duration.toMillis()) {
                LOGGER.info("Only " + (duration.toMillis() - runTimeSoFar) +
                        " millis left. Stopping iterations");
                return getBestResultSoFar();
            }
        }
    }

    public abstract List<BasicShape> getBestResultSoFar();

    public abstract double getBestScoreSoFar();

    public int getNumberOfIterationsSoFar() {
        return iterationsSoFar;
    }
}
