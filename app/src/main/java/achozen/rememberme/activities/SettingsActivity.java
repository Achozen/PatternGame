package achozen.rememberme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import achozen.rememberme.R;
import achozen.rememberme.engine.PeferencesUtil;
import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameMode;
import achozen.rememberme.enums.GameSize;
import androidx.core.content.ContextCompat;

import static achozen.rememberme.activities.GameActivity.GAME_MODE;
import static achozen.rememberme.engine.PeferencesUtil.Preferences.DIFFICULTY;
import static achozen.rememberme.engine.PeferencesUtil.Preferences.SIZE;
import static achozen.rememberme.enums.Difficulty.EASY;
import static achozen.rememberme.enums.Difficulty.HARD;
import static achozen.rememberme.enums.GameSize.BIG;
import static achozen.rememberme.enums.GameSize.SMALL;


/**
 * Created by Achozen on 2016-05-26.
 */
public class SettingsActivity extends Activity {

    private Context context;

    private ImageView easyButton;
    private ImageView mediumButton;
    private ImageView hardButton;

    private ImageView smallSizeButton;
    private ImageView mediumSizeButton;
    private ImageView bigSizeButton;

    private ImageView saveButton;
    private GameMode gameMode;

    GameSize pickedSize;
    Difficulty pickedDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        gameMode = (GameMode) getIntent().getExtras().get(GAME_MODE);
        context = this;
        prepareListeners();
        markButtonForCurrentSizeSettings();
        markButtonForCurrentDifficultySettings();
        requestForAds();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void prepareListeners() {
        View.OnClickListener onClickListener = view -> {
            switch (view.getId()) {
                case R.id.difficultyEasyButton:
                    clearDifficultyButtonsMarks();
                    setPickedIcon(view, R.drawable.easy_button_picked);
                    pickedDifficulty = EASY;
                    break;
                case R.id.difficultyMediumButton:
                    clearDifficultyButtonsMarks();
                    setPickedIcon(view, R.drawable.medium_button_picked);
                    pickedDifficulty = Difficulty.MEDIUM;
                    break;
                case R.id.difficultyHardButton:
                    clearDifficultyButtonsMarks();
                    setPickedIcon(view, R.drawable.hard_button_picked);
                    pickedDifficulty = HARD;
                    break;
                case R.id.sizeSmallButton:
                    clearSizeButtonsMarks();
                    setPickedIcon(view, R.drawable.small_button_picked);
                    pickedSize = SMALL;
                    break;
                case R.id.sizeMediumButton:
                    clearSizeButtonsMarks();
                    setPickedIcon(view, R.drawable.medium_size_button_picked);
                    pickedSize = GameSize.MEDIUM;
                    break;
                case R.id.sizeBigButton:
                    clearSizeButtonsMarks();
                    setPickedIcon(view, R.drawable.big_button_picked);
                    pickedSize = BIG;
                    break;
                case R.id.saveButton:
                    if (pickedSize != null) {
                        PeferencesUtil.storeInPrefs(context, SIZE, pickedSize);
                    }
                    if (pickedDifficulty != null) {
                        PeferencesUtil.storeInPrefs(context, DIFFICULTY, pickedDifficulty);
                    }
                    finish();
                    Intent intent = new Intent(this, GameActivity.class);
                    intent.putExtra(GAME_MODE, gameMode);
                    startActivity(intent);
                    break;
            }
        };

        easyButton = findViewById(R.id.difficultyEasyButton);
        mediumButton = findViewById(R.id.difficultyMediumButton);
        hardButton = findViewById(R.id.difficultyHardButton);
        smallSizeButton = findViewById(R.id.sizeSmallButton);
        mediumSizeButton = findViewById(R.id.sizeMediumButton);
        bigSizeButton = findViewById(R.id.sizeBigButton);
        saveButton = findViewById(R.id.saveButton);


        easyButton.setOnClickListener(onClickListener);
        mediumButton.setOnClickListener(onClickListener);
        hardButton.setOnClickListener(onClickListener);
        smallSizeButton.setOnClickListener(onClickListener);
        mediumSizeButton.setOnClickListener(onClickListener);
        bigSizeButton.setOnClickListener(onClickListener);

        saveButton.setOnClickListener(onClickListener);

    }

    private void setPickedIcon(View view, int id) {
        ((ImageView) view).setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, id));
    }

    private void clearSizeButtonsMarks() {

        smallSizeButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.small_button));
        mediumSizeButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.medium_size_button));
        bigSizeButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.big_button));

    }

    private void clearDifficultyButtonsMarks() {

        easyButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.easy_button));
        mediumButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.medium_button));
        hardButton.setImageDrawable(ContextCompat.getDrawable
                (SettingsActivity.this, R.drawable.hard_button));
    }

    private void markButtonForCurrentSizeSettings() {
        String currentSize = PeferencesUtil.readFromPrefs(context, SIZE);

        if (SMALL.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(smallSizeButton, R.drawable.small_button_picked);
        }
        if (GameSize.MEDIUM.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(mediumSizeButton, R.drawable.medium_size_button_picked);
        }
        if (BIG.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(bigSizeButton, R.drawable.big_button_picked);
        }
    }

    private void markButtonForCurrentDifficultySettings() {
        String currentSize = PeferencesUtil.readFromPrefs(context, DIFFICULTY);

        if (EASY.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(easyButton, R.drawable.easy_button_picked);
        }
        if (Difficulty.MEDIUM.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(mediumButton, R.drawable.medium_button_picked);
        }
        if (HARD.toString().equalsIgnoreCase(currentSize)) {
            setPickedIcon(hardButton, R.drawable.hard_button_picked);
        }
    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
