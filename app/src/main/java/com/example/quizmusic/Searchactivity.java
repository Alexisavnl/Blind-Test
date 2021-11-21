package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    ArrayList<HashMap<String,String>> artistsList;
    private HashMap<String, String> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        artistsList = new ArrayList<>();
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);

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
                    display(new ArrayList<>());
                    return;
                }
                getListTrack(s);

            }
        });
    }

    private void getListTrack(String s) {
        //la

        final TextView textView = (TextView) findViewById(R.id.text);
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


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String name, id, cover, nb_fan;
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                id = jsonObject1.getString("id");
                                name = jsonObject1.getString("name");
                                cover = jsonObject1.getString("picture_small");
                                nb_fan = jsonObject1.getString("nb_fan");

                                //System.out.println("id "+id+" name "+name);

                                artists = new HashMap<>();
                                artists.put("id",id);
                                artists.put("name",name);
                                artists.put("picture_small",cover);
                                artists.put("nb_fan",nb_fan);

                                artistsList.add(artists);
                            }
                            /*for (HashMap<String,String> map : artistsList){
                                for (String key : map.keySet()) {
                                    System.out.println(key + " = " + map.get(key));
                                }
                            }*/
                            display(artistsList);

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


    private void display(ArrayList<HashMap<String,String>> artistsList){
        HasMapAdapter adapter = new HasMapAdapter(artistsList);
        listView.setAdapter(adapter);
    }
}