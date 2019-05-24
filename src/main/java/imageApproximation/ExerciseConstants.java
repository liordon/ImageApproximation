package imageApproximation;

import java.time.Duration;

/**
 * just a place to put all exercise-wide definitions. it's an enum only to keep
 * it as a singleton implementation.
 */
public enum ExerciseConstants {
    ;
    public static final int MAX_ALLOWED_SHAPES = 100;
    public static final int MAX_COLOR_VALUE = 255;
    public static final int MAX_IMAGE_SIZE = 400;
    public static final Duration MAXIMUM_RUN_TIME = Duration.ofMinutes(5);
}
