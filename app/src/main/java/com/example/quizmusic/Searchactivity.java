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
import android.widget.ImageView;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Searchactivity extends AppCompatActivity {

    private EditText editText;
    private static HttpURLConnection connection;
    private ListView listView;
    private Button buttonGenerate;
    ArrayList<HashMap<String,String>> artistsList;
    private HashMap<String, String> artists;
    private List<Artist> artistList;
    private Artist selectedArtist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        artistList = new ArrayList<>();
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
                System.out.println(s);
                if(s.isEmpty()){
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
                listView.invalidateViews();
            }
        });
    }

    private void getListTrack(String s) {
        //la

        final TextView textView = (TextView) findViewById(R.id.text);
        RequestQueue requestQueue;
        artistsList.clear();
        artistList.clear();
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
                                artistList.add(new Artist(id,name, cover, nb_fan));
                                artists = new HashMap<>();
                                //artists.put("id",id);
                                artists.put("name",name);
                                artists.put("picture_small",cover);
                                //artists.put("nb_fan",nb_fan);

                                artistsList.add(artists);
                            }
                            /*for (HashMap<String,String> map : artistsList){
                                for (String key : map.keySet()) {
                                    System.out.println(key + " = " + map.get(key));
                                }
                            }*/
                            displayList(artistList);

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

    private void displayList(List<Artist> artistsList){
        ListAdapter adapter = new ListAdapter(this,R.layout.list_item, artistsList);
        listView.setAdapter(adapter);
    }

}