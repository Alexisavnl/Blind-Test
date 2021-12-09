package com.example.quizmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        System.out.println("je suis dans le won j'ai recup "+collectionName+" et le score "+score.getPseudo());
        addDataToFirestore(collectionName, score);
    }

    private void addDataToFirestore(String collectionName, Score score){
        db.collection(collectionName).add(score).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}