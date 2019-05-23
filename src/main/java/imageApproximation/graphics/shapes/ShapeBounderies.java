package imageApproximation.graphics.shapes;

public class ShapeBounderies {
    private final int maxWidth;
    private final int maxHeight;
    private final int maxSize;

    public ShapeBounderies(int maxWidth, int maxHeight, int maxSize) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxSize = maxSize;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
