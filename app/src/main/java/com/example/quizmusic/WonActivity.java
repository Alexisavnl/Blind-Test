package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WonActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Score score;
    private String documentName;
    private TextView summary;
    private Button newParty, rank;
    private ArrayList<Score> scores;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        db = FirebaseFirestore.getInstance();
        newParty = findViewById(R.id.btnNewParty);
        rank=findViewById(R.id.btnRank);
        summary = findViewById(R.id.summary);

        scores= new ArrayList<>();

        if (getIntent().hasExtra("collectionName")){
            documentName = (String) getIntent().getSerializableExtra("collectionName");
        }
        if (getIntent().hasExtra("score")){
            score = (Score) getIntent().getSerializableExtra("score");
        }
        if (getIntent().hasExtra("Mode")){
            gameMode = (String) getIntent().getStringExtra("Mode");
        }
        if(gameMode.equals("classic")){
            db.collection("classic").document(documentName).set(score);
            String s = "Bravo tu as marqué "+score.getCorrectCount()+ " point(s) en seulement "+score.getDuration()+" secondes";
            summary.setText(s);

        } else{
            score.setDuration(30);
            db.collection("flash").document(documentName).set(score);
            String s = "Bravo tu as reconnu "+score.getCorrectCount()+ " musiques en 60 secondes";
            summary.setText(s);

        }


        newParty.setOnClickListener(v -> startActivity(new Intent(WonActivity.this, Searchactivity.class)));

        rank.setOnClickListener(v -> {
            Intent intent = new Intent(WonActivity.this, RankingActivity.class);
            intent.putExtra("collectionName", documentName);
            intent.putExtra("Mode",gameMode);
            startActivity(intent);
        });
    }
}