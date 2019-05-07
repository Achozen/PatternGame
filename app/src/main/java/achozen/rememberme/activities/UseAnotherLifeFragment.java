package achozen.rememberme.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import achozen.rememberme.R;
import achozen.rememberme.engine.PreferencesUtil;
import achozen.rememberme.interfaces.GameProgressListener;
import achozen.rememberme.statistics.GameStatistics;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UseAnotherLifeFragment extends Fragment {

    @BindView(R.id.livesCount)
    TextView livesCount;

    @BindView(R.id.question)
    TextView questionLabel;

    @BindView(R.id.livesAvailableLabel)
    TextView livesAvailableLabel;

    @BindView(R.id.buttonContinue)
    Button buttonContinue;

    @BindView(R.id.buttonNo)
    Button buttonNo;
    long livesCounter = 0;

    private GameStatistics gameStatistics;
    private GameProgressListener listener;

    public static UseAnotherLifeFragment getInstance(final GameStatistics statistics, GameProgressListener listener) {
        final UseAnotherLifeFragment fragment = new UseAnotherLifeFragment();
        fragment.gameStatistics = statistics;
        fragment.listener = listener;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use_another_life,
                container, false);
        ButterKnife.bind(this, view);

        PreferencesUtil.readFromPrefs(getContext(), PreferencesUtil.LongPreferences.LIVES_COUNT);
        livesCounter = PreferencesUtil.readFromPrefs(getContext(), PreferencesUtil.LongPreferences.LIVES_COUNT);
        livesCount.setText(String.valueOf(livesCounter));
        if (livesCounter > 0) {
            setupViewForLivesAvailable();
        } else {
            setupViewForAdDisplay();
        }
        buttonNo.setOnClickListener(v -> listener.onRankedFinished(gameStatistics));
        return view;
    }

    private void setupViewForLivesAvailable() {
        questionLabel.setText(R.string.lives_available_description);
        buttonContinue.setOnClickListener(v -> {
            removeLife();
            listener.resumeLostGame(gameStatistics);
        });
    }

    private void setupViewForAdDisplay() {
        questionLabel.setText(R.string.no_lives_available_description);
        buttonContinue.setOnClickListener(v -> listener.displayRevenueAd(gameStatistics));
    }

    private void removeLife() {
        PreferencesUtil.storeInPrefs(getContext(), PreferencesUtil.LongPreferences.LIVES_COUNT, livesCounter - 1);
        PreferencesUtil.storeInPrefs(getContext(), PreferencesUtil.LongPreferences.LIVES_UPDATE_TIMESTAMP, System.currentTimeMillis());
    }
}
