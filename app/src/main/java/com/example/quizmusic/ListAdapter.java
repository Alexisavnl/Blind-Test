package com.example.quizmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Artist> {

    private int resourceLayout;
    private Context mContext;

    public ListAdapter(Context context, int resource, List<Artist> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Artist p = getItem(position);

        if (p != null) {
            TextView artistName = (TextView) v.findViewById(R.id.artistName);
            TextView nb_followers = (TextView) v.findViewById(R.id.followers);
            ImageView cover =  v.findViewById(R.id.cover);
            ImageView buttonSelected = v.findViewById(R.id.button_check);

            if (artistName != null) {
                artistName.setText(p.getName());
            }

            if (nb_followers != null) {
                String s = p.getNb_fan() + " followers";
                nb_followers.setText(s);
            }

            if (cover != null) {
                //Picasso.get().load(p.getPicture_small()).fit().into(cover);
                try {
                    downloadImage(p, cover);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            if(buttonSelected != null){

                if (p.getSelected()){
                    buttonSelected.setVisibility(v.VISIBLE);
                }else{
                    buttonSelected.setVisibility(v.GONE);
                }
            }

            //System.out.println("nom artiste "+p.getName()+" boolean "+p.getSelected());

        }

        return v;
    }


    private void downloadImage(Artist artist, ImageView imageView) throws MalformedURLException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(artist.getURLPicture()).getContent());
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}