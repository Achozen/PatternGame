package achozen.rememberme.engine;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PatternView extends View{
    private ShapeDrawable mDrawable = new ShapeDrawable(new OvalShape());
    Canvas canvas;
    private GameInitializationData gameInitializationData;
    float xStart = 0;
    float yStart = 0;
    float xStop = 0;
    float yStop = 0;
    int width = 300;
    int height = 50;
    private Path drawPath;
    private int paintColor = 0xFF660000;
    private Paint drawPaint, canvasPaint;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;

    public PatternView(Context context) {
        super(context);
        Log.d("TAGTAG", "PatternView1");
    }
    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("TAGTAG", "PatternView2");
        setupDrawing();
    }

    public PatternView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        Log.d("TAGTAG", "PatternView3");

    }
    private void setupDrawing(){

        //prepare for drawing and setup paint stroke properties
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    public void setupView(GameInitializationData gameInitializationData){
        this.gameInitializationData = gameInitializationData;
    }

    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TAGTAG", "onDraw");
      //  canvas.drawLine();
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
 //       canvas.drawPath(drawPath, drawPaint);
        canvas.drawLine(xStart,yStart,xStop,yStop,drawPaint);

    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               // drawPath.moveTo(touchX, touchY);
                xStart = touchX;
                yStart = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
             //   drawPath.lineTo(touchX, touchY);
                xStop = touchX;
                yStop = touchY;
                break;
            case MotionEvent.ACTION_UP:
              //  drawPath.lineTo(touchX, touchY);
              //  drawCanvas.drawPath(drawPath, drawPaint);
              //  drawPath.reset();
/*                xStart = 0;
                yStart = 0;*/
                break ;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;

    }
}