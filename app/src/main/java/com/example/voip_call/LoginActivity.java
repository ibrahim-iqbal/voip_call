package com.example.voip_call;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailid, password;
    String id, pass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        int is = sp.getInt("id", 0);
        String email = sp.getString("email", null);
        if (is == 1) {
            Intent it = new Intent(LoginActivity.this, LandingPage.class);
            it.putExtra("id", email);
            startActivity(it);
            finish();
        }
    }

    //VALIDATE THE USER LOGIN
    public void login(View v) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setProgress(100);
        pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
        pd.setCanceledOnTouchOutside(false);
        id = emailid.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (id.isEmpty()) {
            emailid.setError("Empty");
            emailid.requestFocus();
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                assert vibrator != null;
                vibrator.vibrate(100);
            } else
                Toast.makeText(LoginActivity.this, Build.VERSION.SDK_INT + "", Toast.LENGTH_SHORT).show();
        } else {
            if (pass.isEmpty()) {
                password.setError("Empty");
                password.requestFocus();

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                    assert vibrator != null;
                    vibrator.vibrate(100);
                } else
                    Toast.makeText(LoginActivity.this, Build.VERSION.SDK_INT + "", Toast.LENGTH_SHORT).show();
            } else {
                pd.show();
                mAuth.signInWithEmailAndPassword(id, pass).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;

                        Toast.makeText(LoginActivity.this, "Authenticated :", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                        Intent it = new Intent(LoginActivity.this, LandingPage.class);
                        it.putExtra("id", user.getEmail());
                        startActivity(it);
                        finish();

                        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit().putInt("id", 1).putString("email", user.getEmail());
                        ed.apply();
                    } else {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void register(View view) {
        Intent it = new Intent(this, RegistrationActivity.class);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }
}