package imageApproximation.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageWrapper {
    private final BufferedImage image;

    public static ImageWrapper deepCopy(ImageWrapper other) {
        ColorModel cm = other.image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = other.image.copyData(null);
        return new ImageWrapper(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
    }

    public ImageWrapper(BufferedImage image) {
        this.image = image;
    }

    public ImageWrapper(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void setRGB(int x, int y, Color color) {
        image.setRGB(x,y,color.getRGB());
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Color getColor(int x, int y) {
        return new Color(image.getRGB(x,y));
    }
}
