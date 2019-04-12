package achozen.rememberme.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import achozen.rememberme.LockPointsPositioner;
import achozen.rememberme.R;
import achozen.rememberme.interfaces.AnimationProgressListener;
import achozen.rememberme.interfaces.PointPosition;
import achozen.rememberme.statistics.GameState;
import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.utils.DrawingUtil;
import achozen.rememberme.utils.OnPreDrawingListener;
import achozen.rememberme.utils.TimerUtils;
import androidx.core.content.ContextCompat;

public class PatternView extends View implements AnimationProgressListener, OnPreDrawingListener, OnPathDrawingListener {
    private ShapeDrawable mDrawable = new ShapeDrawable(new OvalShape());
    private GameInitializationData gameInitializationData;
    float xStart = 0;
    float yStart = 0;
    float xStop = 0;
    float yStop = 0;
    int width = 300;
    int height = 50;
    int motionXMultiplyer;
    int motionYMultiplyer;
    private Path drawPath;
    private int paintColor = 0x00FF00;
    private Paint drawPaint, canvasPaint;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;
    private Context context;
    private PatternAnimator patternAnimator;
    private PathDrawable pathDrawable;
    private int w, h = 0;
    private TextView notificationTextView;
    private boolean drawingModeEnabled;
    private HoverPointChecker hoverPointChecker;
    private ArrayList<PointPosition> allPoints;
    private ArrayList<PointPosition> alreadyLinkedPoints = new ArrayList<>();
    private ArrayList<PointPosition> randomlyGeneratedPoints;
    private OnLevelFinishListener onLevelFinishListener;


