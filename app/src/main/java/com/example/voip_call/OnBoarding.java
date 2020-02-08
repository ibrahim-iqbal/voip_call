package com.example.voip_call;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class OnBoarding extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Hello");
        sliderPage.setDescription("Welcomwe to the App");
        sliderPage.setImageDrawable(R.drawable.googleg_standard_color_18);
        sliderPage.setBgColor(R.color.BlueViolet);
        addSlide(AppIntroFragment.newInstance(sliderPage));


        Handler h = new Handler();
        h.postDelayed(() ->
        {
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
            finish();
        }, 5000);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
