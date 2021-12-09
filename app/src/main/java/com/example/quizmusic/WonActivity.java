package com.example.quizmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class WonActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Score score;
    private String collectionName;
    private static final String TAG = "WonActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        db = FirebaseFirestore.getInstance();

        if (getIntent().hasExtra("collectionName")){
            collectionName = (String) getIntent().getSerializableExtra("collectionName");
        }
        if (getIntent().hasExtra("score")){
            score = (Score) getIntent().getSerializableExtra("score");
        }

        addDataToFirestore(collectionName, score);
        getRanking();
    }

    private void addDataToFirestore(String collectionName, Score score){
        db.collection(collectionName).document(score.getPseudo()).set(score);
    }

    private void getRanking(){
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}