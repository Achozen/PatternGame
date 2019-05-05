package achozen.rememberme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import achozen.rememberme.R;
import achozen.rememberme.engine.PreferencesUtil;
import achozen.rememberme.firebase.statistics.model.Score;
import achozen.rememberme.statistics.GameStatistics;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static achozen.rememberme.activities.HighScoresActivity.ScoreFragment.HIGH_SCORES_DATABASE;

/**
 * Created by Achozen on 2016-02-27.
 */
public class StatisticsFragment extends Fragment {

    @BindView(R.id.progressLayout)
    View progressLayout;

    @BindView(R.id.confirmationButton)
    View confirmationButton;

    @BindView(R.id.gameScoreTextView)
    TextView gameScoreTextView;

    @BindView(R.id.totalGameTimeValue)
    TextView gameTimeTextView;

    @BindView(R.id.levelPassedNumber)
    TextView levelsPassedTextView;

    @BindView(R.id.rankingPositionValue)
    TextView rankingPosition;

    @BindView(R.id.rankingPositionLabel)
    TextView rankingPositionLabel;

    private GameStatistics gameStatistics;
    private FirebaseUser user;
    private Score currentScore;
    Mode displayMode;

    public static StatisticsFragment getInstanceForGameEnd(final GameStatistics statistics) {
        final StatisticsFragment fragment = new StatisticsFragment();
        fragment.displayMode = Mode.GAME_END;
        fragment.gameStatistics = statistics;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (displayMode == Mode.GAME_END) {
            currentScore = getCurrentGameScore();
            startGameEndInitialization();
        } else {
            startHighScoresInitialization();
        }

    }

    private void startHighScoresInitialization() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(HIGH_SCORES_DATABASE);
        final Query queryRef = databaseReference.orderByChild("score");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryRef.removeEventListener(this);
                Log.d("TAGTAGS", "onDataChange");
                final ArrayList<Score> allScores = new ArrayList<>();
                final long itemsCount = dataSnapshot.getChildrenCount();
                Score myScore = null;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Score serverScore = messageSnapshot.getValue(Score.class);
                    allScores.add(serverScore);
                    if (serverScore != null && serverScore.email.equalsIgnoreCase(user.getEmail())) {
                        myScore = serverScore;
                        Log.d("TAGTAGS", "Removing event listener");
                        long currentPosition = itemsCount - (allScores.size() - 1);
                        rankingPosition.setText(String.valueOf(currentPosition));
                        progressLayout.setVisibility(View.GONE);
                        confirmationButton.setVisibility(View.GONE);
                        setupViews(String.valueOf(serverScore.score), formatTime(serverScore.gameTime), String.valueOf(serverScore.maxLevel));
                        break;
                    }
                }
                if (myScore == null) {
                    rankingPositionLabel.setText("UNRANKED");
                    progressLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Score getCurrentGameScore() {
        final String username = PreferencesUtil.readFromPrefs(getContext(), PreferencesUtil.Preferences.USERNAME);
        final Score score = new Score();
        score.email = user.getEmail();
        score.score = gameStatistics.getScoredPoints();
        score.name = username;
        score.maxLevel = gameStatistics.getLevelFinishedCounter();
        score.gameTime = gameStatistics.getGameTime();
        return score;
    }

    @OnClick(R.id.confirmationButton)
    void onConfirmationButtonClicked(final View v) {
        getActivity().finish();
    }

    private void startGameEndInitialization() {
        Log.d("TAGTAGS", "startGameEndInitialization");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(HIGH_SCORES_DATABASE);
        final Query queryRef = databaseReference.orderByChild("score");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
/*
                Log.d("TAGTAGS", "onDataChange");
                GenericTypeIndicator<List<Score>> t = new GenericTypeIndicator<List<Score>>() {};

                List<Score> allScores = dataSnapshot.getChildren(t
               // final ArrayList<Score> allScores = new ArrayList<>();
              //  Collections.reverse(allScores);
*/
                List<Score> allScores = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Score serverScore = messageSnapshot.getValue(Score.class);
                    allScores.add(serverScore);
                }
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Score serverScore = messageSnapshot.getValue(Score.class);
                    if (serverScore != null && serverScore.email.equalsIgnoreCase(user.getEmail())) {
                        Log.d("TAGTAGS", "Removing event listener");
                        queryRef.removeEventListener(this);
                        long currentPosition = 0;
                        int bestScore = serverScore.score;
                        if (currentScore.score > serverScore.score) {
                            Log.d("TAGTAGS", "Setting value for better score");
                            bestScore = currentScore.score;
                            messageSnapshot.getRef().setValue(currentScore);
                            rankingPositionLabel.setText("Congratulations - new record:");
                        }
                        currentPosition = getPosition(bestScore, allScores);
                        rankingPosition.setText(String.valueOf(currentPosition));
                        progressLayout.setVisibility(View.GONE);
                        confirmationButton.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                Log.d("TAGTAGS", "Pushing new score");
                dataSnapshot.getRef().push().setValue(currentScore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getPosition(int currentScore, List<Score> allScores) {
        int position = allScores.size() + 1;
        for (Score score : allScores) {
            if (currentScore > score.score) {
                position--;
            } else {
                return position;
            }
        }
        return position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics,
                container, false);
        ButterKnife.bind(this, view);
        if (displayMode == Mode.GAME_END) {
            setupViews(String.valueOf(gameStatistics.getScoredPoints()), formatTime(gameStatistics.getGameTime()), String.valueOf(gameStatistics.getLevelFinishedCounter()));
        }

        return view;
    }


    private void setupViews(String scoredPoints, String gameTime, String levelFinishedCount) {
        gameScoreTextView.setText(scoredPoints);
        gameTimeTextView.setText(gameTime);
        levelsPassedTextView.setText(levelFinishedCount);
    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }
}

enum Mode {
    HIGH_SCORES,
    GAME_END
}

