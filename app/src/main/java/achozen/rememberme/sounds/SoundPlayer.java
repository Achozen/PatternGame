package achozen.rememberme.sounds;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import achozen.rememberme.R;
import achozen.rememberme.engine.PreferencesUtil;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int popSound;
    private static int unlockError;
    private static int unlockedsound;
    private static boolean loaded;
    private static MediaPlayer mediaPlayer;
    private static boolean effectsEnabled = true;
    private static boolean backgroundMusicEnabled = true;
    private static Context appContext;


    public static void initMediaPlayer(Context context) {
        appContext = context;
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_1);
            mediaPlayer.setLooping(true);
            startBackgroundMusic();
        }
    }

    public static void initSoundPool(Context context) {
        effectsEnabled = PreferencesUtil.readFromPrefs(context, PreferencesUtil.SoundPreferences.SOUND_EFFECTS);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        popSound = soundPool.load(context, R.raw.pop_sound, 1);
        unlockError = soundPool.load(context, R.raw.unlock_error_sound, 1);
        unlockedsound = soundPool.load(context, R.raw.unlocked_sound, 1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loaded = true);
    }

    public static void playPopSound() {
        if (!loaded || !effectsEnabled) {
            return;
        }
        soundPool.play(popSound, 1, 1, 0, 0, 1);
    }

    public static void playErrorSound() {
        if (!loaded || !effectsEnabled) {
            return;
        }
        soundPool.play(unlockError, 0.5f, 0.5f, 0, 0, 1);
    }

    public static void playUnlockedSound() {
        if (!loaded || !effectsEnabled) {
            return;
        }
        soundPool.play(unlockedsound, 0.5f, 0.5f, 0, 0, 1);
    }

    public static void releaseSoundPool() {
        soundPool.release();
        soundPool = null;
        loaded = false;
    }

    public static void startBackgroundMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && appContext != null && PreferencesUtil.readFromPrefs(appContext, PreferencesUtil.SoundPreferences.MUSIC)) {
            mediaPlayer.start();
        }
    }

    public static void pauseBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}
