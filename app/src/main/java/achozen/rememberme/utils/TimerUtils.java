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
    private static ValueAnimator timeLeftAnimator = new ValueAnimator();
    private static ValueAnimator timeBeforeAnimationAnimator = new ValueAnimator();
    private static ValueAnimator timeBeforeDrawAnimator = new ValueAnimator();
    private static long totalTimeStartTimestamp = 0;
    private static long alreadyMeasuredTime = 0;

    public static void pauseTotalTimeMeasurement() {
        alreadyMeasuredTime += System.currentTimeMillis() - totalTimeStartTimestamp;
        totalTimeStartTimestamp = 0;
    }

    public static void startTotalTimeMeasurement(long initValue) {
        alreadyMeasuredTime = initValue;
        totalTimeStartTimestamp = System.currentTimeMillis();
    }

    public static void startTotalTimeMeasurement() {
        alreadyMeasuredTime = 0;
        totalTimeStartTimestamp = System.currentTimeMillis();
    }


    public static void resumeTotalTimeMeasurement() {
        totalTimeStartTimestamp = System.currentTimeMillis();
    }

    public static long finishAndGetTotalTimeMeasurement() {
        long timeSpendPlaying = alreadyMeasuredTime + (System.currentTimeMillis() - totalTimeStartTimestamp);
        totalTimeStartTimestamp = 0;
        alreadyMeasuredTime = 0;
        return timeSpendPlaying;
    }

    public static void beforeAnimationCount(final TextView textView, final OnPreDrawingListener listener) {
        timeBeforeAnimationAnimator = new ValueAnimator();
        timeBeforeAnimationAnimator.setObjectValues(3, 0);
        timeBeforeAnimationAnimator.setInterpolator(new LinearInterpolator());
        timeBeforeAnimationAnimator.addUpdateListener(animation -> textView.setText("GET READY\n" + String.valueOf(animation.getAnimatedValue())));
        timeBeforeAnimationAnimator.addListener(new Animator.AnimatorListener() {
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
        timeBeforeAnimationAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        timeBeforeAnimationAnimator.setDuration(3000);
        timeBeforeAnimationAnimator.start();

    }

    public static void beforeDrawCount(final TextView textView, final OnPreDrawingListener listener) {

        timeBeforeDrawAnimator = new ValueAnimator();
        timeBeforeDrawAnimator.setObjectValues(3, 0);
        timeBeforeDrawAnimator.setInterpolator(new LinearInterpolator());
        timeBeforeDrawAnimator.addUpdateListener(animation -> textView.setText("REPEAT PATTERN\n" + String.valueOf(animation.getAnimatedValue())));
        timeBeforeDrawAnimator.addListener(new Animator.AnimatorListener() {
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
        timeBeforeDrawAnimator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        timeBeforeDrawAnimator.setDuration(3000);
        timeBeforeDrawAnimator.start();

    }

    public static void startTimeLeftCounter(final TextView textView, final OnPreDrawingListener
            listener) {

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
                timeLeftAnimator.removeAllListeners();
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

    public static void pauseTimeLeftCounter() {
        timeLeftAnimator.pause();
        timeBeforeDrawAnimator.pause();
        timeBeforeAnimationAnimator.pause();
    }

    public static void forceEndTimeLeftCounter() {
        if (timeBeforeDrawAnimator.isRunning()) {
            timeBeforeDrawAnimator.end();
        }
        if (timeBeforeAnimationAnimator.isRunning()) {
            timeBeforeAnimationAnimator.end();
        }
        if (timeLeftAnimator.isRunning()) {
            timeLeftAnimator.end();
        }
    }

    public static void resumeTimeLeftCounter() {
        timeLeftAnimator.resume();
        timeBeforeDrawAnimator.resume();
        timeBeforeAnimationAnimator.resume();
    }

    public static void clearTimers() {
        timeLeftAnimator.removeAllListeners();
        timeLeftAnimator.removeAllUpdateListeners();
        timeLeftAnimator.cancel();
        timeBeforeDrawAnimator.removeAllListeners();
        timeBeforeDrawAnimator.removeAllUpdateListeners();
        timeBeforeDrawAnimator.cancel();
        timeBeforeAnimationAnimator.removeAllListeners();
        timeBeforeAnimationAnimator.removeAllUpdateListeners();
        timeBeforeAnimationAnimator.cancel();
    }

    public static int finishCountingAndGetLeftValue() {
        int timeLeft = (int) timeLeftAnimator.getAnimatedValue();
        return timeLeft;
    }
}
