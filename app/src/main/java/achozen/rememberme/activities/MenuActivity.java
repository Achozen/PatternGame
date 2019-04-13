package achozen.rememberme.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import achozen.rememberme.R;
import achozen.rememberme.enums.GameMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static achozen.rememberme.activities.GameActivity.GAME_MODE;

/**
 * Created by Achozen on 2016-02-23.
 */
public class MenuActivity extends Activity {
    @BindView(R.id.loggedAsTextView)
    TextView loggedAsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        setupAds();
        setupLoggedAsTextView();
    }

    private void setupLoggedAsTextView(){
        String userEmail;
        String loggedAsFullText;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            userEmail = currentUser.getEmail();
            loggedAsFullText= getString(R.string.logged_as) + userEmail;
        }else{
            loggedAsFullText = "OFFLINE";
        }


        loggedAsTextView.setText(loggedAsFullText);
    }

    @OnClick(R.id.buttonStart)
    void startClickListener(View v){
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        intent.putExtra(GAME_MODE, GameMode.RANKING);
        startActivity(intent);
    }

    @OnClick(R.id.buttonTraining)
    void trainingClickListener(View v){
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        intent.putExtra(GAME_MODE, GameMode.TRAINING);
        startActivity(intent);
    }

    @OnClick(R.id.buttonExit)
    void exitClickListener(View v){
        finish();
    }

    @OnClick(R.id.buttonAbout)
    void aboutClickListener(View v){
        Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonHighScores)
    void highScoresClickListener(View v){
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
