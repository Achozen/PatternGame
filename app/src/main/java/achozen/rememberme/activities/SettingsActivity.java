package achozen.rememberme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import achozen.rememberme.R;
import achozen.rememberme.engine.PeferencesUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends Activity {

    @BindView(R.id.inputText)
    TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        final String userName = PeferencesUtil.readFromPrefs(this, PeferencesUtil.Preferences.USERNAME);
        if (!"UNKNOWN".equalsIgnoreCase(userName)) {
            editText.setText(userName);
        }
    }

    @OnClick(R.id.saveButton)
    void saveButtonClicked(View v) {
        PeferencesUtil.storeInPrefs(this, PeferencesUtil.Preferences.USERNAME, editText.getText().toString());
        finish();
    }
}
