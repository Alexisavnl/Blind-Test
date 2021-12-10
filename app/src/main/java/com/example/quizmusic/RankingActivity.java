package com.example.quizmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    private String collectionName;
    private static final String TAG = "RankingActivity";
    private List<Score> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        listView = findViewById(R.id.listView);
        db = FirebaseFirestore.getInstance();
        scores= new ArrayList<>();

        if (getIntent().hasExtra("collectionName")){
            collectionName = (String) getIntent().getSerializableExtra("collectionName");
        }

        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                scores.add(document.toObject(Score.class));
                                System.out.println("taille de la liste en desouus delle "+scores.size());

                            }displayList(scores);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    private void displayList(List<Score> scoresList){
        System.out.println("avant"+scoresList.toString());
        Collections.sort(scoresList);
        System.out.println(scoresList.toString());
        RankingListAdapter adapter = new RankingListAdapter(this,R.layout.list_item, scoresList);
        listView.setAdapter(adapter);
    }

}