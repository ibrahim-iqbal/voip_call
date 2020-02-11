package com.example.voip_call;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
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

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

        mp = MediaPlayer.create(this, R.raw.intro_melody);
        wel.animate().alpha(0.8f).translationY(100f).scaleY(1f).scaleX(1f).setDuration(2000);

        Handler h = new Handler();
        h.postDelayed(() ->
        {
            mp.stop();
            SharedPreferences sp = getSharedPreferences("onboard", MODE_PRIVATE);
            int is = sp.getInt("id", 0);
            if (is == 1) {
                Intent it = new Intent(this, LoginActivity.class);
                startActivity(it);
                finish();
            } else {
                Intent it = new Intent(this, OnBoarding.class);
                startActivity(it);
                finish();
            }
        }, 4000);
    }
}