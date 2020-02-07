package com.example.voip_call;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView wel;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wel = findViewById(R.id.well);
        mp = MediaPlayer.create(this, R.raw.intro_melody);
        wel.animate().alpha(0.8f).translationY(50f).setDuration(2000);

        Handler h = new Handler();
        h.postDelayed(() ->
        {
            mp.stop();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }, 4000);
    }
}