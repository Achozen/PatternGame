package achozen.rememberme.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.engine.GameSize;
import achozen.rememberme.engine.Point;
import achozen.rememberme.engine.PointPosition;
import achozen.rememberme.fragments.PatternGameFragment;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.navigation.FragmentNavigator;
import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-02-23.
 */
public class GameActivity extends FragmentActivity implements GameProgressListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        requestForAds();
        initiateGameFragment();

    }

    private void initiateGameFragment() {
        PatternGameFragment gameFragment = new PatternGameFragment();

        gameFragment.setGameInitializationData(createHardcodedInitData());
        FragmentNavigator.navigateToNextFragment(GameActivity.this, gameFragment);
    }


    private GameInitializationData createHardcodedInitData() {

        Point point1 = new Point(2, 2);
        Point point2 = new Point(1, 2);
        Point point3 = new Point(0, 1);
        ArrayList<PointPosition> arrayList = new ArrayList<>();

        arrayList.add(point1);
        arrayList.add(point2);
        arrayList.add(point3);


        return new GameInitializationData(GameSize.SMALL, arrayList);
    }

    @Override
    public void onGameLost(GameStatistics statistics) {

    }

    @Override
    public void onGameWin(GameStatistics statistics) {

    }

    @Override
    public void onGameSaveInstanceState(GameStatistics statistics) {

    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
