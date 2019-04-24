package achozen.rememberme.engine;

import android.content.Context;
import android.content.SharedPreferences;

import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameSize;

/**
 * Created by Achozen on 2016-05-27.
 */
public class PeferencesUtil {
    private static final String prefType = "settings";
    public static final String UNKNOWN_USERNAME = "UNKNOWN";

    public static void storeInPrefs(Context context, Preferences type, String value) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(type.name(), value);
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

    public enum Preferences {
        SIZE,
        DIFFICULTY,
        USERNAME
    }
}
