package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class EasyModeActivity extends AppCompatActivity {

    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private TextView textView;
    private String artistName;
    private int artistId;
    private Artist selectedArtist;
    private ArrayList<Track> tracksList;
    private ArrayList<Track> trackSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);
        textView = findViewById(R.id.textViewArtistName);

        tracksList = new ArrayList<>();
        if (getIntent().hasExtra("Artist")){
            selectedArtist = (Artist) getIntent().getSerializableExtra("Artist");

        }
        textView.setText(selectedArtist.getName());
        getListTrack();
    }

    private void getListTrack() {

        RequestQueue requestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.deezer.com/artist/"+selectedArtist.getId()+"/top?limit=100";
        System.out.println(url);
        //

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String title;
                            String preview;
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                title = jsonObject1.getString("title");
                                preview = jsonObject1.getString("preview");
                                if(title != null && preview != null){
                                    tracksList.add(new Track(title,preview));
                                    System.out.println("avant avant" +tracksList.size());
                                }

                            }
                            select10Tracks(tracksList);
                            displayOfChoices();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }


    private void select10Tracks(ArrayList<Track> tracksList){
        trackSelected = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            int random = (int) (Math.random() * tracksList.size());
            System.out.println(tracksList.size());
            trackSelected.add(tracksList.remove(random));
            System.out.println(trackSelected.size());
        }
    }

   private void displayOfChoices(){
       // int random = (int) (Math.random() * 3)+1;
       for (int i = 1; i <11;i++)  {
           int random = 1;
           ArrayList<Track> temporaryList = trackSelected;
           System.out.println("taille de la liste"+trackSelected.size());
           Button correctResponse;
           switch (random){
               case 1 :
                   choice1.setText(trackSelected.get(i).getTitle());
                   choice2.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice3.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice4.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   correctResponse = choice1;
                   break;
               case 2 :

                   choice1.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice2.setText(trackSelected.get(i).getTitle());
                   choice3.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice4.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   correctResponse = choice2;
                   break;
               case 3 :
                   choice1.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice2.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice3.setText(trackSelected.get(i).getTitle());
                   choice4.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   correctResponse = choice3;
                   break;
               default:
                   choice1.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice2.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice2.setText(temporaryList.remove(randomValue(temporaryList)).getTitle());
                   choice4.setText(trackSelected.get(i).getTitle());
                   correctResponse = choice4;
                   break;
           }

           while (isItGood(correctResponse)){
               isItGood(correctResponse);
           }


       }

   }

   private boolean isItGood(Button correctResponse){
        if (choice1 != correctResponse){
            choice1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            return false;
        } else if(choice2!= correctResponse) {
            choice2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            return false;
        } else if (choice3 != correctResponse){
           choice3.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {

               }
           });
           return false;
       } else if(choice4!= correctResponse) {
           choice4.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v) {

               }
           });
           return false;
       } else {
            correctResponse.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            return true;
        }
   }

   private int randomValue(ArrayList<Track> temporaryList){
       System.out.println(temporaryList.size());
        int randomTitle = (int) (Math.random() * temporaryList.size());
        return randomTitle;
   }


}