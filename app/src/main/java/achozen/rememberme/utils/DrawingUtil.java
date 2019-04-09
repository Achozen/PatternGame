package achozen.rememberme.utils;

import android.graphics.Paint;

/**
 * Created by Achozen on 2016-06-07.
 */
public class DrawingUtil {

    public static Paint getPaintForPath(){
        Paint drawPaint = new Paint();
        drawPaint.setColor(0xFF000000);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        return drawPaint;
    }
}