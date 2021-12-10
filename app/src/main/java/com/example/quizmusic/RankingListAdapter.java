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

    private int resourceLayout;
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


        View v = convertView;

        System.out.println("taille de la lis "+scores.size());
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Score score = getItem(position);

        if (score != null) {
            TextView pseudo = (TextView) v.findViewById(R.id.artistName);
            TextView registeredScore = (TextView) v.findViewById(R.id.followers);
            ImageView cover =  v.findViewById(R.id.cover);
            cover.setVisibility(View.GONE);
            TextView rankingPosition = v.findViewById(R.id.rankingNumber);
            ImageView buttonSelected = v.findViewById(R.id.button_check);
            rankingPosition.setVisibility(View.VISIBLE);
            buttonSelected.setVisibility(View.GONE);
            if (pseudo != null) {
                pseudo.setText(score.getPseudo());
            }

            if (registeredScore != null) {
                String s = "Nb bonne réponse : "+score.getCorrectCount()+ " en "+score.getDuration()+" secondes";
                registeredScore.setText(s);
            }


            String i = Integer.toString(position+1);
            rankingPosition.setText(i);

        }

        return v;
    }

}
