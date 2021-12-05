package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PlayGameActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    int timerValue = 30;
    RoundedHorizontalProgressBar progressBar;
    private ArrayList<ChoiceAnswer> choiceAnswers;
    private Artist selectedArtist;
    private ArrayList<Track> selectedTracks;
    private ArrayList<Track> listOfAllTracks;
    private TextView optionA, optionB, optionC, optionD;
    private CardView cardA, cardB, cardC, cardD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        if (getIntent().hasExtra("Artist")){
            selectedArtist = (Artist) getIntent().getSerializableExtra("Artist");
        }

        listOfAllTracks = new ArrayList<>();
        choiceAnswers = new ArrayList<>();
        progressBar=findViewById(R.id.blind_test_timer);
        optionA=findViewById(R.id.card_optionA);
        optionB=findViewById(R.id.card_optionB);
        optionC=findViewById(R.id.card_optionC);
        optionD=findViewById(R.id.card_optionD);

        cardA=findViewById(R.id.cardA);
        cardB=findViewById(R.id.cardB);
        cardC=findViewById(R.id.cardC);
        cardD=findViewById(R.id.cardD);

        getListTrack();


        countDownTimer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue--;
                progressBar.setProgress(timerValue);
            }

            @Override
            public void onFinish() {
                System.out.println("its finish");
                Dialog dialog = new Dialog(PlayGameActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setContentView(R.layout.time_out_dialog);
                dialog.findViewById(R.id.btn_timeout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
            }
        }.start();
    }

    private void displayAnswer() {
        ChoiceAnswer test = choiceAnswers.get(0);
        optionA.setText(test.getcA());
        optionB.setText(test.getcB());
        optionC.setText(test.getcC());
        optionD.setText(test.getcD());
    }


    private void generateQuestion(){

        ArrayList<Track> tracksAlreadySelected = new ArrayList<>();
        for (int i=0; i<10; i++){
            String[] tmpSelectedTracks = new String[3];
            ArrayList<Track> temporyList = new ArrayList<>(listOfAllTracks);
            int answer = (int) Math.random() * temporyList.size();
            tmpSelectedTracks[0] = temporyList.remove(answer).getTitle();
            int answer2 = (int) Math.random() * temporyList.size();
            tmpSelectedTracks[1] = temporyList.remove(answer2).getTitle();
            int answer3 = (int) Math.random() * temporyList.size();
            tmpSelectedTracks[2] = temporyList.remove(answer3).getTitle();
            System.out.println("resultat tableau "+tmpSelectedTracks[0]);
            System.out.println("resultat tableau "+tmpSelectedTracks[1]);
            System.out.println("resultat tableau "+tmpSelectedTracks[2]);
            System.out.println("list "+selectedTracks.get(i).getTitle());
            System.out.println("list "+selectedTracks.get(i).getTitle());


            int randomPosition = (int) (Math.random() * 3) +1;
            switch (randomPosition){
                case 1 :
                    choiceAnswers.add(new ChoiceAnswer(selectedTracks.get(i).getTitle(),tmpSelectedTracks[0],tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i).getTitle()));
                    break;
                case 2 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],selectedTracks.get(i).getTitle(),tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i).getTitle()));
                    break;
                case 3 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],tmpSelectedTracks[1],selectedTracks.get(i).getTitle(),tmpSelectedTracks[2],selectedTracks.get(i).getTitle()));
                    break;
                case 4 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i).getTitle(),selectedTracks.get(i).getTitle()));
                    break;
            }
        }
        displayAnswer();
    }

    private void getListTrack(){
        RequestQueue requestQueue;

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);

        Network network = new BasicNetwork(new HurlStack());

        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.deezer.com/artist/"+selectedArtist.getId()+"/top?limit=100";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String title;
                            String preview;
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                title = jsonObject1.getString("title");
                                preview = jsonObject1.getString("preview");
                                System.out.println("juste avant le if");
                                if(title != null && preview != null){
                                    listOfAllTracks.add(new Track(title,preview));
                                    System.out.println("taille de la liste "+listOfAllTracks.size());
                                }

                            }
                            select10Tracks();

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

    private void select10Tracks(){
        selectedTracks = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            int random = (int) (Math.random() * listOfAllTracks.size());
            System.out.println("avant le get" +listOfAllTracks.size());
            selectedTracks.add(listOfAllTracks.remove(random));
            System.out.println("apres le get" +listOfAllTracks.size());
        }
        generateQuestion();
    }
}