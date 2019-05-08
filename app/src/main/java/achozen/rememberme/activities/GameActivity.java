package achozen.rememberme.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

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
import achozen.rememberme.sounds.SoundPlayer;
import achozen.rememberme.statistics.GameStatistics;
import achozen.rememberme.statistics.LevelState;
import achozen.rememberme.utils.TimerUtils;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Achozen on 2016-02-23.
 */
public class GameActivity extends AppCompatActivity implements GameProgressListener,
        OnLevelFinishListener, RewardedVideoAdListener {

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

    boolean adLoaded = false;


    private PatternGameFragment currentGameFragment;
    private long inactivityTime;
    private RewardedVideoAd mRewardedVideoAd;
    private GameStatistics lastGameStatistics;
    private boolean videoRewardClosed;
    private boolean rewardObtained;
    private boolean lifeUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        requestForAds();
        loadRewardedVideoAd();
        gamemode = (GameMode) getIntent().getSerializableExtra(GAME_MODE);
        if (gamemode == GameMode.RANKING) {
            showMainPoints();
        } else {
            hideMainPoints();
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
        AnalyticEvent.rankingGameFinished(statistics.getLevelFinishedCounter(), statistics.getScoredPoints(), (int) statistics.getGameTime());
        FragmentNavigator.navigateToNextFragment(GameActivity.this, StatisticsFragment.getInstanceForGameEnd(statistics));
    }

    @Override
    public void startNewLevel(LevelInitializationData levelInitializationData) {
        showMainPoints();
        currentGameFragment = new PatternGameFragment();
        currentGameFragment.setLevelInitializationData(levelInitializationData);
        currentGameFragment.setOnLevelFinishListener(this);
        FragmentNavigator.navigateToNextFragment(GameActivity.this, currentGameFragment);
        currentPointsValue.setText(String.valueOf(levelInitializationData.getGameStatistics().getScoredPoints()));
    }

    @Override
    public void displayRevenueAd(GameStatistics statistics) {
        lastGameStatistics = statistics;

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void resumeLostGame(GameStatistics statistics) {
        lifeUsed = true;
        statistics.setLevelState(LevelState.SUCCESS);
        gameProgressCoordinator.startNextLevel(statistics);
    }

    @Override
    public void onTrainingFinished() {
        Log.d("TAGTAG", "onTrainingFinished finishing activity");
        AnalyticEvent.trainingGameFinished();

        finish();
    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onLevelFinished(GameStatistics gameStatistics) {
        SoundPlayer.playUnlockedSound();
        if (gameStatistics.getLevelState() == LevelState.SUCCESS) {
            gameProgressCoordinator.startNextLevel(gameStatistics);
        } else if (gamemode == GameMode.RANKING) {
            if (rewardObtained || lifeUsed) {
                onRankedFinished(gameStatistics);
            } else {
                displayAdditionalLifeQuestion(gameStatistics);
            }

        } else {
            onTrainingFinished();
        }

    }

    private void displayAdditionalLifeQuestion(GameStatistics gameStatistics) {
        hideMainPoints();
        FragmentNavigator.navigateToNextFragment(GameActivity.this, UseAnotherLifeFragment.getInstance(gameStatistics, this));
    }

    private void showMainPoints() {
        if (gamemode == GameMode.RANKING) {
            currentPointsLabel.setVisibility(View.VISIBLE);
            currentPointsValue.setVisibility(View.VISIBLE);
            giveUpButton.setVisibility(View.VISIBLE);
        }

    }

    private void hideMainPoints() {
        currentPointsLabel.setVisibility(View.INVISIBLE);
        currentPointsValue.setVisibility(View.INVISIBLE);
        giveUpButton.setVisibility(View.INVISIBLE);
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
        mRewardedVideoAd.pause(this);
        super.onPause();
        SoundPlayer.releaseSoundPool();
        SoundPlayer.pauseBackgroundMusic();
        isPaused = true;
        currentGameFragment.onGamePaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(this);
    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
        SoundPlayer.initSoundPool(this.getApplicationContext());
        SoundPlayer.startBackgroundMusic();

        if (isPaused) {
            isPaused = false;
            inactivityTime = System.currentTimeMillis();
            if (videoRewardClosed && rewardObtained) {
                resumeLostGame(lastGameStatistics);
                TimerUtils.startTotalTimeMeasurement(lastGameStatistics.getGameTime());
            } else if (videoRewardClosed) {
                videoRewardClosed = false;
            } else {
                pausedActivityView.setVisibility(View.VISIBLE);
                AnalyticEvent.gamePaused(false);
            }
        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
        rewardObtained = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdClosed() {
        videoRewardClosed = true;
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }
}
