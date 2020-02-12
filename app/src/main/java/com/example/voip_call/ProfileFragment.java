package com.example.voip_call;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfileFragment extends Fragment {
	Context context;
	private de.hdodenhof.circleimageview.CircleImageView profileimg;
	private EditText etname, etemail, etmob;
	private ImageView camera, gallery;
	private AlertDialog alertDialog;
	private Button save, update;
	private Bitmap bitmap;
	private String name, imgurl, id, number, url, na;
	private RecyclerView recycle;
	private DatabaseReference db;
	private FirebaseRecyclerAdapter<alluserinfo, allUser_ViewHolder> adapter;
	private FirebaseAuth mAuth;
	private StorageReference mStorageRef;

	ProfileFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile, container, false);

		profileimg = v.findViewById(R.id.profileimg);
		etname = v.findViewById(R.id.etname);
		etmob = v.findViewById(R.id.etmob);
		etemail = v.findViewById(R.id.etemail);
		update = v.findViewById(R.id.update);
		save = v.findViewById(R.id.save);
		mStorageRef = FirebaseStorage.getInstance().getReference();

		profileimg.setEnabled(false);

		Window window = Objects.requireNonNull(getActivity()).getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDarker));

		etname.setEnabled(false);
		etemail.setEnabled(false);
		etmob.setEnabled(false);
		recycle = v.findViewById(R.id.contactView);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
		recycle.setLayoutManager(layoutManager);

		db = FirebaseDatabase.getInstance().getReference("userinfo");
		mAuth = FirebaseAuth.getInstance();
		String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
		db.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot dp : dataSnapshot.getChildren()) {
					name = Objects.requireNonNull(dp.child("name").getValue()).toString();
					id = Objects.requireNonNull(dp.child("id").getValue()).toString();
					//number = dp.child("num").getValue().toString();
					url = Objects.requireNonNull(dp.child("imgurl").getValue()).toString();
					etname.setText(name);
					etmob.setText(number);

					if (url == null) {
						profileimg.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
					} else {
						Picasso.get().load(url).error(R.drawable.ic_user).into(profileimg);
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});
		etemail.setText(email);

		profileimg.setOnClickListener(v13 ->
		{
			AlertDialog.Builder ld = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
			LayoutInflater inflater1 = getLayoutInflater();
			@SuppressLint("InflateParams")
			View dialogView = inflater1.inflate(R.layout.custom_img, null);
			ld.setView(dialogView);
			alertDialog = ld.create();
			alertDialog.show();
			camera = alertDialog.findViewById(R.id.camera);
			gallery = alertDialog.findViewById(R.id.gallery);

			camera.setOnClickListener(v12 ->
			{
				Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
				Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(it, 0);
			});
			gallery.setOnClickListener(v1 ->
			{
				Toast.makeText(getContext(), "gallery", Toast.LENGTH_SHORT).show();
				Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(it, 1);
			});
		});
		update.setOnClickListener(v1 ->
		{
			profileimg.setEnabled(true);
			save.animate().alpha(1f).setDuration(1000);
			save.setEnabled(true);
			update.animate().alpha(0f).setDuration(1000);
			update.setEnabled(true);

			etname.setEnabled(true);
			etemail.setEnabled(true);
			etmob.setEnabled(true);
		});
		save.setOnClickListener(v1 ->
		{
			ProgressDialog pd = new ProgressDialog(getContext());
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.setIcon(getResources().getDrawable(R.drawable.fab_bg_mini));
			pd.show();
			if (bitmap != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
				byte[] b = baos.toByteArray();

				final StorageReference sr = mStorageRef.child("Profile Images").child(b + "jpg");
				final UploadTask uploadTask = sr.putBytes(b);
				uploadTask.addOnCompleteListener((Activity) Objects.requireNonNull(getContext()), task ->
						uploadTask.addOnSuccessListener(taskSnapshot ->
								sr.getDownloadUrl().addOnSuccessListener(uri ->
										{
											imgurl = String.valueOf(uri);
											update();
											pd.dismiss();
										}
								)
						)
				);
			} else {
				Toast.makeText(context, "Select a Profile Photo", Toast.LENGTH_SHORT).show();
			}
			profileimg.setEnabled(false);
			save.setEnabled(false);
			save.animate().alpha(0f).setDuration(1000);
			update.animate().alpha(1f).setDuration(1000);
			update.setEnabled(true);

			etname.setEnabled(false);
			etemail.setEnabled(false);
			etmob.setEnabled(false);
		});
		return v;
	}

	private void update() {

		String id1 = id.trim();
		na = etname.getText().toString().trim();
		String em = etemail.getText().toString().trim();
		String num = etmob.getText().toString().trim();

		Map<String, Object> post = new HashMap<>();
		post.put("email", em);
		post.put("id", id1);
		post.put("imgurl", imgurl);
		post.put("name", name);
		post.put("number", num);
		try {
			db.child(id).updateChildren(post).addOnCompleteListener(task ->
					Toast.makeText(ProfileFragment.this.getContext(), "Your profile updated", Toast.LENGTH_SHORT).show())
					.addOnFailureListener(e ->
							Toast.makeText(getContext(), "Profile Not Updated" + e.getMessage(), Toast.LENGTH_SHORT).show());
		} catch (Exception e) {
			Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
			bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
			profileimg.setImageBitmap(bitmap);
			Toast.makeText(getContext(), "" + bitmap, Toast.LENGTH_SHORT).show();
			alertDialog.dismiss();
		} else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), uri);
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			profileimg.setImageBitmap(bitmap);
			alertDialog.dismiss();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onStart() {
		super.onStart();
		FirebaseRecyclerOptions<alluserinfo> options = new FirebaseRecyclerOptions.Builder<alluserinfo>()
				.setQuery(db, alluserinfo.class)
				.build();

		adapter = new FirebaseRecyclerAdapter<alluserinfo, allUser_ViewHolder>(options) {
			@Override
			protected void onBindViewHolder(@NonNull allUser_ViewHolder holder, int position, @NonNull alluserinfo model) {
				String userid = getRef(position).getKey();

				assert userid != null;
				db.child(userid).addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
						if (dataSnapshot.hasChild("name")) {
							String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
							//String num = dataSnapshot.child("num").getValue().toString();
							String iurl = Objects.requireNonNull(dataSnapshot.child("imgurl").getValue()).toString();
							String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
							if (Objects.equals(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail(), email)) {
							} else {
								holder.all_name.setText(name);
								holder.all_email.setText(email);

								Picasso.get().load(iurl).centerCrop().resize(100, 100).error(R.drawable.ic_user).into(holder.allimg);

								holder.all_user.setOnClickListener(v ->
										startActivity(new Intent(context, ChattingActivity.class)
												.putExtra("userid", model.getId())
												.putExtra("name", model.getName())));
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
			public allUser_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user, parent, false);
				return new allUser_ViewHolder(view);
			}
		};
		recycle.setAdapter(adapter);
		adapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		adapter.stopListening();
	}

	public static class allUser_ViewHolder extends RecyclerView.ViewHolder {

		TextView all_name, all_email;
		ImageView allimg;
		LinearLayout all_user;

		allUser_ViewHolder(@NonNull View itemView) {
			super(itemView);
			all_name = itemView.findViewById(R.id.all_name);
			allimg = itemView.findViewById(R.id.allimg);
			all_email = itemView.findViewById(R.id.all_email);
			all_user = itemView.findViewById(R.id.all_use);
		}
	}
}
