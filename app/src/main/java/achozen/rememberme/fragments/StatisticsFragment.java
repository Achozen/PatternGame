package achozen.rememberme.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import achozen.rememberme.R;
import achozen.rememberme.statistics.GameStatistics;

/**
 * Created by Achozen on 2016-02-27.
 */
public class StatisticsFragment extends Fragment {
    private GameStatistics gameStatistics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(gameStatistics != null){


        }
    }

    public void setStatistics(GameStatistics gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics,
                container, false);

        return view;
    }
}
