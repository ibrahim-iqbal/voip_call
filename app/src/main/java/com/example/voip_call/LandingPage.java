package com.example.voip_call;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class LandingPage extends AppCompatActivity {
    TabLayout tabs;
    ViewPager pagerv;
    Depth depth;
    String ids;
    PagerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pagerv = findViewById(R.id.pagerv);
        tabs = findViewById(R.id.tabs);

        Intent it = getIntent();
        ids = it.getStringExtra("id");
        depth = new Depth();

        adapter = new PagerAdapter(this, getSupportFragmentManager(), ids);
        pagerv.setAdapter(adapter);
        pagerv.setPageTransformer(true, depth);
        tabs.setupWithViewPager(pagerv);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item3:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("You Want to Exit?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", (dialog, ids) -> {
                    SharedPreferences sa = LandingPage.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                    sa.edit().remove("id").apply();
                    Intent it = new Intent(LandingPage.this, MainActivity.class);
                    LandingPage.this.startActivity(it);
                    Toast.makeText(LandingPage.this, "Thank you for Login In!", Toast.LENGTH_SHORT).show();
                });

                builder1.setNegativeButton("No", (dialog, ids) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You Want to Exit?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> finish());

        builder1.setNegativeButton
                (
                        "No",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
