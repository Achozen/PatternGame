package achozen.rememberme.engine;

import android.content.Context;
import android.content.SharedPreferences;

import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameSize;

/**
 * Created by Achozen on 2016-05-27.
 */
public class PreferencesUtil {
    private static final String prefType = "settings";
    public static final String UNKNOWN_USERNAME = "UNKNOWN";

    public static void storeInPrefs(Context context, Preferences type, String value) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(type.name(), value);
        editor.apply();
    }

    public static void storeInPrefs(Context context, SoundPreferences type, boolean value) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(type.name(), value);
        editor.apply();
    }

    public static void storeInPrefs(Context context, LongPreferences type, long value) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(type.name(), value);
        editor.apply();
    }

    public static String readFromPrefs(Context context, Preferences type) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);

        String defaultValue = "nothing";
        if (type == Preferences.SIZE) {
            defaultValue = GameSize.SMALL.name();
        }

        if (type == Preferences.DIFFICULTY) {
            defaultValue = Difficulty.EASY.name();
        }

        if (type == Preferences.USERNAME) {
            defaultValue = UNKNOWN_USERNAME;
        }

        return sharedPref.getString(type.name(), defaultValue);
    }

    public static boolean readFromPrefs(Context context, SoundPreferences type) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);

        boolean defaultValue = false;
        if (type == SoundPreferences.MUSIC) {
            defaultValue = true;
        }

        if (type == SoundPreferences.SOUND_EFFECTS) {
            defaultValue = true;
        }


        return sharedPref.getBoolean(type.name(), defaultValue);
    }


    public static long readFromPrefs(Context context, LongPreferences type) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);

        long defaultValue = 0;
        if (type == LongPreferences.LIVES_COUNT) {
            defaultValue = 0;
        }
        if (type == LongPreferences.LIVES_UPDATE_TIMESTAMP) {
            defaultValue = 0;
        }

        return sharedPref.getLong(type.name(), defaultValue);
    }


    public enum Preferences {
        SIZE,
        DIFFICULTY,
        USERNAME
    }

    public enum SoundPreferences {
        MUSIC,
        SOUND_EFFECTS
    }

    public enum LongPreferences {
        LIVES_COUNT,
        LIVES_UPDATE_TIMESTAMP
    }
}
