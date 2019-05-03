package achozen.rememberme.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
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
import achozen.rememberme.analytics.AnalyticEvent;
import achozen.rememberme.firebase.statistics.model.Score;
import achozen.rememberme.fragments.HighScoresStatisticsFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Achozen on 2016-02-27.
 */
public class HighScoresActivity extends AppCompatActivity {

    public static final String TOP_100_TEXT = "TOP 100";

    public static final int TOP100 = 0, MY_SCORES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        ScoresPagerAdapter scoresPageAdapter = new ScoresPagerAdapter(
                getSupportFragmentManager(), getApplicationContext());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(scoresPageAdapter);
        AnalyticEvent.highScoresDisplayed();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == TOP100) {
                    AnalyticEvent.highScoresDisplayed();
                } else if (position == MY_SCORES) {
                    AnalyticEvent.myScoreDisplayed();
                }
            }
        });

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
            Fragment fragment = null;
            switch (i) {
                case TOP100:
                    fragment = new ScoreFragment();
                    break;
                case MY_SCORES:
                    fragment = HighScoresStatisticsFragment.getInstanceForHighScores();
                    break;
            }


            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TOP100:
                    return context.getString(R.string.top_100_tab_title);
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
            rootView = inflater.inflate(R.layout.fragment_high_scores, container, false);
            mRecyclerView = rootView.findViewById(R.id.highScoresRecyclerView);
            createDBConnection();
            setupRecyclerView();
            prepareTop100Fragment();
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
