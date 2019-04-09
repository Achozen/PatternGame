package achozen.rememberme.interfaces;

/**
 * Created by Achozen on 2016-02-27.
 */
public interface PointPosition {
     int getColumn();
     int getRow();
     float getXCanvas();
     float getYCanvas();
    PointPosition setXCanvas(float xOnCanvas);
    PointPosition setYCanvas(float yOnCanvas);
}

