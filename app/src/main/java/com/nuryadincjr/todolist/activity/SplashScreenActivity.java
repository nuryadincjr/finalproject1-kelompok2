package com.nuryadincjr.todolist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.content.Intent;
import com.nuryadincjr.todolist.R;
import com.nuryadincjr.todolist.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        transition();
    }

    private void transition() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 1000);
    }
}