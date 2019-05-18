package imageApproximation.graphics.shapes;

import imageApproximation.graphics.ImageWrapper;

public class ShapeDrawer {
    public static ImageWrapper draw(BasicShape shape, ImageWrapper canvas) {
        ImageWrapper result = ImageWrapper.deepCopy(canvas);
        result.setRGB(shape.getX(), shape.getY(), shape.getColor());
        return result;
    }
}
