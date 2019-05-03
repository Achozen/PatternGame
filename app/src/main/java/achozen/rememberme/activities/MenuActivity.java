package achozen.rememberme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import achozen.rememberme.R;
import achozen.rememberme.analytics.AnalyticEvent;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.sounds.SoundPlayer;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static achozen.rememberme.activities.GameActivity.GAME_MODE;

/**
 * Created by Achozen on 2016-02-23.
 */
public class MenuActivity extends AppCompatActivity {
    @BindView(R.id.loggedAsTextView)
    TextView loggedAsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        setupAds();
        SoundPlayer.initMediaPlayer(this.getApplicationContext());
        setupLoggedAsTextView();
    }

    private void setupLoggedAsTextView() {
        String userEmail;
        String loggedAsFullText;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            loggedAsFullText = getString(R.string.logged_as) + userEmail;
        } else {
            loggedAsFullText = "OFFLINE";
        }


        loggedAsTextView.setText(loggedAsFullText);
    }

    @OnClick(R.id.buttonStart)
    void startClickListener(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GAME_MODE, GameMode.RANKING);
        startActivity(intent);
        AnalyticEvent.rankingGameStarted();
    }

    @OnClick(R.id.buttonTraining)
    void trainingClickListener(View v) {
        Intent intent = new Intent(MenuActivity.this, TrainingSettingsActivity.class);
        intent.putExtra(GAME_MODE, GameMode.TRAINING);
        startActivity(intent);
        AnalyticEvent.trainingGameStarted();
    }

    @OnClick(R.id.buttonSettings)
    void startSettingsListener(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        AnalyticEvent.settingsClicked();
    }

    @OnClick(R.id.buttonExit)
    void exitClickListener(View v) {
        finish();
    }

    @OnClick(R.id.buttonAbout)
    void aboutClickListener(View v) {
        Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(intent);
        AnalyticEvent.aboutClicked();
    }

    @OnClick(R.id.buttonHighScores)
    void highScoresClickListener(View v) {
        Intent intent = new Intent(MenuActivity.this, HighScoresActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}
