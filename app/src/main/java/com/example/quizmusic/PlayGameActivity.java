package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    private int totalTime=0;
    private ArrayList<ChoiceAnswer> choiceAnswers;
    private ChoiceAnswer choiceAnswer;
    private Artist selectedArtist;
    private ArrayList<Track> selectedTracks;
    private ArrayList<Track> listOfAllTracks;
    private TextView optionA, optionB, optionC, optionD, point;
    private CardView cardA, cardB, cardC, cardD;
    private ImageView bPlay, bPause, cover;
    MediaPlayer mediaPlayer;
    private RelativeLayout nextbtn;
    private int index=0;
    private int correctCount=0;
    private Score score;
    private Vibrator vibe;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        if (getIntent().hasExtra("Artist")){
            selectedArtist = (Artist) getIntent().getSerializableExtra("Artist");
        }
        if (getIntent().hasExtra("Mode")){
            gameMode = (String) getIntent().getStringExtra("Mode");
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
        vibe = (Vibrator) PlayGameActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        getListTrack();
        startTimer();
    }

    private void startTimer(){
        if(countDownTimer!=null && gameMode!="flash") { countDownTimer.cancel(); }
        countDownTimer=new CountDownTimer((timerValue* 1000L),1000) {
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
                dialog.findViewById(R.id.btn_timeout).setOnClickListener(v -> {
                    if(gameMode=="classic"){
                        prepareNextQuestion();
                        dialog.dismiss();
                    } else{
                        GameWon();
                        dialog.dismiss();
                    }

                });
                dialog.show();
            }
        }.start();
    }

    private void getListTrack(){
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.deezer.com/artist/"+selectedArtist.getId()+"/top?limit=100";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                String title,preview,cover_small;
                for(int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    title = jsonObject1.getString("title");
                    preview = jsonObject1.getString("preview");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("album");
                    cover_small = jsonObject2.getString("cover");
                    listOfAllTracks.add(new Track(title,preview,cover_small));
                }
                selectTracks();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, error -> System.out.println("That didn't work!"));
        queue.add(stringRequest);
    }

    private void selectTracks(){
        selectedTracks = new ArrayList<>();
        int iteration=0;
        if (gameMode.equals("classic")){
            iteration=10;
        } else{
            iteration=listOfAllTracks.size();
        }
        System.out.println("dans le selectTracks "+iteration);
        for(int i = 0; i < iteration;i++){
            int random = (int) (Math.random() * listOfAllTracks.size());
            selectedTracks.add(listOfAllTracks.get(random));
        }
        generateQuestion();
    }

    private void generateQuestion(){
        int iteration=0;
        System.out.println("generate "+gameMode);
        if(gameMode.equals("classic")){
            iteration=10;
        } else {
            iteration=listOfAllTracks.size();
        }
        System.out.println("dans le generate "+iteration);
        for (int i=0; i<iteration; i++){
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
        }
        choiceAnswer= choiceAnswers.get(index);
        play_audio(choiceAnswer.getCorrectAnswer().getPreview());
        displayAnswer();
    }

    private void displayAnswer() {
        String s = correctCount + " Pts";
        point.setText(s);
        optionA.setText(choiceAnswer.getcA().getTitle());
        optionB.setText(choiceAnswer.getcB().getTitle());
        optionC.setText(choiceAnswer.getcC().getTitle());
        optionD.setText(choiceAnswer.getcD().getTitle());
    }

    private void correct(CardView card){
        card.setBackgroundColor(getResources().getColor(R.color.green));
        bPause.setVisibility(View.INVISIBLE);
        bPlay.setVisibility(View.INVISIBLE);
        if(gameMode.equals("classic")) {
            countDownTimer.cancel();
        }
        try {
            downloadImage(choiceAnswer.getCorrectAnswer(), cover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        nextbtn.setOnClickListener(v -> {
            if(index<choiceAnswers.size()-1) {
                correctCount++;
                prepareNextQuestion();
            } else {
                GameWon();
            }
        });
    }

    private void wrong(CardView card){
        vibe.vibrate(80);
        if (card!=null){card.setBackgroundColor(getResources().getColor(R.color.red));}
        bPause.setVisibility(View.INVISIBLE);
        bPlay.setVisibility(View.INVISIBLE);
        if(gameMode.equals("classic")) {
            countDownTimer.cancel();
        }
        try {
            downloadImage(choiceAnswer.getCorrectAnswer(), cover);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        nextbtn.setOnClickListener(v -> {
            if(index<choiceAnswers.size()-1){
                prepareNextQuestion();
            }else {
                GameWon();
            }
        });
    }

    private void prepareNextQuestion(){
        totalTime+= 30 - timerValue;
        index++;
        mediaPlayer.pause();
        choiceAnswer= choiceAnswers.get(index);
        displayAnswer();
        bPause.setVisibility(View.VISIBLE);
        enableBtn();
        resetColor();
        cover.setImageResource(0);
        play_audio(choiceAnswer.getCorrectAnswer().getPreview());
        if(gameMode.equals("classic")) {
            timerValue=30;
            System.out.println("je relzance le timer ");
            startTimer();
        }
    }

    private void GameWon() {
        mediaPlayer.pause();
        Intent intent = new Intent(PlayGameActivity.this, WonActivity.class);
        final String[] pseudo = new String[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(PlayGameActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setTitle("Tape ton pseudo");
        alert.setCancelable(false);
        alert.setView(input);

        alert.setPositiveButton("envoyer", (dialog, whichButton) -> {
            pseudo[0] = input.getText().toString();
            score = new Score(pseudo[0], correctCount, totalTime, selectedArtist.getName());
            intent.putExtra("score", score);
            intent.putExtra("collectionName", selectedArtist.getName());
            intent.putExtra("Mode",gameMode);
            startActivity(intent);
        });
        alert.show();
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
            correct(cardA);
        } else {
            wrong(cardA);
        }
    }

    public void optionClickB(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcB().equals(choiceAnswer.getCorrectAnswer())){
            correct(cardB);
        } else {
            wrong(cardB);
        }
    }

    public void optionClickC(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcC().equals(choiceAnswer.getCorrectAnswer())){
            correct(cardC);
        } else {
            wrong(cardC);
        }
    }

    public void optionClickD(View view){
        disableBtn();
        nextbtn.setClickable(true);
        if(choiceAnswer.getcD().equals(choiceAnswer.getCorrectAnswer())){
            correct(cardD);
        } else {
            wrong(cardD );
        }
    }

    private void play_audio(String URLPreview){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Thread th=new Thread(() -> {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(URLPreview);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            } catch (Exception e) {
                e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}