package imageApproximation.steppers.shapes;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicCircle extends BasicShape {
    public BasicCircle(double rotation, double scale, Color color, double opacity) {
        super(rotation, scale, color, opacity);
    }

    @Override
    public BasicShapesTypeEnum getShapesType() {
        return BasicShapesTypeEnum.CIRCLE;
    }

    @Override
    public BufferedImage draw(BufferedImage source) {
        return null;
    }
}