    public PatternView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        Log.d("TAGTAG", "PatternView2");
        setupDrawing();
        this.context = context;
    }

    private void setupDrawing() {
        float[] dashes = {0.0f, Float.MAX_VALUE};
        //prepare for drawing and setup paint stroke properties
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.GREEN);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setupView(GameInitializationData gameInitializationData, TextView notifyingTextView, OnLevelFinishListener onLevelFinishListener) {
        this.notificationTextView = notifyingTextView;
        this.gameInitializationData = gameInitializationData;
        this.onLevelFinishListener = onLevelFinishListener;
    }


    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        clearGameView();
        patternAnimator = new PatternAnimator(this, drawCanvas, this);
        TimerUtils.beforeAnimationCount(notificationTextView, this);
        disableDrawingMode();
        hoverPointChecker = new HoverPointChecker(allPoints, this);
        //    pathDrawable = new Pa.thDrawable();
        //    this.setBackground(pathDrawable);

    }

    public void drawPointsOnCanvas(Canvas canvas, boolean demo) {
        Bitmap icon;
        Bitmap iconChecked;
        iconChecked = BitmapFactory.decodeResource(this.getResources(), R.drawable.point_drawable_checked);
        icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.point_drawable);


        icon = Bitmap.createScaledBitmap(icon, 50, 50, false);
        allPoints = LockPointsPositioner.obtainPointCoordinatesForCanvas(w, h,
                gameInitializationData.getPointPositions(), gameInitializationData.getGameSize());

        iconChecked = Bitmap.createScaledBitmap(iconChecked, 60, 60, false);
        allPoints = LockPointsPositioner.obtainPointCoordinatesForCanvas(w, h,
                gameInitializationData.getPointPositions(), gameInitializationData.getGameSize());

        for (PointPosition point : allPoints) {
            canvas.drawBitmap(icon, point.getXCanvas() - (icon.getWidth() / 2), point.getYCanvas() - (icon.getHeight() / 2), null);
        }
        if (demo) {
            for (PointPosition point : randomlyGeneratedPoints) {
                canvas.drawBitmap(iconChecked, point.getXCanvas() - (iconChecked.getWidth() / 2), point.getYCanvas() - (iconChecked.getHeight() / 2), null);
            }
        } else {
            for (PointPosition point : alreadyLinkedPoints) {
                canvas.drawBitmap(iconChecked, point.getXCanvas() - (iconChecked.getWidth() / 2), point.getYCanvas() - (iconChecked.getHeight() / 2), null);
            }
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TAGTAG", "onDraw");
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        if (drawingModeEnabled) {
            drawPointsOnCanvas(drawCanvas, false);
            drawAlreadyLinkedPoints(canvas);
            canvas.drawLine(xStart, yStart, xStop, yStop, drawPaint);
        }
    }

    private void drawAlreadyLinkedPoints(Canvas canvas) {
        if (alreadyLinkedPoints.size() < 2) {
            return;
        }
        for (int i = 0; i < alreadyLinkedPoints.size() - 1; i++) {
            Path pathToDraw = new Path();
            Paint drawPaint = DrawingUtil.getPaintForPath();

            pathToDraw.moveTo(alreadyLinkedPoints.get(i).getXCanvas(), alreadyLinkedPoints.get(i).getYCanvas());
            pathToDraw.lineTo(alreadyLinkedPoints.get(i + 1).getXCanvas(), alreadyLinkedPoints.get(i + 1).getYCanvas());

            canvas.drawPath(pathToDraw, drawPaint);
        }
    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        if (!drawingModeEnabled) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xStart = touchX;
                yStart = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                xStop = touchX;
                yStop = touchY;
                hoverPointChecker.checkIfCurrentPositionOverlapsPoint(xStop, yStop);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                xStart = 0;
                yStart = 0;
                xStop = 0;
                yStop = 0;
                alreadyLinkedPoints = new ArrayList<>();
                hoverPointChecker = new HoverPointChecker(allPoints, this);
                clearGameView();
                invalidate();
                break;
            default:
                return false;
        }
        return true;

    }

    public void enableDrawingMode() {
        drawingModeEnabled = true;
    }

    public void disableDrawingMode() {
        drawingModeEnabled = false;
    }

    private void clearGameView() {
        drawCanvas.drawColor(ContextCompat.getColor(context, R.color.black));
        drawPointsOnCanvas(drawCanvas, false);
    }

    private void startLevelAnimation() {
        randomlyGeneratedPoints = LockPointsPositioner.calculateCoordinatesForGeneratedPointsByGameSize(gameInitializationData
                        .getPatternPointPositions(), w, h,
                gameInitializationData.getGameSize());
        drawPointsOnCanvas(drawCanvas, true);

        patternAnimator.animatePath(randomlyGeneratedPoints);
    }

    @Override
    public void onAnimationFinish() {
        TimerUtils.beforeDrawCount(notificationTextView, this);
    }

    @Override
    public void onPreDrawingStart() {
        clearGameView();
        enableDrawingMode();
    }

    @Override
    public void onDrawingTimeOver() {
        onLevelFinishListener.onLevelFinished(createStatisticsForCurrentLevel(GameState.FAILED, 0));
    }

    @Override
    public void onPreAnimationCountOver() {
        startLevelAnimation();
    }


    @Override
    public void onPointMeet(PointPosition point) {
        xStart = point.getXCanvas();
        yStart = point.getYCanvas();
        addPointToLinked(point);
        checkIfGameDone();
    }

    private void checkIfGameDone() {
        int correctPoints = 0;
        if (alreadyLinkedPoints == null || randomlyGeneratedPoints == null) {
            return;
        }
        if (alreadyLinkedPoints.size() != randomlyGeneratedPoints.size()) {
            return;
        }
        for (int i = 0; i < alreadyLinkedPoints.size(); i++) {
            if (alreadyLinkedPoints.get(i).getColumn() == randomlyGeneratedPoints.get(i).getColumn() &&
                    alreadyLinkedPoints.get(i).getRow() == randomlyGeneratedPoints.get(i).getRow()) {
                correctPoints++;
            }
        }

        if (correctPoints == randomlyGeneratedPoints.size()) {
            Toast.makeText(context, "CONGRATULATIONS", Toast.LENGTH_SHORT).show();
            onLevelFinishListener.onLevelFinished(createStatisticsForCurrentLevel(GameState.SUCCESS, TimerUtils.stopTimeLeftCounter()));
        }

    }

    private GameStatistics createStatisticsForCurrentLevel(GameState gameState, int timeLeft) {
        return new GameStatistics(gameState, timeLeft, 30);
    }

    private void addPointToLinked(PointPosition point) {
        alreadyLinkedPoints.add(point);
    }
}