package imageApproximation.steppers.shapes;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ShapeDrawer {
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage draw(BasicShape shape, BufferedImage canvas) {
        BufferedImage result = deepCopy(canvas);
        result.setRGB(shape.getX(), shape.getY(), shape.getColor().getRGB());
        return result;
    }
}
