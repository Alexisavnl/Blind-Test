package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlayGameActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    int timerValue = 30;
    RoundedHorizontalProgressBar progressBar;
    private ArrayList<ChoiceAnswer> choiceAnswers;
    private ChoiceAnswer choiceAnswer;
    private Artist selectedArtist;
    private ArrayList<Track> selectedTracks;
    private ArrayList<Track> listOfAllTracks;
    private TextView optionA, optionB, optionC, optionD, point;
    private CardView cardA, cardB, cardC, cardD;
    private ImageView bPlay, bPause, cover;
    MediaPlayer mediaPlayer;
    private LinearLayout nextbtn;
    private int index=0;
    private int correctCount=0;
    private int totalTime=0;
    private Score score;


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
        point =findViewById(R.id.score);

        cardA=findViewById(R.id.cardA);
        cardB=findViewById(R.id.cardB);
        cardC=findViewById(R.id.cardC);
        cardD=findViewById(R.id.cardD);

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setClickable(false);

        cover=findViewById(R.id.cover);
        bPlay=findViewById(R.id.btnplay);
        bPause=findViewById(R.id.btnpause);
        mediaPlayer = new MediaPlayer();
        getListTrack();
        startTimer();


    }

    private void startTimer(){
        timerValue=30;
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
        countDownTimer=new CountDownTimer(timerValue*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue--;
                System.out.println("temps restant "+timerValue);
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

        point.setText(correctCount + " Pts");
        optionA.setText(choiceAnswer.getcA().getTitle());
        optionB.setText(choiceAnswer.getcB().getTitle());
        optionC.setText(choiceAnswer.getcC().getTitle());
        optionD.setText(choiceAnswer.getcD().getTitle());
    }


    private void generateQuestion(){

        ArrayList<Track> tracksAlreadySelected = new ArrayList<>();
        for (int i=0; i<10; i++){
            Track[] tmpSelectedTracks = new Track[3];
            ArrayList<Track> temporyList = new ArrayList<>(listOfAllTracks);
            int answer = (int) (Math.random() * temporyList.size());
            tmpSelectedTracks[0] = temporyList.remove(answer);
            int answer2 = (int) (Math.random() * temporyList.size());
            tmpSelectedTracks[1] = temporyList.remove(answer2);
            int answer3 = (int) (Math.random() * temporyList.size());
            tmpSelectedTracks[2] = temporyList.remove(answer3);


            int randomPosition = (int) (Math.random() * 4) +1;
            switch (randomPosition){
                case 1 :
                    choiceAnswers.add(new ChoiceAnswer(selectedTracks.get(i),tmpSelectedTracks[0],tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i)));
                    break;
                case 2 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],selectedTracks.get(i),tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i)));
                    break;
                case 3 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],tmpSelectedTracks[1],selectedTracks.get(i),tmpSelectedTracks[2],selectedTracks.get(i)));
                    break;
                case 4 :
                    choiceAnswers.add(new ChoiceAnswer(tmpSelectedTracks[0],tmpSelectedTracks[1],tmpSelectedTracks[2],selectedTracks.get(i),selectedTracks.get(i)));
                    break;
            }
            System.out.println("bonne reponse pour "+i+" est "+choiceAnswers.get(i).getCorrectAnswer().getTitle());
        }
        choiceAnswer= choiceAnswers.get(index);
        play_audio(choiceAnswer.getCorrectAnswer().getPreview());
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
                            String cover_small;
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                title = jsonObject1.getString("title");
                                preview = jsonObject1.getString("preview");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("album");
                                cover_small = jsonObject2.getString("cover");
                                System.out.println(jsonObject2);
                                if(title != null && preview != null){
                                    listOfAllTracks.add(new Track(title,preview,cover_small));
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
            selectedTracks.add(listOfAllTracks.remove(random));
        }
        generateQuestion();
    }

    private void correct(CardView card){
        card.setBackgroundColor(getResources().getColor(R.color.green));
        try {
            downloadImage(choiceAnswer.getCorrectAnswer(), cover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<choiceAnswers.size()-1) {
                    countDownTimer.cancel();
                    totalTime+= 30 - timerValue;
                    correctCount++;
                    index++;
                    mediaPlayer.pause();
                    choiceAnswer = choiceAnswers.get(index);
                    displayAnswer();
                    resetColor();
                    enableBtn();
                    startTimer();
                    cover.setImageResource(0);
                    play_audio(choiceAnswer.getCorrectAnswer().getPreview());
                } else {
                    GameWon();
                }

            }
        });
    }

    private void wrong(CardView card){

        card.setBackgroundColor(getResources().getColor(R.color.red));
        try {
            downloadImage(choiceAnswer.getCorrectAnswer(), cover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<choiceAnswers.size()-1){
                    countDownTimer.cancel();
                    totalTime+= 30 - timerValue;
                    index++;
                    mediaPlayer.pause();
                    choiceAnswer= choiceAnswers.get(index);
                    displayAnswer();
                    enableBtn();
                    resetColor();
                    startTimer();
                    cover.setImageResource(0);
                    play_audio(choiceAnswer.getCorrectAnswer().getPreview());
                }else {
                    GameWon();
                }
            }
        });

    }

    private void GameWon() {

        Intent intent = new Intent(PlayGameActivity.this, WonActivity.class);
        score = new Score("alexis", correctCount, totalTime);
        intent.putExtra("score", score);
        intent.putExtra("collectionName", selectedArtist.getName());
        startActivity(intent);
    }

    private void resetColor(){
        cardA.setBackgroundColor(getResources().getColor(R.color.white));
        cardB.setBackgroundColor(getResources().getColor(R.color.white));
        cardC.setBackgroundColor(getResources().getColor(R.color.white));
        cardD.setBackgroundColor(getResources().getColor(R.color.white));

    }
    private void enableBtn(){
        nextbtn.setClickable(false);
        cardA.setClickable(true);
        cardB.setClickable(true);
        cardC.setClickable(true);
        cardD.setClickable(true);

    }

    private void disableBtn(){
        cardA.setClickable(false);
        cardB.setClickable(false);
        cardC.setClickable(false);
        cardD.setClickable(false);
    }

    public void optionClickA(View view) {
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcA().equals(choiceAnswer.getCorrectAnswer())){
            if(index<choiceAnswers.size()-1){
                correct(cardA);
            } else{
                GameWon();
            }
        } else {
            wrong(cardA);
        }
    }

    public void optionClickB(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcB().equals(choiceAnswer.getCorrectAnswer())){
            if(index<choiceAnswers.size()-1){
                correct(cardB);
            } else{
                GameWon();
            }
        } else {
            wrong(cardB);
        }
    }

    public void optionClickC(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcC().equals(choiceAnswer.getCorrectAnswer())){
            if(index<choiceAnswers.size()-1){
                correct(cardC);
            } else{
                GameWon();
            }
        } else {
            wrong(cardC);
        }
    }

    public void optionClickD(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcD().equals(choiceAnswer.getCorrectAnswer())){
            if(index<choiceAnswers.size()-1){
                correct(cardD);
            } else{
                GameWon();
            }
        } else {
            wrong(cardD );
        }
    }

    private void play_audio(String URLPreview){

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.reset();
                    System.out.println("url passer en parametre "+URLPreview);
                    mediaPlayer.setDataSource(URLPreview);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //send message to handler
            }
        });
        th.start();
    }

    public void clickPause(View view) {
        bPause.setVisibility(View.INVISIBLE);
        bPlay.setVisibility(View.VISIBLE);
        mediaPlayer.pause();
    }

    public void clickPlay(View view) {
        bPause.setVisibility(View.VISIBLE);
        bPlay.setVisibility(View.INVISIBLE);
        mediaPlayer.start();
    }

    private void downloadImage(Track track, ImageView imageView) throws MalformedURLException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(track.getCover()).getContent());
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}