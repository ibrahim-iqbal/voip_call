package com.example.voip_call;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    String email;
    private Context mContext;

    PagerAdapter(Context context, FragmentManager fm, String email) {
        super(fm);
        mContext = context;
        this.email = email;
    }

    // This determines the fragment for each tab
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CallFragment(mContext);
        } else if (position == 1) {

            return new ChatFragment(mContext);
        } else {
            return new ProfileFragment(mContext);
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.call);
            case 1:
                return mContext.getString(R.string.chat);
            case 2:
                return mContext.getString(R.string.profile);
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
