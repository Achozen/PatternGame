package achozen.rememberme.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.R;

/**
 * Created by Achozen on 2016-02-27.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView version = findViewById(R.id.version);
        version.setText("App version: " + BuildConfig.VERSION_NAME);
    }
}
