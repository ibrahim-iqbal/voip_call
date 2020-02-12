package com.example.voip_call;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
	Context context;
	private List<UserinfoList> userList;

	RecyclerAdapter(@NonNull Context context, List<UserinfoList> userList) {
		this.context = context;
		this.userList = userList;
	}

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View listItem = layoutInflater.inflate(R.layout.custom_user_layout, parent, false);
		return new ViewHolder(listItem);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		holder.usertag.setText(userList.get(position).getName());
		holder.userup.setText(userList.get(position).getEmail());
		Picasso.get().load(userList.get(position).getImageurl()).centerCrop().resize(80, 80).into(holder.userimg);
		holder.call_img.setOnClickListener(v ->
		{
			Intent it = new Intent(context, CallScreen.class);
			it.putExtra("callerid", userList.get(position).getName());
			context.startActivity(it);
		});
	}

	@Override
	public int getItemCount() {
		return userList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		de.hdodenhof.circleimageview.CircleImageView userimg;
		androidx.appcompat.widget.AppCompatImageButton call_img, video_img;
		TextView usertag, userup;
		LinearLayout chatlL;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			this.chatlL = itemView.findViewById(R.id.chatlL);
			this.userimg = itemView.findViewById(R.id.userimg);
			this.usertag = itemView.findViewById(R.id.usertag);
			this.userup = itemView.findViewById(R.id.userup);
			this.call_img = itemView.findViewById(R.id.call_img);
			this.video_img = itemView.findViewById(R.id.video_img);
		}
	}
}