package achozen.rememberme.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import achozen.rememberme.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DISPLAY_TIME = 3000;
    private static final int VIEWS_FADE_IN_DELAY = 1000;
    private static final int VIEWS_FADE_OUT_DELAY = 3000;

    private ImageView headerImage;
    private ImageView belowHeaderImage;
    private Handler handler;
    private int mLongAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        setupViews();
      }

    private void setupViewsInAnimation(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                headerImage.animate()
                        .alpha(1f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(null);


                belowHeaderImage.animate()
                        .alpha(1f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(null);

            }
        }, VIEWS_FADE_IN_DELAY);

        }
    private void setupViewsOutAnimation(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                headerImage.animate().alpha(0f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                headerImage.setVisibility(View.GONE);

                            }
                        });
                belowHeaderImage.animate().alpha(0f)
                        .setDuration(mLongAnimationDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                belowHeaderImage.setVisibility(View.GONE);
                            }
                        });
            }
        }, VIEWS_FADE_OUT_DELAY);
    }
    public void setupViews(){

        headerImage = (ImageView) findViewById(R.id.headerImage);
        belowHeaderImage = (ImageView) findViewById(R.id.belowHeaderImage);

        headerImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.header));
        belowHeaderImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.below_header));

        headerImage.setAlpha(0f);
        belowHeaderImage.setAlpha(0f);

        headerImage.setVisibility(View.VISIBLE);
        belowHeaderImage.setVisibility(View.VISIBLE);

        setupViewsInAnimation();
        setupViewsOutAnimation();
        startMenuActivityWithDelay(SPLASH_SCREEN_DISPLAY_TIME);
    }


public void startMenuActivityWithDelay(int delay){
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashScreenActivity.this, MenuActivity.class);
        //    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(intent);

        }
    },delay);

}


}
