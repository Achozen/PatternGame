package achozen.rememberme;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Queue;

import achozen.rememberme.config.AppConfig;
import achozen.rememberme.engine.PeferencesUtil;
import achozen.rememberme.fragments.PhaseFinishedListener;

import static achozen.rememberme.StartupPresenter.StartupPhase.AUTHENTICATION;
import static achozen.rememberme.StartupPresenter.StartupPhase.CONFIG_DOWNLOAD;
import static achozen.rememberme.StartupPresenter.StartupPhase.NICKNAME_CHOOSE;
import static achozen.rememberme.engine.PeferencesUtil.UNKNOWN_USERNAME;

public class StartupPresenter implements PhaseFinishedListener {

    private final Context context;
    private ViewInterface view;
    private Queue<StartupPhase> phases = new LinkedList<StartupPhase>() {{
        add(CONFIG_DOWNLOAD);
        add(AUTHENTICATION);
        add(NICKNAME_CHOOSE);
    }};

    public StartupPresenter(Context context, ViewInterface view) {
        this.view = view;
        this.context = context;
        startPhaze(phases.poll());
    }

    private void initializeConfig() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.default_config);
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d("TAGTAG", "Config params updated: " + updated);
                        String configJson = firebaseRemoteConfig.getString("app_config");
                        AppConfig targetObject = new Gson().fromJson(configJson, AppConfig.class);
                        AppConfig.init(targetObject);
                        new Handler().postDelayed(() -> onPhaseFinished(CONFIG_DOWNLOAD), 5000);

                    }
                });

    }

    private void startPhaze(StartupPhase state) {
        if (state == null) {
            view.goToMainScreen();
            return;
        }
        switch (state) {
            case CONFIG_DOWNLOAD:
                if (AppConfig.getInstance() != null) {
                    startPhaze(phases.poll());
                    break;
                }
                initializeConfig();
                view.displayLoadingScreen();
                break;
            case AUTHENTICATION:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startPhaze(phases.poll());
                    break;
                }
                view.displayLoginScreen(this);
                break;
            case NICKNAME_CHOOSE:
                if (!UNKNOWN_USERNAME.equalsIgnoreCase(PeferencesUtil.readFromPrefs(context, PeferencesUtil.Preferences.USERNAME))) {
                    view.goToMainScreen();
                    break;
                }
                view.displayNicknameScreen(this);
                break;
        }
    }

    @Override
    public void onPhaseFinished(StartupPhase phase) {
        startPhaze(phases.poll());
    }

    public enum StartupPhase {
        CONFIG_DOWNLOAD,
        AUTHENTICATION,
        NICKNAME_CHOOSE
    }

    public interface ViewInterface {
        void displayLoginScreen(PhaseFinishedListener phaseFinishedListener);

        void displayLoadingScreen();

        void displayNicknameScreen(PhaseFinishedListener phaseFinishedListener);

        void goToMainScreen();
    }
}
