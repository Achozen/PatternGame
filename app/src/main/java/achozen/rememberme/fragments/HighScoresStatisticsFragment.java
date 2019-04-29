package achozen.rememberme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import achozen.rememberme.R;
import butterknife.ButterKnife;

public class HighScoresStatisticsFragment extends StatisticsFragment {

    public static StatisticsFragment getInstanceForHighScores() {
        final HighScoresStatisticsFragment fragment = new HighScoresStatisticsFragment();
        fragment.displayMode = Mode.HIGH_SCORES;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.high_scores_statistics_fragment,
                container, false);
        ButterKnife.bind(this, view);

        confirmationButton.setVisibility(View.GONE);
        return view;
    }

}
