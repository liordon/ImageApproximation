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
        image.setRGB(x, y, color.getRGB());
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Color getColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    /**
     * turns out BufferedImage does not compare itself pixel by pixel so I had to make my own equality method
     *
     * @param other another object, preferably another ImageWrapper
     * @return true iff other is an ImageWrapper with the same pixel content
     */
    @Override
    public boolean equals(Object other) {
        if (other.getClass() != this.getClass()) {
            return false;
        } else {
            ImageWrapper castedOther = (ImageWrapper) other;
            if (this.image.getWidth() != castedOther.image.getWidth()
                    || this.image.getHeight() != castedOther.image.getHeight())
                return false;
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (this.image.getRGB(i, j) != castedOther.image.getRGB(i, j))
                        return false;
                }
            }
            return true;
        }
    }
}
