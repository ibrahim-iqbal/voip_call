package com.example.voip_call;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.model.SliderPage;

public class OnBoarding extends AppIntro implements ISlideBackgroundColorHolder {
    ConstraintLayout cons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cons = findViewById(R.id.cons);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Hello");
        sliderPage1.setDescription("Welcome to the App");
        sliderPage1.setImageDrawable(R.drawable.googleg_standard_color_18);
        sliderPage1.setBgColor(R.color.DarkKhaki);
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Hello");
        sliderPage2.setDescription("Welcome to the App");
        sliderPage2.setImageDrawable(R.drawable.googleg_standard_color_18);
        sliderPage2.setBgColor(R.color.CornflowerBlue);
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Toast.makeText(this, "Skip Pressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#000000");
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        cons.setBackgroundColor(backgroundColor);
    }
}
