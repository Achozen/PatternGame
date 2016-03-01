package achozen.rememberme.engine;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by Achozen on 2016-02-27.
 */
public class PointConnectionTest {

    @Test
    public void shouldNotValidateSamePoints() {

        Point point1 = new Point(3, 2);
        Point point2 = new Point(3, 2);
        PointConnections pointConnection = new PointConnections(point1, point2);
        assertFalse(pointConnection.validate());

    }

    @Test
    public void shouldValidateNearPoints() {

        Point point1 = new Point(3, 2);
        Point point2 = new Point(3, 3);

        PointConnections pointConnection = new PointConnections(point1, point2);
        assertTrue(pointConnection.validate());

    }

    @Test
    public void shouldNotValidateFarVerticalPoints() {

        Point point1 = new Point(3, 0);
        Point point2 = new Point(3, 2);

        PointConnections pointConnection = new PointConnections(point1, point2);
        assertFalse(pointConnection.validate());

    }

    @Test
    public void shouldNotValidateFarHorizontalPoints() {

        Point point1 = new Point(0, 0);
        Point point2 = new Point(0, 2);

        PointConnections pointConnection = new PointConnections(point1, point2);
        assertFalse(pointConnection.validate());

    }

    @Test
    public void shouldValidateNearHorizontalPoints() {

        Point point1 = new Point(0, 2);
        Point point2 = new Point(1, 2);

        PointConnections pointConnection = new PointConnections(point1, point2);
        assertTrue(pointConnection.validate());

    }

}
