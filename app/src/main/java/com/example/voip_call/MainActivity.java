package com.example.voip_call;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
	ImageView wel;
	MediaPlayer mp;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wel = findViewById(R.id.well);
		text = findViewById(R.id.text);

		Window window = this.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

		mp = MediaPlayer.create(this, R.raw.intro_melody);
		wel.animate().alpha(1f).translationY(20f).scaleY(2.5f).scaleX(2.5f).setDuration(2000);
		mp.start();

		Handler h = new Handler();
		h.postDelayed(() ->
		{
			mp.stop();
			SharedPreferences sp = getSharedPreferences("onboard", MODE_PRIVATE);
			int is = sp.getInt("id", 0);
			if (is == 1) {
				Intent it = new Intent(this, LoginActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
			} else {
				Intent it = new Intent(this, OnBoarding.class);
				startActivity(it);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				finish();
			}
		}, 3000);
	}
}