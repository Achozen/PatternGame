package achozen.rememberme.engine;

import achozen.rememberme.interfaces.PointPosition;

/**
 * Created by Achozen on 2016-02-27.
 */
public class Point implements PointPosition {

    private int id;
    private int column, row;
    private float xOnCanvas, yOnCanvas;

    public Point(int id, int column, int row) {
        this.column = column;
        this.row = row;
        this.id = id;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public float getXCanvas() {
        return xOnCanvas;
    }

    @Override
    public float getYCanvas() {
        return yOnCanvas;
    }

    @Override
    public PointPosition setXCanvas(float xOnCanvas) {
        this.xOnCanvas = xOnCanvas;
        return this;
    }

    @Override
    public PointPosition setYCanvas(float yOnCanvas) {
        this.yOnCanvas = yOnCanvas;
        return this;
    }

    public int getId(){
        return id;
    }
}
