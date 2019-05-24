package imageApproximation.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWrapper {
//    private final BufferedImage image;
    private final Color[][] image;

    public ImageWrapper(Color[][] image) {
        this.image = image;
    }

    public static ImageWrapper deepCopy(ImageWrapper other) {
        final Color[][] image = new Color[other.getWidth()][other.getHeight()];
        for (int i = 0; i < other.getHeight(); i++) {
            for (int j = 0; j < other.getHeight(); j++) {
                if (other.image[i][j] != null && other.image[i][j] != Color.BLACK) {
                    image[i][j] = other.image[i][j];
                }
            }
        }
        return new ImageWrapper(image);
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
        image = new Color[width][height];
    }

    public void setRGB(int x, int y, Color color) {
        image[x][y] = color;
    }

    public int getWidth() {
        return image.length;
    }

    public int getHeight() {
        return image[0].length;
    }

    public Color getColor(int x, int y) {
        Color result = image[x][y];
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
                    if (!this.getColor(i, j).equals(castedOther.getColor(i, j))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

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
