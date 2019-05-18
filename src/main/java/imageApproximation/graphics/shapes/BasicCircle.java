package imageApproximation.graphics.shapes;

import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BasicCircle extends BasicShape {

    private BasicCircle(double rotation, double scale, Color color, double opacity, int x, int y) {
        super(rotation, scale, color, opacity, x, y);
    }

    public BasicCircle(double radius, Color color, double opacity, int x, int y) {
        this(0, radius, color, opacity, x, y);
    }

    @Override
    public BasicShapesTypeEnum getShapesType() {
        return BasicShapesTypeEnum.CIRCLE;
    }

    @Override
    public Pair<Point, Point> getBoundingRectangle() {
        if (getScale() == 0) {
            return new Pair<>(new Point(getX(), getY()), new Point(getX(), getY()));
        } else {
            return new Pair<>(
                    new Point((int) Math.round(getX() - getScale()), (int) Math.round(getY() - getScale())),
                    new Point((int) Math.round(getX() + getScale()), (int) Math.round(getY() + getScale()))
            );
        }
    }

    @Override
    public List<Point> getAffectedPixels() {
        int approxAffectedPixels = (int) Math.round(2 * Math.PI * getScale());
        List<Point> result = new ArrayList<>(approxAffectedPixels);
        Pair<Point, Point> bound = getBoundingRectangle();
        for (int i = bound.getKey().x; i <= bound.getValue().x; i++) {
            for (int j = bound.getKey().y; j <= bound.getValue().y; j++) {
                if (this.contains(i, j)) {
                    result.add(new Point(i, j));
                }
            }
        }
        return result;
    }

    @Override
    public boolean contains(int x, int y) {
        int xDistance = x-getX();
        int yDistance = y-getY();
        double euclidianDistance = Math.sqrt(xDistance*xDistance + yDistance * yDistance);

        return ((euclidianDistance <=  getScale()
                ));
    }
}
