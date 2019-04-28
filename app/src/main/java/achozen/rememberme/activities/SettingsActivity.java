package achozen.rememberme.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.R;
import achozen.rememberme.engine.PeferencesUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends Activity {

    @BindView(R.id.inputText)
    TextInputEditText editText;

    @BindView(R.id.loggedInValue)
    TextView loggedInVaue;

    @BindView(R.id.appVersion)
    TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        final String userName = PeferencesUtil.readFromPrefs(this, PeferencesUtil.Preferences.USERNAME);
        if (!"UNKNOWN".equalsIgnoreCase(userName)) {
            editText.setText(userName);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            loggedInVaue.setText(currentUser.getEmail());
        }

        appVersion.setText("Version: " + BuildConfig.VERSION_NAME);
    }


    @OnClick(R.id.saveButton)
    void saveButtonClicked(View v) {
        if (editText.getText() != null && TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError("Username cannot be empty !");
        } else {
            PeferencesUtil.storeInPrefs(this, PeferencesUtil.Preferences.USERNAME, editText.getText().toString());
            finish();
        }
    }

    @OnClick(R.id.logoutButton)
    void onLogoutClicked(View v) {
        PeferencesUtil.storeInPrefs(this, PeferencesUtil.Preferences.USERNAME, PeferencesUtil.UNKNOWN_USERNAME);
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        Intent startupIntent = new Intent(this, SplashScreenActivity.class);
        startActivity(startupIntent);
    }
}
