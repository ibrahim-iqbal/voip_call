package com.example.voip_call;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowChatRecAdapter extends RecyclerView.Adapter<ShowChatRecAdapter.chat_showlayout>
{
    private String c_userid;
    private Context context;
    private List<Chat_Data> chat_data;

    ShowChatRecAdapter(String email, ChattingActivity context, List<Chat_Data> chat_data)
    {
        c_userid = email;
        this.context = context;
        this.chat_data = chat_data;
    }

    @NonNull
    @Override
    public chat_showlayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.messagelayout, parent, false);
        return new chat_showlayout(v);
    }


    public void onBindViewHolder(@NonNull chat_showlayout holder, int position)
    {
        if (c_userid.equals(chat_data.get(position).getSender()))
        {

            holder.r_massage.setPadding(0, 0, 0, 0);
            holder.massage.setText(chat_data.get(position).getMassage());
        } else
            {
            holder.massage.setPadding(0, 0, 0, 0);
            holder.r_massage.setText(chat_data.get(position).getMassage());
        }
    }

    @Override
    public int getItemCount() {
        return chat_data.size();
    }

    class chat_showlayout extends RecyclerView.ViewHolder
    {
        TextView massage, r_massage;
        chat_showlayout(@NonNull View itemView)
        {
            super(itemView);
            massage = itemView.findViewById(R.id.massage);
            r_massage = itemView.findViewById(R.id.r_massage);
        }
    }

}
