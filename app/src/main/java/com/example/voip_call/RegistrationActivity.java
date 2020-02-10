package com.example.voip_call;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class RegistrationActivity extends AppCompatActivity {
    TextInputEditText email, password, name;
    String id, pass, n;
    DatabaseReference db;
    long maxid = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.nameid);

        db = FirebaseDatabase.getInstance().getReference("userinfo");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    //REGISTER A NEW USER
    public void reg(View v) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCanceledOnTouchOutside(false);

        id = Objects.requireNonNull(email.getText()).toString().trim();
        pass = Objects.requireNonNull(password.getText()).toString().trim();
        n = Objects.requireNonNull(name.getText()).toString().trim();
        if (n.isEmpty()) {
            name.setError("empty");
            name.requestFocus();
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                assert vibrator != null;
                vibrator.vibrate(100);
            } else
                Toast.makeText(RegistrationActivity.this, Build.VERSION.SDK_INT + "", Toast.LENGTH_SHORT).show();
        } else if (id.isEmpty()) {
            email.setError("empty");
            email.requestFocus();

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                assert vibrator != null;
                vibrator.vibrate(100);
            } else
                Toast.makeText(RegistrationActivity.this, Build.VERSION.SDK_INT + "", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            password.setError("empty");
            password.requestFocus();

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                assert vibrator != null;
                vibrator.vibrate(100);
            } else
                Toast.makeText(RegistrationActivity.this, Build.VERSION.SDK_INT + "", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            mAuth.createUserWithEmailAndPassword(id, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(String.valueOf(this), "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        createuser(n, user != null ? user.getEmail() : null, "");
                        pd.dismiss();
                        Intent it = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(it);
                        finish();
                    } else {
                        pd.dismiss();
                        Log.w(String.valueOf(this), "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void cancel(View view) {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    public void createuser(String name, String email, String imgurl) {
        db.push();
        Createuser user = new Createuser(email, String.valueOf(maxid + 1), imgurl, name);
        db.child(String.valueOf(maxid + 1)).setValue(user);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
