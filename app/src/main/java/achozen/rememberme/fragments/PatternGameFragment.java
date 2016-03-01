package achozen.rememberme.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.engine.PatternView;

/**
 * Created by Achozen on 2016-02-27.
 */
public class PatternGameFragment extends Fragment {
    private GameInitializationData gameInitializationData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == gameInitializationData) {
            return;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pattern_game,
                container, false);
        //lets keep a reference of DrawView
        PatternView patternView = (PatternView ) view.findViewById(R.id.patternView);
        patternView.setupView(gameInitializationData);
        return view;
    }

    public void setGameInitializationData(GameInitializationData gameInitializationData) {

        this.gameInitializationData = gameInitializationData;
    }


}
