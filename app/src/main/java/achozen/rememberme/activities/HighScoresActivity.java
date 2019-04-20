package achozen.rememberme.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import achozen.rememberme.R;
import achozen.rememberme.adapter.HighScoreAdapter;
import achozen.rememberme.firebase.statistics.model.Score;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Achozen on 2016-02-27.
 */
public class HighScoresActivity extends FragmentActivity {

    public static final String TOP_100_TEXT = "TOP 100";
    public static final String MY_SCORES_TEXT = "MY SCORES";
    public static final String MY_RANING = "MY RANKING";
    public static final String SCORE_TYPE = "score_type";

    public static final int TOP100 = 0, MY_RANKING = 1, MY_SCORES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scoress);
        ScoresPagerAdapter mDemoCollectionPagerAdapter = new ScoresPagerAdapter(
                getSupportFragmentManager(), getApplicationContext());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class ScoresPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        public ScoresPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ScoreFragment();
            Bundle args = new Bundle();
            args.putInt(SCORE_TYPE, i);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TOP100:
                    return context.getString(R.string.top_100_tab_title);
                case MY_RANKING:
                    return context.getString(R.string.my_ranking_tab_title);
                case MY_SCORES:
                    return context.getString(R.string.my_scores_tab_title);
            }
            return "";
        }
    }

    public static class ScoreFragment extends Fragment implements ValueEventListener {
        public static final String HIGH_SCORES_DATABASE = "high_scores";
        View rootView;
        RecyclerView mRecyclerView;
        HighScoreAdapter mAdapter;
        List<Score> highScores = new ArrayList<>();
        private DatabaseReference databaseReference;

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_high_scores, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.highScoresRecyclerView);
            createDBConnection();
            setupRecyclerView();


            Bundle args = getArguments();
            int scoreType = args.getInt(SCORE_TYPE);
            switch (scoreType) {
                case TOP100:
                    prepareTop100Fragment();
                    break;
                case MY_RANKING:
                    prepareMyRankingFragment(ScoreFragment.this);
                    break;
                case MY_SCORES:
                    prepareMyScoresFragment();
                    break;
            }

            return rootView;
        }

        private void setupRecyclerView() {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            mAdapter = new HighScoreAdapter(new ArrayList<>());
            mRecyclerView.setAdapter(mAdapter);
        }

        private void prepareTop100Fragment() {
            ((TextView) rootView.findViewById(R.id.score_header)).setText(TOP_100_TEXT);

            Query myTopPostsQuery = databaseReference.orderByChild("score").limitToLast(100);
            myTopPostsQuery.addListenerForSingleValueEvent(this);
        }

        private void prepareMyScoresFragment() {
            ((TextView) rootView.findViewById(R.id.score_header)).setText(MY_SCORES_TEXT);
        }

        private void prepareMyRankingFragment(ScoreFragment fragment) {
            ((TextView) rootView.findViewById(R.id.score_header)).setText(MY_RANING);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            String email = "";
            if (user != null) {
                email = user.getEmail();
            }
            Log.d("TAGTAG", "email " + email);
            Query myTopPostsQuery = databaseReference.orderByChild("email").equalTo(email);
            myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot == null) {
                        return;
                    }
                    Score myScore = null;
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        myScore = messageSnapshot.getValue(Score.class);
                    }
                    if (myScore == null) {
                        return;
                    }
                    Query myTopPostsQuery = databaseReference.orderByChild("score").startAt(myScore.score).limitToLast(5);
                    myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null) {
                                return;
                            }
                            highScores.clear();
                            for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                Score score = messageSnapshot.getValue(Score.class);
                                highScores.add(score);
                            }

                            Collections.reverse(highScores);
                            mAdapter.swap(highScores);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void createDBConnection() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference(HIGH_SCORES_DATABASE);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("TAGTAG", " onDataChange: " + dataSnapshot.getValue().toString());
            highScores.clear();
            for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                Score score = messageSnapshot.getValue(Score.class);
                highScores.add(score);
            }

            Collections.reverse(highScores);
            mAdapter.swap(highScores);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
