package imageApproximation.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageWrapper {
//    private final BufferedImage image;
    private final Color[][] image;

    public ImageWrapper(Color[][] image) {
        this.image = image;
    }

    public static ImageWrapper deepCopy(ImageWrapper other) {
//        ColorModel cm = other.image.getColorModel();
//        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//        WritableRaster raster = other.image.copyData(null);
//        return new ImageWrapper(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
        return new ImageWrapper(other.image);
    }

    public ImageWrapper(BufferedImage image) {
        this.image = new Color[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                this.image[i][j] = new Color(image.getRGB(i,j));
            }
        }
    }

    public ImageWrapper(int width, int height) {
//        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image = new Color[width][height];
    }

    public void setRGB(int x, int y, Color color) {
//        image.setRGB(x, y, color.getRGB());
        image[x][y] = color;
    }

    public int getWidth() {
//        return image.getWidth();
        return image.length;
    }

    public int getHeight() {
//        return image.getHeight();
        return image[0].length;
    }

    public Color getColor(int x, int y) {
//        return new Color(image.getRGB(x, y));
        Color result = image[x][y];
        return result == null ? Color.BLACK : result;
    }

//    /**
//     * turns out BufferedImage does not compare itself pixel by pixel so I had to make my own equality method
//     *
//     * @param other another object, preferably another ImageWrapper
//     * @return true iff other is an ImageWrapper with the same pixel content
//     */
//    @Override
//    public boolean equals(Object other) {
//        if (other.getClass() != this.getClass()) {
//            return false;
//        } else {
//            ImageWrapper castedOther = (ImageWrapper) other;
//            if (this.image.getWidth() != castedOther.image.getWidth()
//                    || this.image.getHeight() != castedOther.image.getHeight())
//                return false;
//            for (int i = 0; i < image.getWidth(); i++) {
//                for (int j = 0; j < image.getHeight(); j++) {
//                    if (this.image.getRGB(i, j) != castedOther.image.getRGB(i, j))
//                        return false;
//                }
//            }
//            return true;
//        }
//    }

    public BufferedImage getInnerImage() {
        BufferedImage result = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                result.setRGB(i,j,getColor(i,j).getRGB());
            }
        }
        return result;
    }
}
