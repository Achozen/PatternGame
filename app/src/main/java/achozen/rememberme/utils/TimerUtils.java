package achozen.rememberme.utils;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Achozen on 2016-06-05.
 */
public class TimerUtils {
    static ValueAnimator timeLeftAnimator = new ValueAnimator();
    public static void beforeAnimationCount(final TextView textView, final OnPreDrawingListener listener){

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(3, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("GET READY\n"+String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText("Go !");
                listener.onPreAnimationCountOver();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(4000);
        animator.start();

    }

    public static void beforeDrawCount(final TextView textView, final OnPreDrawingListener listener){

        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(5, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("REPEAT PATTERN\n"+String.valueOf(animation.getAnimatedValue()));
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText("START !");
                listener.onPreDrawingStart();
                startTimeLeftCounter(textView, listener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.setDuration(6000);
        animator.start();

    }

    public static void startTimeLeftCounter(final TextView textView, final OnPreDrawingListener
            listener){

        timeLeftAnimator.setObjectValues(30, 0);
        timeLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        timeLeftAnimator.setInterpolator(new LinearInterpolator());
        timeLeftAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText("Game Over");
                listener.onDrawingTimeOver();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        timeLeftAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        timeLeftAnimator.setDuration(30000);
        timeLeftAnimator.start();
    }

    public static int stopTimeLeftCounter(){
        int timeLeft = (int )timeLeftAnimator.getAnimatedValue();
        return timeLeft;
    }
}
