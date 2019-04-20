package achozen.rememberme.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameProgressCoordinator;
import achozen.rememberme.engine.LevelInitializationData;
import achozen.rememberme.engine.OnLevelFinishListener;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.fragments.PatternGameFragment;
import achozen.rememberme.fragments.StatisticsFragment;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.navigation.FragmentNavigator;
import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.statistics.LevelState;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Achozen on 2016-02-23.
 */
public class GameActivity extends FragmentActivity implements GameProgressListener,
        OnLevelFinishListener {

    public static final String GAME_MODE = "GameMode";
    GameProgressCoordinator gameProgressCoordinator;

    GameMode gamemode;
    private boolean isPaused;

    @BindView(R.id.gamePausePopup)
    View pausedActivityView;


    @BindView(R.id.currentPointsValue)
    TextView currentPointsValue;


    private PatternGameFragment currentGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        requestForAds();
        gamemode = (GameMode) getIntent().getSerializableExtra(GAME_MODE);

        gameProgressCoordinator = new GameProgressCoordinator(this, gamemode, this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @OnClick(R.id.resumeButton)
    void resumeClickListener(View v) {
        isPaused = false;
        currentGameFragment.onGameResumed();
        pausedActivityView.setVisibility(View.GONE);
    }

    @OnClick(R.id.exitButton)
    void exitClickListener(View v) {
        pausedActivityView.setVisibility(View.GONE);
        finish();
    }

    @Override
    public void onRankedFinished(GameStatistics statistics) {
        FragmentNavigator.navigateToNextFragment(GameActivity.this, StatisticsFragment.getInstance(statistics));
    }

    @Override
    public void startNewLevel(LevelInitializationData levelInitializationData) {
        currentGameFragment = new PatternGameFragment();
        currentGameFragment.setLevelInitializationData(levelInitializationData);
        currentGameFragment.setOnLevelFinishListener(this);
        FragmentNavigator.navigateToNextFragment(GameActivity.this, currentGameFragment);
        currentPointsValue.setText("" + levelInitializationData.getGameStatistics().getScoredPoints());
    }

    @Override
    public void onTrainingFinished() {
        Log.d("TAGTAG", "onTrainingFinished finishing activity");
        finish();
    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onLevelFinished(GameStatistics gameStatistics) {
        if (gameStatistics.getLevelState() == LevelState.SUCCESS) {
            gameProgressCoordinator.startNextLevel(gameStatistics);
        } else if (gamemode == GameMode.RANKING) {
            onRankedFinished(gameStatistics);
        } else {
            onTrainingFinished();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        currentGameFragment.onGamePaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            pausedActivityView.setVisibility(View.VISIBLE);
        }
    }
}
