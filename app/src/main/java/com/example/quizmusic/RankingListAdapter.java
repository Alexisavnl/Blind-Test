package com.example.quizmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RankingListAdapter extends ArrayAdapter<Score> {

    private int resourceLayout, iteration=0;
    private Context mContext;
    private ArrayList<Score> scores;

    public RankingListAdapter(Context context, int resource, List<Score> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.scores = (ArrayList<Score>) items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        iteration++;

        View v = convertView;

        System.out.println("taille de la lis "+scores.size());
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Score s = getItem(position);

        if (s != null) {
            TextView pseudo = (TextView) v.findViewById(R.id.artistName);
            TextView registeredScore = (TextView) v.findViewById(R.id.followers);
            ImageView cover =  v.findViewById(R.id.cover);
            cover.setVisibility(View.GONE);
            TextView rankingPosition = v.findViewById(R.id.rankingNumber);
            ImageView buttonSelected = v.findViewById(R.id.button_check);
            rankingPosition.setVisibility(View.VISIBLE);
            buttonSelected.setVisibility(View.GONE);
            if (pseudo != null) {
                pseudo.setText(s.getPseudo());
            }

            if (registeredScore != null) {
                registeredScore.setText("Nb bonne réponse : "+s.getCorrectCount()+ " en "+s.getDuration()+" secondes");
            }

            if (rankingPosition != null) {
                String i = Integer.toString(position+1);
                System.out.println("le i "+i);
                rankingPosition.setText(i);
            }

        }

        return v;
    }

}
