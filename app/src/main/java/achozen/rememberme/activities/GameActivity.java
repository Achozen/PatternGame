package achozen.rememberme.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.engine.GameProgressCoordinator;
import achozen.rememberme.engine.OnLevelFinishListener;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.fragments.PatternGameFragment;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.navigation.FragmentNavigator;
import achozen.rememberme.statistics.GameState;
import achozen.rememberme.statistics.GameStatistics;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        requestForAds();
        gamemode = (GameMode) getIntent().getSerializableExtra(GAME_MODE);

        gameProgressCoordinator = new GameProgressCoordinator(this, this);
        gameProgressCoordinator.startGame(gamemode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @OnClick(R.id.restartButton)
    void restartClickListener(View v) {
        gameProgressCoordinator.startNextLevel();
    }

    @OnClick(R.id.backButton)
    void backClickListener(View v) {
        Intent intent = new Intent(GameActivity.this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.settingsButton)
    void settingsClickListener(View v) {
        Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRankedFinished(GameStatistics statistics) {
        // show statistics screen
    }

    @Override
    public void startNewLevel(GameInitializationData gameInitializationData) {
        PatternGameFragment gameFragment = new PatternGameFragment();
        gameFragment.setGameInitializationData(gameInitializationData);
        gameFragment.setOnLevelFinishListener(this);
        FragmentNavigator.navigateToNextFragment(GameActivity.this, gameFragment);
    }

    @Override
    public void onTrainingFinished() {
        Log.d("TAGTAG", "onTrainingFinished finishing activity");
        finish();
    }

    @Override
    public void onGameSaveInstanceState(GameStatistics statistics) {

    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onLevelFinished(GameStatistics gameStatistics) {
        if (GameState.FAILED.equals(gameStatistics.getGameState())) {
            finish();
        } else {
            gameProgressCoordinator.startNextLevel();
        }

    }
}
