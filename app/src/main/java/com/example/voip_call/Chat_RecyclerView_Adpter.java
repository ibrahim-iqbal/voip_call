package com.example.voip_call;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.voip_call.Notify.CHANNEL1;

public class Chat_RecyclerView_Adpter extends RecyclerView.Adapter<Chat_RecyclerView_Adpter.MyViewholder> {
    private static final String TAG = "Chat_RecyclerView";
    private NotificationManagerCompat notificationManager;
    @NonNull
    private Context context;
    private List<UserinfoList> userList;

    Chat_RecyclerView_Adpter(@NonNull Context context, List<UserinfoList> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alluserlayout, parent, false);
        notificationManager = NotificationManagerCompat.from(context);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, final int position)
    {
        holder.name.setText(userList.get(position).getName());
        holder.email.setText(userList.get(position).getMassage());
////        int n = userList.get(position).getUnRno();
//        holder.unread.setText("" + n);

        if (userList.get(position).imageurl.equals("")) {
//            Log.d(TAG, "Hey");
        } else {
//            Picasso.get().load(userList.get(position).getImageurl()).into(holder.profileppic);
        }
        holder.userlayout.setOnClickListener(v ->
        {
            Intent it = new Intent(context, ChattingActivity.class);
            it.putExtra("userid", userList.get(position).getId())
                    .putExtra("name", userList.get(position).getName())
                    .putExtra("img", userList.get(position).getImageurl());
            context.startActivity(it);

            Intent intent = new Intent(context, ChattingActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Notification notification = new NotificationCompat.Builder(context, CHANNEL1)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(userList.get(position).getName())
//                    .setContentText(userList.get(position).getId())
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
                    .build();
            notificationManager.notify(1, notification);
//            context.startActivity(it);
//            ((Activity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(ArrayList<UserinfoList> viewModels) {
        userList.clear();
        userList.addAll(viewModels);
        notifyDataSetChanged();
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