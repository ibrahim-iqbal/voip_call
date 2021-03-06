package com.example.voip_call;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChattingActivity extends AppCompatActivity {
	FirebaseAuth mauth;
	TextView tname;
	ImageView send_btn, backchat;
	EditText text_massage;
	String Email, chatuserid, chatId, chatname;
	RecyclerView show_chat;
	RecyclerView.Adapter chatAdpter;
	RecyclerView.LayoutManager chatlayoutManeger;
	List<Chat_Data> chat_data;
	androidx.core.widget.NestedScrollView scroll;
	DatabaseReference r_db, u_db;
	String c_userId, chaterId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chating);

		Window window = this.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDarker));

		text_massage = findViewById(R.id.textmassge);
		send_btn = findViewById(R.id.send_button);
		scroll = findViewById(R.id.scroll);
		backchat = findViewById(R.id.backchat);
		r_db = FirebaseDatabase.getInstance().getReference("chatinfo");
		u_db = FirebaseDatabase.getInstance().getReference("userinfo");


		show_chat = findViewById(R.id.show_chat);
		chatlayoutManeger = new LinearLayoutManager(this);
		show_chat.setLayoutManager(chatlayoutManeger);

		mauth = FirebaseAuth.getInstance();
		Email = Objects.requireNonNull(mauth.getCurrentUser()).getEmail();

		Intent I = getIntent();
		chatuserid = I.getStringExtra("userid");
		chatname = I.getStringExtra("name");
		String img = I.getStringExtra("img");

		tname = findViewById(R.id.tvname);
		tname.setText(chatname);


		assert chatuserid != null;
		send_btn.setOnClickListener(v -> {
			if (text_massage.getText().toString().trim().isEmpty()) {

			} else {
				scroll.fullScroll(ScrollView.FOCUS_DOWN);

				chatInsert(chatId);
			}
		});

		backchat.setOnClickListener(v -> onBackPressed());
	}

	@Override
	protected void onStart() {
		super.onStart();


		u_db.orderByChild("email").equalTo(Objects.requireNonNull(mauth.getCurrentUser()).getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot datas : dataSnapshot.getChildren()) {
					c_userId = Objects.requireNonNull(datas.child("id").getValue()).toString();
					u_db.orderByChild("id").equalTo(chatuserid).addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
							for (DataSnapshot datas : dataSnapshot.getChildren()) {
								chaterId = Objects.requireNonNull(datas.child("id").getValue()).toString();
								chatId = idgenrater(c_userId, chaterId);
								updateunradmassage(chatId);
								r_db.child(c_userId).child(chatId).addValueEventListener(new ValueEventListener() {
									@Override
									public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
										chat_data = new ArrayList<>();
										for (DataSnapshot datas : dataSnapshot.getChildren()) {
											String mass = Objects.requireNonNull(datas.child("massage").getValue()).toString();
											String senderId = Objects.requireNonNull(datas.child("senderId").getValue()).toString();
											Chat_Data d = new Chat_Data(mass, senderId);
											chat_data.add(d);
											text_massage.requestFocus();
										}
										chatAdpter = new ShowChatRecAdapter(c_userId, ChattingActivity.this, chat_data);
										show_chat.setAdapter(chatAdpter);
										if (chatAdpter.getItemCount() == 0) {
											show_chat.post(() -> show_chat.smoothScrollToPosition(0));
										} else {
											show_chat.post(() -> show_chat.smoothScrollToPosition(chatAdpter.getItemCount() - 1));
										}
										Runnable runnable = () -> scroll.fullScroll(ScrollView.FOCUS_DOWN);
										scroll.post(runnable);
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
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
	}

	public void updateunradmassage(String ch) {
		Query query = r_db.child(c_userId).child(chatId);
		query.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot unda : dataSnapshot.getChildren()) {
//                Toast.makeText(ChattingActivity.this, "" + unda.getKey(), Toast.LENGTH_SHORT).show();
//                    int unread=Integer.parseInt(unda.child("unread").getValue().toString());
//                Toast.makeText(ChattingActivity.this, ""+unread, Toast.LENGTH_SHORT).show();
					String key = unda.getKey();
					Map<String, Object> upd = new HashMap<String, Object>();
					upd.put("unread", 1);
					assert key != null;
					r_db.child(c_userId).child(chatId).child(key).updateChildren(upd);

				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
	}

	public String idgenrater(String c_userId, String chat_user) {
		String id = "";
		int id1 = Integer.parseInt(c_userId);
		int id2 = Integer.parseInt(chat_user);
		if (id1 > id2) {
			id = c_userId + "_chat_" + chat_user;
		} else {
			id = chat_user + "_chat_" + c_userId;
		}
		return id;
	}


	public void chatInsert(String chatId) {
		long tsLong = System.currentTimeMillis() / 1000;

		Toast.makeText(this, "" + tsLong, Toast.LENGTH_SHORT).show();
		String id = r_db.push().getKey();
//        Toast.makeText(this, ""+chatId, Toast.LENGTH_SHORT).show();
		String Massage = text_massage.getText().toString().trim();

		ChatInsertData chat = new ChatInsertData(chatId, Massage, c_userId, chaterId, 1, tsLong);
		reciverchatinsert();
		r_db.child(c_userId).child(chatId).child(id).setValue(chat);
		text_massage.setText("");

		Runnable runnable = () -> scroll.fullScroll(ScrollView.FOCUS_DOWN);
		scroll.post(runnable);
	}

	public void reciverchatinsert() {
		long tsLong = System.currentTimeMillis() / 1000;

		Toast.makeText(this, "" + tsLong, Toast.LENGTH_SHORT).show();
		String id = r_db.push().getKey();
//        Toast.makeText(this, ""+chatId, Toast.LENGTH_SHORT).show();
		String Massage = text_massage.getText().toString().trim();
		ChatInsertData chat = new ChatInsertData(chatId, Massage, c_userId, chaterId, 0, tsLong);

		r_db.child(chaterId).child(chatId).child(id).setValue(chat);
		Runnable runnable = () -> scroll.fullScroll(ScrollView.FOCUS_DOWN);
		scroll.post(runnable);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (FirebaseDatabase.getInstance() != null) {
			FirebaseDatabase.getInstance().goOnline();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (FirebaseDatabase.getInstance() != null) {
			FirebaseDatabase.getInstance().goOffline();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);

	}
}