package achozen.rememberme.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
        requestForAds();
        TextView version = findViewById(R.id.version);
        version.setText("App version: " + BuildConfig.VERSION_NAME);
    }

    private void requestForAds() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
