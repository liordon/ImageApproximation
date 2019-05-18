package imageApproximation.graphics.shapes;

import javafx.util.Pair;

import java.awt.*;
import java.util.List;

public abstract class BasicShape {
    private final double rotation;
    private final double scale;
    private final Color color;
    private final double opacity;
    private final int x;
    private final int y;


    public BasicShape(double rotation, double scale, Color color, double opacity, int x, int y) {
        this.rotation = rotation;
        this.scale = scale;
        this.color = color;
        this.opacity = opacity;
        this.x = x;
        this.y = y;
    }

    public abstract BasicShapesTypeEnum getShapesType();

    public double getRotation() {
        return rotation;
    }

    public double getScale() {
        return scale;
    }

    public Color getColor() {
        return color;
    }

    public double getOpacity() {
        return opacity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract Pair<Point, Point> getBoundingRectangle();

    public abstract List<Point> getAffectedPixels();

    public abstract boolean contains(int x, int y);
}
