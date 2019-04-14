package achozen.rememberme.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.engine.OnLevelFinishListener;
import achozen.rememberme.engine.PatternView;
import androidx.fragment.app.Fragment;

/**
 * Created by Achozen on 2016-02-27.
 */
public class PatternGameFragment extends Fragment {

    private GameInitializationData gameInitializationData;
    private OnLevelFinishListener onLevelFinishListener;
    private PatternView patternView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == gameInitializationData || onLevelFinishListener == null) {
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_pattern_game,
                container, false);
        //lets keep a reference of DrawView
        patternView = view.findViewById(R.id.patternView);
        patternView.setupView(gameInitializationData, view.findViewById(R.id
                .notification_text_view), onLevelFinishListener);
        return view;
    }

    public void setGameInitializationData(GameInitializationData gameInitializationData) {

        this.gameInitializationData = gameInitializationData;
    }

    public void setOnLevelFinishListener(OnLevelFinishListener onLevelFinishListener) {
        this.onLevelFinishListener = onLevelFinishListener;
    }

    public void onGamePaused() {
        patternView.pauseGame();
    }

    public void onGameResumed() {
        patternView.resumeGame();
    }
}
