package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class PlayGameActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    int timerValue = 30;
    RoundedHorizontalProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        progressBar=findViewById(R.id.blind_test_timer);

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
}