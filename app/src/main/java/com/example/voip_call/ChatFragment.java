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

public class ChatFragment extends Fragment {
    String userEmail;
    FirebaseAuth mauth;
    RecyclerView recyclerView;
    RecyclerView.Adapter madpter;
    RecyclerView.LayoutManager layoutManager;
    List<UserinfoList> userlist;
    DatabaseReference r_db, db;
    String id, massage, name, Id, img;
    long time;
    Context context;

    public ChatFragment(Context mcontext) {
        // Required empty public constructor
        context = mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mauth = FirebaseAuth.getInstance();
        r_db = FirebaseDatabase.getInstance().getReference("userinfo");
        userEmail=mauth.getCurrentUser().getEmail();

        recyclerView = v.findViewById(R.id.recic);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

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
                        userlist.add(new UserinfoList(img, name, email));
                    }
                }
                madpter = new Chat_RecyclerView_Adpter(Objects.requireNonNull(getContext()), userlist);
                recyclerView.setAdapter(madpter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
