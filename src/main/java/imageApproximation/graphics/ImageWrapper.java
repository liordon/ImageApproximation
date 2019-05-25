package imageApproximation.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWrapper {
    private final Color[][] pixelArray;

    public ImageWrapper(Color[][] pixelArray) {
        this.pixelArray = pixelArray;
    }

    public static ImageWrapper deepCopy(ImageWrapper other) {
        final Color[][] image = new Color[other.getWidth()][other.getHeight()];
        for (int i = 0; i < other.getHeight(); i++) {
            for (int j = 0; j < other.getHeight(); j++) {
                if (other.pixelArray[i][j] != null && other.pixelArray[i][j] != Color.BLACK) {
                    image[i][j] = other.pixelArray[i][j];
                }
            }
        }
        return new ImageWrapper(image);
    }

    public ImageWrapper(BufferedImage image) {
        this.pixelArray = new Color[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final int rgb = image.getRGB(i, j);
                if ( rgb != Color.BLACK.getRGB()) {
                    this.pixelArray[i][j] = new Color(rgb);
                }
            }
        }
    }

    public ImageWrapper(int width, int height) {
        pixelArray = new Color[width][height];
    }

    public void setColor(int x, int y, Color color) {
        pixelArray[x][y] = color;
    }

    public int getWidth() {
        return pixelArray.length;
    }

    public int getHeight() {
        return pixelArray[0].length;
    }

    public Color getColorAtPixel(int x, int y) {
        Color result = pixelArray[x][y];
        return result == null ? Color.BLACK : result;
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
            if (this.getWidth() != castedOther.getWidth()
                    || this.getHeight() != castedOther.getHeight()) {
                return false;
            }
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (!this.getColorAtPixel(i, j).equals(castedOther.getColorAtPixel(i, j))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage result = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                result.setRGB(i, j, getColorAtPixel(i, j).getRGB());
            }
        }
        return result;
    }
}
