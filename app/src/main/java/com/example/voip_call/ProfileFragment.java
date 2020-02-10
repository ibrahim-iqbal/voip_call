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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    RecyclerView recycle;
    DatabaseReference db;
    FirebaseRecyclerAdapter<alluserinfo, alluserViewholder> adapter;
    RecyclerView.LayoutManager layoutManager;
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
        recycle = v.findViewById(R.id.contactView);
        layoutManager = new LinearLayoutManager(context);
        recycle.setLayoutManager(layoutManager);

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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<alluserinfo> options = new FirebaseRecyclerOptions.Builder<alluserinfo>()
                .setQuery(db, alluserinfo.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<alluserinfo, alluserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull alluserViewholder holder, int position, @NonNull alluserinfo model) {
                String userid = getRef(position).getKey();

                db.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("name")) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            String email = dataSnapshot.child("email").getValue().toString();
                            if (mAuth.getCurrentUser().getEmail().equals(email)) {
                            } else {
                                holder.all_name.setText(name);
                                holder.all_email.setText(email);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public alluserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allusserlayout, parent, false);
                return new alluserViewholder(view);
            }
        };
        recycle.setAdapter(adapter);
        adapter.startListening();
    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

    public static class alluserViewholder extends RecyclerView.ViewHolder {

        TextView all_name, all_email;

        public alluserViewholder(@NonNull View itemView) {
            super(itemView);
            all_name = itemView.findViewById(R.id.all_name);
            all_email = itemView.findViewById(R.id.all_email);
        }
    }


}
