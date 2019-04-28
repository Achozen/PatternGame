package achozen.rememberme.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.R;
import achozen.rememberme.StartupPresenter;
import achozen.rememberme.fragments.PhaseFinishedListener;
import achozen.rememberme.fragments.ProgressWaitingFragment;
import achozen.rememberme.fragments.startup.ChooseNickFragment;
import achozen.rememberme.fragments.startup.LoginFragment;
import achozen.rememberme.navigation.FragmentNavigator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity implements StartupPresenter.ViewInterface {

    private static final int VIEWS_FADE_IN_DELAY = 1000;
    private static final int VIEWS_FADE_OUT_DELAY = 3000;

    @BindView(R.id.game_fragment_place_holder)
    FrameLayout contentLayout;
    @BindView(R.id.headerImage)
    ImageView headerImage;
    @BindView(R.id.belowHeaderImage)
    ImageView belowHeaderImage;
    @BindView(R.id.appVersion)
    TextView appVersion;
    StartupPresenter presenter;


    private Handler handler;
    private int mLongAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_splash_screen);
        presenter = new StartupPresenter(getApplicationContext(), this);
        ButterKnife.bind(this);
        handler = new Handler();
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        setupViews();
        appVersion.setText("Version: "+ BuildConfig.VERSION_NAME);

    }


    private void setupViewsInAnimation() {
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

    private void setupViewsOutAnimation() {
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
                    });
            ;
        }, VIEWS_FADE_OUT_DELAY);
    }

    public void setupViews() {
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
    public void displayLoginScreen(PhaseFinishedListener listener) {
        FragmentNavigator.navigateToNextFragment(this, LoginFragment.getInstance(listener));
    }

    @Override
    public void displayLoadingScreen() {
        FragmentNavigator.navigateToNextFragment(this, new ProgressWaitingFragment());
    }

    @Override
    public void displayNicknameScreen(PhaseFinishedListener phaseFinishedListener) {
        FragmentNavigator.navigateToNextFragment(this, ChooseNickFragment.getInstance(phaseFinishedListener));
    }


    @Override
    public void goToMainScreen() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}

