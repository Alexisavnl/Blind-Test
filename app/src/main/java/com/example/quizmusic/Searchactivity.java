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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Searchactivity extends AppCompatActivity {

    private EditText editText;
    private static HttpURLConnection connection;
    private ListView listView;
    private Button buttonGenerate;
    private List<Artist> artistsList;
    private Artist selectedArtist = null;

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
                //System.out.println(s);
                if(s.isEmpty()){
                    selectedArtist=null;
                    buttonGenerate.setVisibility(listView.getRootView().INVISIBLE);
                    displayList(new ArrayList<>());
                    return;
                }
                getListTrack(s);

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
                                Intent intent = new Intent(getApplicationContext(), Play.class);
                                startActivity(intent);
                            }

                            case 1: {
                                Intent intent = new Intent(getApplicationContext(), Play.class);
                                startActivity(intent);
                            }

                            case 2: {
                                Intent intent = new Intent(getApplicationContext(), Play.class);
                                startActivity(intent);
                            }
                            case 3: {
                                Intent intent = new Intent(getApplicationContext(), Play.class);
                                startActivity(intent);
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

    private void getListTrack(String s) {

        RequestQueue requestQueue;
        artistsList.clear();
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.deezer.com/search/artist?q="+s;
        //https://api.deezer.com/artist/27/top?limit=50

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
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

                                //System.out.println("id "+id+" name "+name);
                                artistsList.add(new Artist(id,name, cover, nb_fan));
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
    }

    private void displayList(List<Artist> artistsList){
        ListAdapter adapter = new ListAdapter(this,R.layout.list_item, artistsList);
        listView.setAdapter(adapter);
    }

}

