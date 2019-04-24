package achozen.rememberme.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import achozen.rememberme.engine.PeferencesUtil;
import achozen.rememberme.firebase.AuthFinishListener;
import achozen.rememberme.fragments.startup.ChooseNickFragment;
import achozen.rememberme.fragments.startup.LoginFragment;
import achozen.rememberme.navigation.FragmentNavigator;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import achozen.rememberme.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static achozen.rememberme.engine.PeferencesUtil.UNKNOWN_USERNAME;

public class SplashScreenActivity extends AppCompatActivity implements AuthFinishListener {

    private static final int VIEWS_FADE_IN_DELAY = 1000;
    private static final int VIEWS_FADE_OUT_DELAY = 3000;

    @BindView(R.id.game_fragment_place_holder)
    FrameLayout contentLayout;
    @BindView(R.id.headerImage)
    ImageView headerImage;
    @BindView(R.id.belowHeaderImage)
    ImageView belowHeaderImage;


    private Handler handler;
    private int mLongAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        handler = new Handler();
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        FragmentNavigator.navigateToNextFragment(this, LoginFragment.getInstance(this));
        setupViews();
      }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewsInAnimation(){
        handler.postDelayed(() -> {
            headerImage.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(null);

            belowHeaderImage.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(null);

        }, VIEWS_FADE_IN_DELAY);

        }
    private void setupViewsOutAnimation(){
        handler.postDelayed(() -> {
            headerImage.animate().alpha(0f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            headerImage.setVisibility(View.INVISIBLE);

                        }
                    });
            belowHeaderImage.animate().alpha(0f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            belowHeaderImage.setVisibility(View.INVISIBLE);
                        }
                    });
            contentLayout.animate()
                    .alpha(1f)
                    .setDuration(mLongAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    contentLayout.setVisibility(View.VISIBLE);
                }
            });;
        }, VIEWS_FADE_OUT_DELAY);
    }
    public void setupViews(){
        headerImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.header));
        belowHeaderImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.below_header));

        headerImage.setAlpha(0f);
        belowHeaderImage.setAlpha(0f);
        contentLayout.setAlpha(0f);

        headerImage.setVisibility(View.VISIBLE);
        belowHeaderImage.setVisibility(View.VISIBLE);

        setupViewsInAnimation();
        setupViewsOutAnimation();
    }

    @Override
    public void onAuthFinished() {
        if(UNKNOWN_USERNAME.equalsIgnoreCase(PeferencesUtil.readFromPrefs(this, PeferencesUtil.Preferences.USERNAME))){
            FragmentNavigator.navigateToNextFragment(this, new ChooseNickFragment());
        }else{
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
