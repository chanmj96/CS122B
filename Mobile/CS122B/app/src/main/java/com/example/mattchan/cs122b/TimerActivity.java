package com.example.mattchan.cs122b;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    public static final long DISCONNECT_TIMEOUT = 300000; // 5 minutes

    private static Handler disconnectHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message m){
            return true;
        }
    });

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run(){
            String msg = "Idle Timeout. User redirected to Login Page...";
            Intent goToIntent = new Intent(TimerActivity.this, MainActivity.class);
            goToIntent.putExtra("timeout", msg);
            startActivity(goToIntent);
        }
    };

    public void resetTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTimer();
    }

}
