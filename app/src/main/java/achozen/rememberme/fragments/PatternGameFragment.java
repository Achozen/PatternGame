package achozen.rememberme.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import achozen.rememberme.R;
import achozen.rememberme.engine.GameInitializationData;
import achozen.rememberme.engine.OnLevelFinishListener;
import achozen.rememberme.engine.PatternView;

/**
 * Created by Achozen on 2016-02-27.
 */
public class PatternGameFragment extends Fragment {
    private GameInitializationData gameInitializationData;
    private OnLevelFinishListener onLevelFinishListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == gameInitializationData || onLevelFinishListener == null) {
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
        patternView.setupView(gameInitializationData, (TextView) view.findViewById(R.id
                .notification_text_view), onLevelFinishListener);
        return view;
    }

    public void setGameInitializationData(GameInitializationData gameInitializationData) {

        this.gameInitializationData = gameInitializationData;
    }

    public void setOnLevelFinishListener(OnLevelFinishListener onLevelFinishListener) {
        this.onLevelFinishListener = onLevelFinishListener;
    }


}
