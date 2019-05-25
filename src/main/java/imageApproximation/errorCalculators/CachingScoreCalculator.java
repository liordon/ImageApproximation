package imageApproximation.errorCalculators;

import imageApproximation.graphics.shapes.BasicShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

public class CachingScoreCalculator implements ToDoubleFunction<List<BasicShape>> {

    private static final int MAXIMUM_CACHE_SIZE = 10_000;
    private final ToDoubleFunction<List<BasicShape>> errorCalculator;
    private final Map<List<BasicShape>, Double> scoreCache = new HashMap<>();

    public CachingScoreCalculator(ToDoubleFunction<List<BasicShape>> errorCalculator) {
        this.errorCalculator = errorCalculator;
    }

    @Override
    public double applyAsDouble(List<BasicShape> value) {
        if (scoreCache.keySet().size() > MAXIMUM_CACHE_SIZE){
            clearCache();
        }
        if (!scoreCache.containsKey(value)) {
            scoreCache.put(value, errorCalculator.applyAsDouble(value));
        }
        return scoreCache.get(value);
    }

    public void clearCache() {
        scoreCache.clear();
    }
}
