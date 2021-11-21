package com.example.quizmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HasMapAdapter extends BaseAdapter {

    //private HashMap<String, String> mData = new HashMap<String, String>();
    private ArrayList<HashMap<String,String>> mData = new ArrayList<>();


    public HasMapAdapter(ArrayList<HashMap<String,String>> data) {
        mData  = data;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        } else {
            result = convertView;
        }

        ImageView cover = result.findViewById(R.id.cover);
        TextView artist = result.findViewById(R.id.artistName);
        TextView followers = result.findViewById(R.id.followers);

        Picasso.get().load(getItem(position).get("picture_small")).fit().into(cover);
        artist.setText(getItem(position).get("name"));
        followers.setText(getItem(position).get("nb_fan"));

        //do your view stuff here

        return result;
    }

}
