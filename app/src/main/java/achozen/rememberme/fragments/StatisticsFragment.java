package achozen.rememberme.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;

import achozen.rememberme.R;
import achozen.rememberme.engine.PeferencesUtil;
import achozen.rememberme.firebase.statistics.model.Score;
import achozen.rememberme.statistics.GameStatistics;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static achozen.rememberme.activities.HighScoresActivity.ScoreFragment.HIGH_SCORES_DATABASE;

/**
 * Created by Achozen on 2016-02-27.
 */
public class StatisticsFragment extends Fragment {

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

    public static StatisticsFragment getInstance(final GameStatistics statistics) {
        final StatisticsFragment fragment = new StatisticsFragment();
        fragment.gameStatistics = statistics;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        currentScore = getCurrentGameScore();
        checkScoreWithRanking();
    }

    private Score getCurrentGameScore() {
        final String username = PeferencesUtil.readFromPrefs(getContext(), PeferencesUtil.Preferences.USERNAME);
        final Score score = new Score();
        score.email = user.getEmail();
        score.score = gameStatistics.getScoredPoints();
        score.name = username;
        return score;
    }

    @OnClick(R.id.confirmationButton)
    void onConfirmationButtonClicked(final View v) {
        getActivity().finish();
    }

    private void checkScoreWithRanking() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(HIGH_SCORES_DATABASE);
        Query queryRef = databaseReference.orderByChild("score");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Score> allScores = new ArrayList<>();
                final long itemsCount = dataSnapshot.getChildrenCount();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Score serverScore = messageSnapshot.getValue(Score.class);
                    allScores.add(serverScore);
                    if (serverScore != null && serverScore.email.equalsIgnoreCase(user.getEmail())) {
                        long currentPosition = 0;
                        if (currentScore.score == serverScore.score) {
                            queryRef.removeEventListener(this);
                        } else if (currentScore.score > serverScore.score) {
                            messageSnapshot.getRef().setValue(currentScore);
                            rankingPositionLabel.setText("Congratulations - new record:");
                        }
                        currentPosition = itemsCount - (allScores.size() - 1);
                        rankingPosition.setText("" + currentPosition);
                        return;
                    }
                }
                dataSnapshot.getRef().push().setValue(currentScore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics,
                container, false);
        ButterKnife.bind(this, view);


        gameScoreTextView.setText("" + gameStatistics.getScoredPoints());
        gameTimeTextView.setText("" + gameStatistics.getGameTime());
        levelsPassedTextView.setText("" + gameStatistics.getLevelFinishedCounter());
        return view;
    }
}
