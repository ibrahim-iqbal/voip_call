package com.example.voip_call;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CallFragment extends Fragment {
    List<UserinfoList> userlist;
    String userEmail;
    FirebaseAuth mauth;
    DatabaseReference r_db;
    RecyclerView.Adapter madpter;
    RecyclerView review;
    private Context context;

    public CallFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_call, container, false);
        review = v.findViewById(R.id.review);
        mauth = FirebaseAuth.getInstance();
        r_db = FirebaseDatabase.getInstance().getReference("userinfo");
        userEmail = mauth.getCurrentUser().getEmail();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        review.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        r_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = Objects.requireNonNull(data.child("name").getValue()).toString();
                    String img = Objects.requireNonNull(data.child("imgurl").getValue()).toString();
                    String email = Objects.requireNonNull(data.child("email").getValue()).toString();

                    if (email.equals(userEmail)) {
                        img = "lkk";
                    } else {
//                        userlist.add(new UserinfoList(img, name, email));
                    }
                }
                madpter = new RecyclerAdapter(Objects.requireNonNull(getContext()), userlist);
                review.setAdapter(madpter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
