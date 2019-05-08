package achozen.rememberme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.R;
import achozen.rememberme.analytics.AnalyticEvent;
import achozen.rememberme.engine.PreferencesUtil;
import achozen.rememberme.sounds.SoundPlayer;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.inputText)
    TextInputEditText editText;

    @BindView(R.id.loggedInValue)
    TextView loggedInVaue;

    @BindView(R.id.appVersion)
    TextView appVersion;

    @BindView(R.id.backgroundMusic)
    Switch backgroundMusicSwitch;

    @BindView(R.id.soundEffects)
    Switch soundEffectsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        requestForAds();
        final String userName = PreferencesUtil.readFromPrefs(this, PreferencesUtil.Preferences.USERNAME);
        if (!"UNKNOWN".equalsIgnoreCase(userName)) {
            editText.setText(userName);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            loggedInVaue.setText(currentUser.getEmail());
        }
        setupSoundSwitches();
        appVersion.setText("Version: " + BuildConfig.VERSION_NAME);
    }

    private void setupSoundSwitches() {
        boolean musicSwitchState = PreferencesUtil.readFromPrefs(this.getApplicationContext(), PreferencesUtil.SoundPreferences.MUSIC);
        boolean soundEffectsSwitchState = PreferencesUtil.readFromPrefs(this.getApplicationContext(), PreferencesUtil.SoundPreferences.SOUND_EFFECTS);

        backgroundMusicSwitch.setChecked(musicSwitchState);
        soundEffectsSwitch.setChecked(soundEffectsSwitchState);

        backgroundMusicSwitch.setOnCheckedChangeListener((button, checked) -> {
            PreferencesUtil.storeInPrefs(this.getApplicationContext(), PreferencesUtil.SoundPreferences.MUSIC, checked);
            if (checked) {
                AnalyticEvent.musicOn();
                SoundPlayer.startBackgroundMusic();
            } else {
                AnalyticEvent.musicOff();
                SoundPlayer.pauseBackgroundMusic();
            }
        });
        soundEffectsSwitch.setOnCheckedChangeListener((button, checked) -> {
            if (checked) {
                AnalyticEvent.soundEffectsOn();
            } else {
                AnalyticEvent.soundEffectsOff();
            }
            PreferencesUtil.storeInPrefs(this.getApplicationContext(), PreferencesUtil.SoundPreferences.SOUND_EFFECTS, checked);
        });
    }

    @OnClick(R.id.saveButton)
    void saveButtonClicked(View v) {
        if (editText.getText() != null && TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError("Username cannot be empty !");
        } else {
            PreferencesUtil.storeInPrefs(this, PreferencesUtil.Preferences.USERNAME, editText.getText().toString());
            AnalyticEvent.usernameChanged();
            finish();
        }
    }

    @OnClick(R.id.logoutButton)
    void onLogoutClicked(View v) {
        PreferencesUtil.storeInPrefs(this, PreferencesUtil.Preferences.USERNAME, PreferencesUtil.UNKNOWN_USERNAME);
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        Intent startupIntent = new Intent(this, SplashScreenActivity.class);
        startActivity(startupIntent);
    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
