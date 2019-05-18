package imageApproximation.graphics.shapes;

import static org.junit.jupiter.api.Assertions.*;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;


class BasicCircleTest {

    @Test
    void whenRadiusIs0BoundingRectangleIsEqualToShapeLocation() {
        Pair<Point, Point> bound =
                new Pair<>(
                        new Point(42, 42),
                        new Point(42, 42)
                );
        assertEquals(bound, opaqueCircleOfWhateverColor(0, 42).getBoundingRectangle());
    }

    private static BasicCircle opaqueCircleOfWhateverColor(int radius, int location) {
        return new BasicCircle(radius, Color.GREEN, 1, location, location);
    }

    @Test
    void boundingRectangleIsAlwaysFromMinusRToR() {
        for (int size = 1; size < 5; size++) {
            Pair<Point, Point> bound =
                    new Pair<>(
                            new Point(-size, -size),
                            new Point(size, size)
                    );
            assertEquals(bound, opaqueCircleOfWhateverColor(size, 0).getBoundingRectangle());
        }
    }

    @Test
    void scale0CircleIsASinglePixel() {
        BasicCircle inspected = opaqueCircleOfWhateverColor(0, 0);
        ArrayList<Point> expected = new ArrayList<>(1);
        expected.add(new Point(0, 0));
        assertEquals(expected, inspected.getAffectedPixels());
    }

    @Test
    void scale1CircleIsACross() {
        BasicCircle inspected = opaqueCircleOfWhateverColor(1, 1);
        ArrayList<Point> expected = new ArrayList<>(5);
        expected.add(new Point(0, 1));
        expected.add(new Point(1, 0));
        expected.add(new Point(1, 1));
        expected.add(new Point(2, 1));
        expected.add(new Point(1, 2));
        assertListContent(expected, inspected.getAffectedPixels());
    }

    @Test
    void scale2CircleIsADiamond() {
        BasicCircle inspected = opaqueCircleOfWhateverColor(2, 2);
        ArrayList<Point> expected = new ArrayList<>(5);
        expected.add(new Point(0, 2));

        expected.add(new Point(1, 1));
        expected.add(new Point(1, 2));
        expected.add(new Point(1, 3));

        expected.add(new Point(2, 0));
        expected.add(new Point(2, 1));
        expected.add(new Point(2, 2));
        expected.add(new Point(2, 3));
        expected.add(new Point(2, 4));

        expected.add(new Point(3, 1));
        expected.add(new Point(3, 2));
        expected.add(new Point(3, 3));

        expected.add(new Point(4, 2));
        assertListContent(expected, inspected.getAffectedPixels());
    }

    private static void assertListContent(java.util.List expected, java.util.List given) {
        assertEquals(expected.size(), given.size());
        for (Object o : expected) {
            assertTrue(given.contains(o));
        }
    }
}