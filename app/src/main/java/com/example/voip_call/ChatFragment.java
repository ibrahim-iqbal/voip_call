package com.example.voip_call;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;

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
        db = FirebaseDatabase.getInstance().getReference("chatinfo");
        userEmail = mauth.getCurrentUser().getEmail();

        recyclerView = v.findViewById(R.id.recic);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        Toast.makeText(context, ""+mauth.getCurrentUser().get, Toast.LENGTH_SHORT).show();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        userlist = new ArrayList<>();

        r_db.orderByChild("email").equalTo(mauth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
//                    String name = datas.child("name").getValue().toString();
//                    String email = datas.child("email").getValue().toString();
//                    String imgurl = datas.child("imgurl").getValue().toString();
                    id = datas.child("id").getValue().toString();
//                    u_name.setText(name);
//                    u_email.setText(email);
//                    if (imgurl.equals("")) {
//                        imgurl = "jjd";
//                    } else {
//                        Picasso.get().load(imgurl).into(profileimg);
//                    }
//                    Toast.makeText(context, "" + id, Toast.LENGTH_SHORT).show();
                    userlist.clear();
                    chatuser(id);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void chatuser(final String id) {

        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot da : dataSnapshot.getChildren()) {
                    String chatid = da.getKey();
//                    Toast.makeText(context, ""+chatid, Toast.LENGTH_SHORT).show();
                    Query query2 = db.child(id).child(chatid);
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int no = 0;
                            for (DataSnapshot unda : dataSnapshot.getChildren()) {
                                if (Integer.parseInt(unda.child("unread").getValue().toString()) >= 0) {
                                    no++;
                                }
                            }
                            Toast.makeText(context, "unread" + no, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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

    public void getuserinfo(final String userid, String massage, long tm) {

        final String mass = massage;
        final long tmm = tm;
        r_db.orderByKey().equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userda : dataSnapshot.getChildren()) {
                    name = userda.child("name").getValue().toString();
                    img = userda.child("imgurl").getValue().toString();
                    Id = userda.child("id").getValue().toString();
                    userlist.add(new UserinfoList(img, name, mass, Id, tmm));
                }

//                sort list according to sending and receiving massage
                Collections.sort(userlist, new Comparator<UserinfoList>() {
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
                madpter = new Chat_RecyclerView_Adpter(context, userlist);
                recyclerView.setAdapter(madpter);
//                madpter.notifyItemChanged(1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
