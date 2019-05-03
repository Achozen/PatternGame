package achozen.rememberme.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import achozen.rememberme.R;
import achozen.rememberme.analytics.AnalyticEvent;
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
import achozen.rememberme.utils.TimerUtils;
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

    @BindView(R.id.giveUpButton)
    View giveUpButton;

    @BindView(R.id.currentPointsValue)
    TextView currentPointsValue;

    @BindView(R.id.currentPointsLabel)
    TextView currentPointsLabel;


    private PatternGameFragment currentGameFragment;
    private long inactivityTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        requestForAds();
        gamemode = (GameMode) getIntent().getSerializableExtra(GAME_MODE);
        if (gamemode == GameMode.RANKING) {
            giveUpButton.setVisibility(View.VISIBLE);
        }

        gameProgressCoordinator = new GameProgressCoordinator(this, gamemode, this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @OnClick(R.id.resumeButton)
    void resumeClickListener(View v) {
        isPaused = false;
        currentGameFragment.onGameResumed();
        pausedActivityView.setVisibility(View.GONE);
        AnalyticEvent.gameResumed(System.currentTimeMillis() - inactivityTime);
    }

    @OnClick(R.id.exitButton)
    void exitClickListener(View v) {
        pausedActivityView.setVisibility(View.GONE);
        finish();
    }

    @OnClick(R.id.giveUpButton)
    void giveUpClickListener(View v) {
        TimerUtils.forceEndTimeLeftCounter();
        AnalyticEvent.giveUpClicked();
    }

    @Override
    public void onRankedFinished(GameStatistics statistics) {
        hideMainPoints();
        AnalyticEvent.rankingGameFinished(statistics.getLevelFinishedCounter(), statistics.getScoredPoints(), statistics.getLevelFinishedCounter());
        FragmentNavigator.navigateToNextFragment(GameActivity.this, StatisticsFragment.getInstanceForGameEnd(statistics));
    }

    @Override
    public void startNewLevel(LevelInitializationData levelInitializationData) {
        showMainPoints();
        currentGameFragment = new PatternGameFragment();
        currentGameFragment.setLevelInitializationData(levelInitializationData);
        currentGameFragment.setOnLevelFinishListener(this);
        FragmentNavigator.navigateToNextFragment(GameActivity.this, currentGameFragment);
        currentPointsValue.setText("" + levelInitializationData.getGameStatistics().getScoredPoints());
    }

    @Override
    public void onTrainingFinished() {
        Log.d("TAGTAG", "onTrainingFinished finishing activity");
        AnalyticEvent.trainingGameFinished();

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

    private void showMainPoints() {
        currentPointsLabel.setVisibility(View.VISIBLE);
        currentPointsValue.setVisibility(View.VISIBLE);
    }

    private void hideMainPoints() {
        currentPointsLabel.setVisibility(View.INVISIBLE);
        currentPointsValue.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isPaused) {
            return;
        }
        currentGameFragment.onGamePaused();
        isPaused = true;
        pausedActivityView.setVisibility(View.VISIBLE);
        inactivityTime = System.currentTimeMillis();
        AnalyticEvent.gamePaused(true);
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
            inactivityTime = System.currentTimeMillis();
            pausedActivityView.setVisibility(View.VISIBLE);
            AnalyticEvent.gamePaused(false);
        }
    }
}
