package achozen.rememberme.engine;

/**
 * Created by Achozen on 2016-02-27.
 */
public class Point implements PointPosition {

   private int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
