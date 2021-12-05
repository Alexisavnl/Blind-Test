package com.example.quizmusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class Searchactivity extends AppCompatActivity {

    private EditText editText;
    private static HttpURLConnection connection;
    private ListView listView;
    private Button buttonGenerate;
    private ArrayList<Artist> artistsList;
    public Artist selectedArtist = null;
    final static public String ARTIST_NAME_KEY = "ARTISTNAME";
    final static public String ARTIST_ID_KEY = "ARTISTID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        artistsList = new ArrayList<>();
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        buttonGenerate = findViewById(R.id.button_generate);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.isEmpty()){
                    selectedArtist=null;
                    buttonGenerate.setVisibility(listView.getRootView().INVISIBLE);
                    displayList(new ArrayList<>());
                    return;
                }

                displayList(getListTrack(s));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                buttonGenerate.setVisibility(view.VISIBLE);
                if(selectedArtist!=null){
                    selectedArtist.isSelected(false);
                }
                selectedArtist = (Artist) listView.getItemAtPosition(position);
                selectedArtist.isSelected(true);
                listView.invalidateViews(); //Refresh list
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Searchactivity.this);
                builder.setCancelable(true);
                builder.setTitle("Sélectionner la difficulté :");
                String[] difficulty = {"facile", "moyen", "difficile", "turboDifficile", "annuler"};
                builder.setItems(difficulty, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                Intent intent = new Intent(Searchactivity.this, PlayGameActivity.class);
                                intent.putExtra("Artist", selectedArtist);
                                //intent.putExtra("ArtistsList", artistsList);
                                startActivity(intent);
                            }

                            case 1: {

                            }

                            case 2: {

                            }
                            case 3: {

                            }
                            case 4: {

                            }
                        }
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    private List<Artist> getListTrack(String s) {


        RequestQueue requestQueue;
        artistsList.clear();

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.deezer.com/search/artist?q="+s;
        
        //https://api.deezer.com/artist/27/top?limit=50

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String name;
                            int id;
                            String cover;
                            int nb_fan;
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                id = jsonObject1.getInt("id");
                                name = jsonObject1.getString("name");
                                cover = jsonObject1.getString("picture_small");
                                nb_fan = jsonObject1.getInt("nb_fan");
                                artistsList.add(new Artist(id,name, cover, nb_fan));
                                System.out.println("debut");
                                System.out.println(artistsList.get(i).getName());
                                System.out.println("fin");
                            }
                            displayList(artistsList);

                        }catch (JSONException | MalformedURLException e){
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
        return artistsList;
    }



    private void displayList(List<Artist> artistsList){
        ListAdapter adapter = new ListAdapter(this,R.layout.list_item, artistsList);
        listView.setAdapter(adapter);
    }

    public Artist getSelectedArtist() {
        return selectedArtist;
    }
}

