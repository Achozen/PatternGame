package achozen.rememberme;

import java.util.List;

import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-05-25.
 */
public class LockPointsPositioner {

    public static List<PointPosition> obtainPointCoordinatesForCanvas(int w, int h,
                                                                      List<PointPosition> points, GameSize gameSize) {

        switch (gameSize) {
            case SMALL:
                return calculateCoordinatesForByGameSize(points, w, h, 3);
            case MEDIUM:
                return calculateCoordinatesForByGameSize(points, w, h, 4);
            case BIG:
                return calculateCoordinatesForByGameSize(points, w, h, 5);

        }
        return points;
    }


    private static List<PointPosition> calculateCoordinatesForByGameSize(
            List<PointPosition> points, int w, int h, int gameSize) {

        if (points.size() != gameSize * gameSize) {
            return null;
        }

        int wMargin = (int) (w * 0.2);
        int hMargin = (int) (h * 0.2);

        int wWithoutMargins = (w - (2 * wMargin));
        int hWithoutMargins = (h - (2 * hMargin));

        int wStep = wWithoutMargins / (gameSize - 1);
        int hStep = hWithoutMargins / (gameSize - 1);

        int pointNumber = 0;
        for (int wIt = 0; wIt < gameSize; wIt++) {
            for (int hIt = 0; hIt < gameSize; hIt++) {
                points.get(pointNumber++).setXCanvas((hIt * wStep) + wMargin).setYCanvas((wIt * hStep)
                        + hMargin);
            }
        }

        return points;
    }

    public static List<PointPosition> calculateCoordinatesForGeneratedPointsByGameSize(
            List<PointPosition> points, int w, int h, GameSize gameSize) {

        int size = 0;
        switch (gameSize) {
            case SMALL:
                size = 3;
                break;
            case MEDIUM:
                size = 4;
                break;
            case BIG:
                size = 5;

        }
        List<PointPosition> initLockPoints = PointsInitializer.generateLockPoints(gameSize);
        if (initLockPoints.size() != size * size) {
            return null;
        }

        int wMargin = (int) (w * 0.2);
        int hMargin = (int) (h * 0.2);

        int wWithoutMargins = (w - (2 * wMargin));
        int hWithoutMargins = (h - (2 * hMargin));

        int wStep = wWithoutMargins / (size - 1);
        int hStep = hWithoutMargins / (size - 1);

        int pointNumber = 0;
        for (int wIt = 0; wIt < size; wIt++) {
            for (int hIt = 0; hIt < size; hIt++) {
                initLockPoints.get(pointNumber++).setXCanvas((hIt * wStep) + wMargin).setYCanvas((wIt * hStep)
                        + hMargin);
            }
        }


        for (PointPosition position : points) {
            for (int i = 0; i < initLockPoints.size(); i++) {
                if (position.getRow() == initLockPoints.get(i).getRow() &&
                        position.getColumn() == initLockPoints.get(i).getColumn()) {

                    position.setYCanvas(initLockPoints.get(i).getYCanvas());
                    position.setXCanvas(initLockPoints.get(i).getXCanvas());
                }

            }

        }

        return points;
    }

}
