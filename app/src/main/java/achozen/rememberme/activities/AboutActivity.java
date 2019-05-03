package achozen.rememberme.activities;

import android.os.Bundle;
import android.widget.TextView;

import achozen.rememberme.BuildConfig;
import achozen.rememberme.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Achozen on 2016-02-27.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView version = findViewById(R.id.version);
        version.setText("App version: " + BuildConfig.VERSION_NAME);
    }
}
