package achozen.rememberme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import achozen.rememberme.R;
import achozen.rememberme.firebase.statistics.model.Score;

/**
 * Created by Achozen on 2016-12-02.
 */

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {
    private List<Score> scoreList;

    public HighScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public HighScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LinearLayout v = (LinearLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.high_score_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.placeColumnTextView.setText(String.valueOf(position+1));
        holder.nameColumnTextView.setText(scoreList.get(position).name);
        holder.scoreColumnTextView.setText(String.valueOf(scoreList.get(position).score));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public void swap(List<Score> scoreList){
        this.scoreList.clear();
        this.scoreList.addAll(scoreList);
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeColumnTextView;
        private TextView nameColumnTextView;
        private TextView scoreColumnTextView;
        ViewHolder(LinearLayout v) {
            super(v);
            placeColumnTextView = (TextView) v.findViewById(R.id.place_column);
            nameColumnTextView = (TextView) v.findViewById(R.id.name_column);
            scoreColumnTextView = (TextView) v.findViewById(R.id.score_column);
        }
    }
}
