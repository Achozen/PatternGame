package achozen.rememberme.engine;

import java.util.ArrayList;

import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-06-07.
 */
public class HoverPointChecker {
    private final OnPathDrawingListener onPathDrawingListener;
    ArrayList<PointPosition> allPoints = new ArrayList<PointPosition>();
    final float HOVER_OFFSET = 50;
    float x = 0;
    float y = 0;

    HoverPointChecker(ArrayList<PointPosition> allPoints, OnPathDrawingListener onPathDrawingListener) {
        this.allPoints.addAll(allPoints);
        this.onPathDrawingListener = onPathDrawingListener;
    }

    public void checkIfCurrentPositionOverlapsPoint(float x, float y) {
        PointPosition pointToRemove = null;
        for (PointPosition point : allPoints) {
            if (checkIfCoordsOverlapsWithPoint(x, y, point)) {
                onPathDrawingListener.onPointMeet(point);
                pointToRemove = point;
                break;
            }
        }
        if (pointToRemove != null) {
            allPoints.remove(pointToRemove);
        }
    }

    boolean checkIfCoordsOverlapsWithPoint(float x, float y, PointPosition point) {
        float pointX = point.getXCanvas();
        float pointY = point.getYCanvas();

        return (Math.abs(x - pointX) <= HOVER_OFFSET && Math.abs(y - pointY) <= HOVER_OFFSET);
    }

}
