package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    private String documentName;
    private static final String TAG = "RankingActivity";
    private List<Score> scores;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listView = findViewById(R.id.listView);
        db = FirebaseFirestore.getInstance();
        scores= new ArrayList<>();

        if (getIntent().hasExtra("collectionName")){
            documentName = (String) getIntent().getSerializableExtra("collectionName");
        }

        if (getIntent().hasExtra("Mode")){
            gameMode = (String) getIntent().getStringExtra("Mode");
        }

        System.out.println("non de du docu "+documentName);
        System.out.println();
        db.collection(gameMode).whereEqualTo("artist", documentName)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId()+ " => " + document.getData());
                            scores.add(document.toObject(Score.class));
                            System.out.println(document.toString());
                        } displayList(scores);
                        System.out.println("taille de la liste "+scores.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void displayList(List<Score> scoresList){
        Collections.sort(scoresList);
        RankingListAdapter adapter = new RankingListAdapter(this,R.layout.list_item, scoresList);
        listView.setAdapter(adapter);
    }

}