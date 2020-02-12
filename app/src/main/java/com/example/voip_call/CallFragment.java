package com.example.voip_call;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

public class CallFragment extends Fragment {
    List<UserinfoList> usercalllist;
    String userEmail, id, massage, name, img, Id;
    long time;
    FirebaseAuth mauth;
    DatabaseReference r_db1, db;
    RecyclerView.Adapter madpter;
    RecyclerView review;
    String chatid;
    ValueEventListener listener;
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
        r_db1 = FirebaseDatabase.getInstance().getReference("userinfo");
        db = FirebaseDatabase.getInstance().getReference("chatinfo");
        userEmail = mauth.getCurrentUser().getEmail();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        review.setLayoutManager(layoutManager);

        Window window = getActivity().getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDarker));

        r_db1.orderByChild("email").equalTo(mauth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
//
                    id = datas.child("id").getValue().toString();

                    chatuser(id);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


//        Toast.makeText(context, "call start", Toast.LENGTH_SHORT).show();


        usercalllist = new ArrayList<>();
        usercalllist.clear();


    }

    private void chatuser(final String id) {

        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot da : dataSnapshot.getChildren()) {
                    chatid = da.getKey();
//                    Toast.makeText(context, ""+chatid, Toast.LENGTH_SHORT).show();
                    Query query = db.child(id).child(chatid).limitToLast(1).orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot lchat : dataSnapshot.getChildren()) {
                                massage = lchat.child("massage").getValue().toString();
                                time = (long) lchat.child("time").getValue();
                                String s_Id = lchat.child("senderId").getValue().toString();
                                String re_Id = lchat.child("reciverId").getValue().toString();
                                String show_userid;
                                if (s_Id.equals(id)) {
                                    show_userid = re_Id;
                                } else {
                                    show_userid = s_Id;
                                }
//                                Toast.makeText(context, "jhjk" + show_userid, Toast.LENGTH_SHORT).show();
                                getuserinfo(show_userid, massage, time);


//                                Toast.makeText(context, "" + show_userid, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(context, "last" + massage, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void getuserinfo(String userid, String massage, long tm) {
        LinkedHashSet<UserinfoList> uniqueStrings = new LinkedHashSet<UserinfoList>();
        String uid = userid;
        final String mass = massage;
        final long tmm = tm;
        usercalllist.clear();
        r_db1.orderByKey().equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userda : dataSnapshot.getChildren()) {
                    name = userda.child("name").getValue().toString();
                    img = userda.child("imgurl").getValue().toString();
                    Id = userda.child("id").getValue().toString();
                    String email = userda.child("email").getValue().toString();
                    uniqueStrings.add(new UserinfoList(img, name, Id, email));
                }
                usercalllist.addAll(uniqueStrings);

//                sort list according to sending and receiving massage
                Collections.sort(usercalllist, new Comparator<UserinfoList>() {
                    @Override
                    public int compare(UserinfoList o1, UserinfoList o2) {
                        int tm1 = (int) o1.getTm();
                        int tm2 = (int) o2.getTm();

                        if (tm1 < tm2) {
                            return 1;
                        } else {
                            return -1;
                        }

                    }
                });
                madpter = new RecyclerAdapter(context, usercalllist);
                madpter.notifyDataSetChanged();
                review.setAdapter(madpter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
//        Toast.makeText(context, "atop", Toast.LENGTH_SHORT).show();

    }


}
