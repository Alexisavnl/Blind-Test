package com.example.quizmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WonActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Score score;
    private String collectionName;
    private TextView summary;
    private Button signOut, newParty, rank;
    private HashMap<String, Map<String, Object>> hashMap;
    private ArrayList<Score> scores;
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
            collectionName = (String) getIntent().getSerializableExtra("collectionName");
        }
        if (getIntent().hasExtra("score")){
            score = (Score) getIntent().getSerializableExtra("score");
        }

        summary.setText("Bravo tu as marqué "+score.getCorrectCount()+ " en seulement "+score.getDuration()+" secondes");
        addDataToFirestore(collectionName, score);

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
                intent.putExtra("collectionName", collectionName);
                startActivity(intent);
            }
        });

    }

    private void addDataToFirestore(String collectionName, Score score){
        db.collection(collectionName).document(score.getPseudo()).set(score);
    }

}