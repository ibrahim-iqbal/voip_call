package com.example.voip_call;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class CallFragment extends Fragment {
	private List<UserinfoList> usercalllist;
	private String userEmail, id, massage, name, img, Id;
	private long time;
	private DatabaseReference r_db1, db;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView review;
	private String chatid;
	private Context context;

	CallFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_call, container, false);
		review = v.findViewById(R.id.review);
		FirebaseAuth mauth = FirebaseAuth.getInstance();
		r_db1 = FirebaseDatabase.getInstance().getReference("userinfo");
		db = FirebaseDatabase.getInstance().getReference("chatinfo");
		userEmail = Objects.requireNonNull(mauth.getCurrentUser()).getEmail();
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
		review.setLayoutManager(layoutManager);

		Window window = Objects.requireNonNull(getActivity()).getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDarker));

		r_db1.orderByChild("email").equalTo(mauth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot datas : dataSnapshot.getChildren()) {
//
					id = Objects.requireNonNull(datas.child("id").getValue()).toString();

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
		Toast.makeText(context, "call start", Toast.LENGTH_SHORT).show();
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
								time = (long) lchat.child("time").getValue();
								massage = Objects.requireNonNull(lchat.child("massage").getValue()).toString();
								String s_Id = Objects.requireNonNull(lchat.child("senderId").getValue()).toString();
								String re_Id = Objects.requireNonNull(lchat.child("reciverId").getValue()).toString();
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

	private void getuserinfo(String userid, String massage, long tm) {
		LinkedHashSet<UserinfoList> uniqueStrings = new LinkedHashSet<>();
		String uid = userid;
		final String mass = massage;
		final long tmm = tm;
		usercalllist.clear();
		r_db1.orderByKey().equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot userda : dataSnapshot.getChildren()) {
					name = Objects.requireNonNull(userda.child("name").getValue()).toString();
					img = Objects.requireNonNull(userda.child("imgurl").getValue()).toString();
					Id = Objects.requireNonNull(userda.child("id").getValue()).toString();
					String email = Objects.requireNonNull(userda.child("email").getValue()).toString();
					uniqueStrings.add(new UserinfoList(img, name, Id, email));
				}
				usercalllist.addAll(uniqueStrings);
//                sort list according to sending and receiving massage
				Collections.sort(usercalllist, (o1, o2) -> {
					int tm1 = (int) o1.getTm();
					int tm2 = (int) o2.getTm();

					if (tm1 < tm2) {
						return 1;
					} else {
						return -1;
					}
				});
				mAdapter = new RecyclerAdapter(context, usercalllist);
				mAdapter.notifyDataSetChanged();
				review.setAdapter(mAdapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
