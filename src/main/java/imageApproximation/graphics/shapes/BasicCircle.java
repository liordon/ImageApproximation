package imageApproximation.graphics.shapes;

import javafx.util.Pair;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BasicCircle extends BasicShape {
    private Pair<Point, Point> boundingRectangle;

    private BasicCircle(double rotation, double scale, Color color, double opacity, int x, int y) {
        super(rotation, scale, color, opacity, x, y);
    }

    public BasicCircle(double radius, Color color, double opacity, int x, int y) {
        this(0, radius, color, opacity, x, y);
    }

    @Override
    public Pair<Point, Point> getBoundingRectangle() {
        if (boundingRectangle == null) {
            if (getScale() == 0) {
                boundingRectangle = new Pair<>(new Point(getX(), getY()), new Point(getX(), getY()));
            } else {
                boundingRectangle = new Pair<>(
                        new Point((int) Math.round(getX() - getScale()), (int) Math.round(getY() - getScale())),
                        new Point((int) Math.round(getX() + getScale()), (int) Math.round(getY() + getScale()))
                );
            }
        }
        return boundingRectangle;
    }

    @Override
    public List<Point> getAffectedPixels() {
        List<Point> affectedPixels = new LinkedList<>();
            Pair<Point, Point> bound = getBoundingRectangle();
            for (int i = bound.getKey().x; i <= bound.getValue().x; i++) {
                for (int j = bound.getKey().y; j <= bound.getValue().y; j++) {
                    if (this.contains(i, j)) {
                        affectedPixels.add(new Point(i, j));
                    }
                }
            }
        return affectedPixels;
    }

    @Override
    public boolean contains(int x, int y) {
        int xDistance = x - getX();
        int yDistance = y - getY();
        double euclideanDistance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        return ((euclideanDistance <= getScale()
        ));
    }
}
