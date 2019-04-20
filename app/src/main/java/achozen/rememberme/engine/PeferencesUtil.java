package achozen.rememberme.engine;

import android.content.Context;
import android.content.SharedPreferences;

import achozen.rememberme.enums.Difficulty;
import achozen.rememberme.enums.GameSize;
import achozen.rememberme.interfaces.Settings;

/**
 * Created by Achozen on 2016-05-27.
 */
public class PeferencesUtil {
    private static final String prefType = "settings";

    public static void storeInPrefs(Context context, Preferences type, Settings value) {

        SharedPreferences sharedPref = context.getSharedPreferences(prefType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(type.name(), value.toString());
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
            defaultValue = "UNKNOWN";
        }

        return sharedPref.getString(type.name(), defaultValue);
    }


    public enum Preferences {
        SIZE,
        DIFFICULTY,
        USERNAME
    }
}
