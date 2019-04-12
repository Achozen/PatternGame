package achozen.rememberme.engine;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

import achozen.rememberme.interfaces.AnimationProgressListener;
import achozen.rememberme.interfaces.PointPosition;
import achozen.rememberme.utils.DrawingUtil;

/**
 * Created by Achozen on 2016-05-31.
 */
public class PatternAnimator implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private final PatternView patternView;
    private final Canvas canvas;
    private final AnimationProgressListener animationProgressListener;
    private int currentlyDrawingLink;
    private int maxLinksToDraw;
    private ArrayList<PointPosition> pointPositions;
    ValueAnimator valueAnimator;
    private Path pathToDraw;
    Paint drawPaint;
    float startX = 0;
    float startY = 0;


    public PatternAnimator(PatternView patternview, Canvas canvas, AnimationProgressListener
            animationProgressListener) {
        this.patternView = patternview;
        this.canvas = canvas;
        this.animationProgressListener = animationProgressListener;
    }

    public void animatePath(ArrayList<PointPosition> pointPositions) {
        this.pointPositions = pointPositions;
        maxLinksToDraw = pointPositions.size() - 1;
        pathToDraw = new Path();

        drawPaint = DrawingUtil.getPaintForPath();

        prepareValueAnimatorForLink(pointPositions.get(0), pointPositions.get(1));

        //    maxLinksToDraw = pointPositions.size() - 1;
        //drawNextLink(pointPositions.get(0), pointPositions.get(1));
    }

    void prepareValueAnimatorForLink(PointPosition point1, PointPosition point2) {

        startX = point1.getXCanvas();
        startY = point1.getYCanvas();

        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("toX", point1.getXCanvas(),
                point2.getXCanvas());
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("toY", point1.getYCanvas(),
                point2.getYCanvas());
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(xHolder, yHolder);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(this);
        valueAnimator.start();

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        Log.d("TAGTAG", "To dla x " + (Float) animation.getAnimatedValue("toX"));
        Log.d("TAGTAG", "To dla y " + (Float) animation.getAnimatedValue("toY"));

        float xAnim = (Float) animation.getAnimatedValue("toX");
        float yAnim = (Float) animation.getAnimatedValue("toY");

        pathToDraw.reset();

        pathToDraw.moveTo(startX, startY);
        pathToDraw.lineTo(xAnim, yAnim);


        canvas.drawPath(pathToDraw, drawPaint);
        patternView.invalidate();

    }

    @Override
    public void onAnimationStart(Animator animation) {
        patternView.disableDrawingMode();
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        currentlyDrawingLink++;

        if (currentlyDrawingLink < maxLinksToDraw) {
            prepareValueAnimatorForLink(pointPositions.get(currentlyDrawingLink), pointPositions.get
                    (currentlyDrawingLink + 1));
        } else {
            animationProgressListener.onAnimationFinish();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


}
