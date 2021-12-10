package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WonActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Score score;
    private String documentName;
    private TextView summary;
    private Button signOut, newParty, rank;
    private HashMap<String, Map<String, Object>> hashMap;
    private ArrayList<Score> scores;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        db = FirebaseFirestore.getInstance();
        signOut = findViewById(R.id.btnDisconnect);
        newParty = findViewById(R.id.btnNewParty);
        rank=findViewById(R.id.btnRank);
        summary = findViewById(R.id.summary);

        hashMap= new HashMap<String, java.util.Map<String, Object>>();
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
            summary.setText("Bravo tu as marqué "+score.getCorrectCount()+ " point(s) en seulement "+score.getDuration()+" secondes");

        } else{
            score.setDuration(30);
            db.collection("flash").document(documentName).set(score);
            summary.setText("Bravo tu as reconnu "+score.getCorrectCount()+ " musiques en 60 secondes");

        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(WonActivity.this, LoginActivity.class));
            }
        });

        newParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WonActivity.this, Searchactivity.class));
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WonActivity.this, RankingActivity.class);
                intent.putExtra("collectionName", documentName);
                intent.putExtra("Mode",gameMode);
                startActivity(intent);
            }
        });
    }
}