package imageApproximation.steppers.shapes;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BasicShape {
    private final double rotation;
    private final double scale;
    private final Color color;
    private final double opacity;


    public BasicShape(double rotation, double scale, Color color, double opacity) {
        this.rotation = rotation;
        this.scale = scale;
        this.color = color;
        this.opacity = opacity;
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

    public abstract BufferedImage draw(BufferedImage source);
}
