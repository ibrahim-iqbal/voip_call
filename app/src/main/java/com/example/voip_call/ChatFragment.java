package com.example.voip_call;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    String userEmail;
    FirebaseAuth mauth;
    RecyclerView recyclerView;
    RecyclerView.Adapter madpter;
    RecyclerView.LayoutManager layoutManager;
    String id, massage, name, Id, img;
    long time;
    Context context;
    FirebaseDatabase DB;
    List<Long> tm;
    String nam, imgurl, uid;
    FirebaseRecyclerAdapter<UserinfoList, MyViewholder> adapter;
    private List<UserinfoList> userlist;
    private DatabaseReference r_db, db;

    public ChatFragment(Context mcontext) {
        // Required empty public constructor
        context = mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        mauth = FirebaseAuth.getInstance();
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {

        }
        DB = FirebaseDatabase.getInstance();

        r_db = DB.getReference("userinfo");
        r_db.keepSynced(true);
        db = DB.getReference("chatinfo");
        db.keepSynced(true);
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
        userlist.clear();
        r_db.orderByChild("email").equalTo(mauth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    id = datas.child("id").getValue().toString();

//                    chatuser(id);


                }


                FirebaseRecyclerOptions<UserinfoList> options = new FirebaseRecyclerOptions.Builder<UserinfoList>()
                        .setQuery(db.child(id), UserinfoList.class)
                        .build();
                adapter = new FirebaseRecyclerAdapter<UserinfoList, MyViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyViewholder holder, int position, @NonNull UserinfoList model) {
                        String idd = getRef(position).getKey();
                        db.child(id).child(idd).limitToLast(1).orderByKey().addValueEventListener(new ValueEventListener() {
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
                                    r_db.orderByKey().equalTo(show_userid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot ud : dataSnapshot.getChildren()) {
                                                nam = ud.child("name").getValue().toString();
                                                uid = ud.child("id").getValue().toString();
                                                imgurl = ud.child("imgurl").getValue().toString();
//                                        Toast.makeText(context, ""+massage, Toast.LENGTH_SHORT).show();
                                                userlist.add(new UserinfoList(imgurl, nam, massage, uid, time));
                                                holder.name.setText(nam);
                                                holder.userlayout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent it = new Intent(context, ChattingActivity.class);
                                                        it.putExtra("userid", uid)
                                                                .putExtra("name", name);

                                                        startActivity(it);
                                                    }

                                                });
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//                            Toast.makeText(context, ""+nam, Toast.LENGTH_SHORT).show();


                                    holder.email.setText(massage);


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.alluserlayout, parent, false);
                        return new MyViewholder(view);
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


//    public void chatuser(final String id) {
//
//
//        db.child(id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (final DataSnapshot da : dataSnapshot.getChildren()) {
//                    String chatid = da.getKey();
//                    userlist.clear();
//                    Query query = db.child(id).child(chatid).limitToLast(1).orderByKey();
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show();
//                            String show_userid="";
//                            for (DataSnapshot lchat : dataSnapshot.getChildren()) {
//                                massage = lchat.child("massage").getValue().toString();
//                                time = (long) lchat.child("time").getValue();
//                                String s_Id = lchat.child("senderId").getValue().toString();
//                                String re_Id = lchat.child("reciverId").getValue().toString();
//
//                                if (s_Id.equals(id)) {
//                                    show_userid = re_Id;
//                                } else {
//                                    show_userid = s_Id;
//                                }
//
//
//                            }
//                            getuserinfo(show_userid, massage, time);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//
//    public void getuserinfo(final String userid, String massage, long tm) {
//        LinkedHashSet<UserinfoList> uniqueStrings = new LinkedHashSet<UserinfoList>();
//        final String mass = massage;
//        final long tmm = tm;
//        userlist.clear();
//        r_db.orderByKey().equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot userda : dataSnapshot.getChildren()) {
//                    name = userda.child("name").getValue().toString();
//                    img = userda.child("imgurl").getValue().toString();
//                    Id = userda.child("id").getValue().toString();
//                    uniqueStrings.add(new UserinfoList(img, name, mass, Id, tmm));
//
//                }
//                userlist.addAll(uniqueStrings);
//                uniqueStrings.clear();
////                sort list according to sending and receiving massage
//                Collections.sort(userlist, new Comparator<UserinfoList>() {
//                    @Override
//                    public int compare(UserinfoList o1, UserinfoList o2) {
//                        int tm1 = (int) o1.getTm();
//                        int tm2 = (int) o2.getTm();
//
//                        if (tm1 < tm2) {
//                            return 1;
//                        } else {
//                            return -1;
//                        }
//
//                    }
//                });
//                madpter = new Chat_RecyclerView_Adpter(context, userlist);
//                madpter.notifyDataSetChanged();
//                recyclerView.setAdapter(madpter);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        userlist.clear();
        FirebaseDatabase.getInstance().goOnline();
    }

    @Override
    public void onPause() {
        super.onPause();
        userlist.clear();
        FirebaseDatabase.getInstance().goOffline();
        Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show();
    }

    static class MyViewholder extends RecyclerView.ViewHolder {
        de.hdodenhof.circleimageview.CircleImageView profileppic;
        TextView name, email, unread;
        LinearLayout userlayout;

        MyViewholder(@NonNull View itemView) {
            super(itemView);
            profileppic = itemView.findViewById(R.id.profilepic);
            name = itemView.findViewById(R.id.alu_name);
            email = itemView.findViewById(R.id.alu_email);
            unread = itemView.findViewById(R.id.unreadcount);
            userlayout = itemView.findViewById(R.id.userlayout);
        }
    }
}
