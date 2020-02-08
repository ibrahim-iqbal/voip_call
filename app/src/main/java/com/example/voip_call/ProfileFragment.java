package com.example.voip_call;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import tourguide.tourguide.TourGuide;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfileFragment extends Fragment {
    de.hdodenhof.circleimageview.CircleImageView profileimg;
    EditText etname, etemail;
    ImageView camera, gallery;
    AlertDialog alertDialog;
    Button save, update;
    Bitmap bitmap;
    String email, name;
    TextView cgpass;
    Context context;
    DatabaseReference db;
    FirebaseAuth mAuth;
    TourGuide tourGuide;

    ProfileFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profileimg = v.findViewById(R.id.profileimg);
        etname = v.findViewById(R.id.etname);
        etemail = v.findViewById(R.id.etemail);

        Window window = getActivity().getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDarker));

        etname.setEnabled(false);
        etemail.setEnabled(false);

        db = FirebaseDatabase.getInstance().getReference("userinfo");
        mAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dp : dataSnapshot.getChildren()) {
                    name = Objects.requireNonNull(dp.child("name").getValue()).toString();
                    etname.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        etemail.setText(email);

        return v;
    }
}
