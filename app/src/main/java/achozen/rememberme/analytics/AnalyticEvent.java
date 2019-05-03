package achozen.rememberme.analytics;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameSize;

public class AnalyticEvent {

    private static final String GAME_PAUSED = "game_paused";
    private static final String GAME_RESUMED = "game_resumed";
    private static final String USERNAME_CHANGED = "username_changed";
    private static final String RANKING_GAME_FINISHED = "ranking_game_finished";
    private static final String TRAINING_GAME_FINISHED = "training_game_finished";
    private static final String RANKING_GAME_STARTED = "ranking_game_started";
    private static final String TRAINING_GAME_STARTED = "training_game_started";
    private static final String HIGH_SCORES_DISPLAYED = "high_scores_displayed";
    private static final String MY_SCORE_DISPLAYED = "my_score_displayed";
    private static final String SETTINGS_CLICKED = "settings_clicked";
    private static final String ABOUT_CLICKED = "about_clicked";
    private static final String PATTERN_GENERATED = "pattern_generated";
    private static final String GIVE_UP_CLICKED = "give_up_clicked";
    private static final String NO_TIME_LEFT = "no_time_left";
    private static final String MUSIC_OFF = "music_off";
    private static final String MUSIC_ON = "music_on";
    private static final String SOUND_EFFECTS_ON = "sound_effects_on";
    private static final String SOUND_EFFECTS_OFF = "sound_effects_off";
    private static FirebaseAnalytics firebaseAnalytics;
    private static Context appContext;

    public static void init(Context context) {
        appContext = context;
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    private static void sendEvent(String eventType, Bundle params) {
        if (BuildConfig.DEBUG) {
            String parameters = params != null ? params.toString() : null;
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(appContext, "Event: " + eventType + " with parameters: " + parameters + " is send", Toast.LENGTH_SHORT).show());
        }

        firebaseAnalytics.logEvent(eventType, params);
    }

    public static void gamePaused(boolean backClick) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("back_click", backClick);
        sendEvent(GAME_PAUSED, null);
    }

    public static void gameResumed(long inactivityTime) {
        Bundle bundle = new Bundle();
        bundle.putLong("inactivity_time", inactivityTime);
        sendEvent(GAME_RESUMED, bundle);
    }

    public static void usernameChanged() {
        sendEvent(USERNAME_CHANGED, null);
    }

    public static void rankingGameFinished(int level, int score, int time) {
        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        bundle.putInt("score", score);
        bundle.putInt("time", time);
        sendEvent(RANKING_GAME_FINISHED, bundle);
    }

    public static void trainingGameFinished() {
        sendEvent(TRAINING_GAME_FINISHED, null);
    }

    public static void rankingGameStarted() {
        sendEvent(RANKING_GAME_STARTED, null);
    }

    public static void trainingGameStarted() {
        sendEvent(TRAINING_GAME_STARTED, null);
    }

    public static void highScoresDisplayed() {
        sendEvent(HIGH_SCORES_DISPLAYED, null);
    }

    public static void myScoreDisplayed() {
        sendEvent(MY_SCORE_DISPLAYED, null);
    }

    public static void settingsClicked() {
        sendEvent(SETTINGS_CLICKED, null);
    }

    public static void aboutClicked() {
        sendEvent(ABOUT_CLICKED, null);
    }

    public static void musicOff() {
        sendEvent(MUSIC_OFF, null);
    }

    public static void musicOn() {
        sendEvent(MUSIC_ON, null);
    }

    public static void soundEffectsOn() {
        sendEvent(SOUND_EFFECTS_ON, null);
    }

    public static void soundEffectsOff() {
        sendEvent(SOUND_EFFECTS_OFF, null);
    }

    public static void patternGenerated(long time, GameSize gameSize, Difficulty difficulty, int linksNumber) {
        Bundle bundle = new Bundle();
        bundle.putLong("time", time);
        bundle.putString("game_size", gameSize.name());
        bundle.putString("difficulty", difficulty.name());
        bundle.putInt("links_number", linksNumber);
        sendEvent(PATTERN_GENERATED, bundle);
    }

    public static void giveUpClicked() {
        sendEvent(GIVE_UP_CLICKED, null);
    }

    public static void noTimeLeft() {
        sendEvent(NO_TIME_LEFT, null);
    }
}
