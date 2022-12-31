package com.example.hunkerchat_app;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import io.grpc.Context;


public class messageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message>messageArrayList;
    int Item_SEND=1;
    int Item_RECEIVE=2;

    public messageAdapter(Context context) {
        this.context = context;
        this.messageArrayList= messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==Item_SEND)
        {
            View view= LayoutInflater.from(parent.context).inflate(R.layout.senderchatlayout,parent,false);
            return  new SenderViewHolder(view);
        }else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.receiverchatlayout,parent,false);
            return  new ReceiverViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=messageArrayList.get(position);
        if (holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder ViewHolder=(SenderViewHolder)holder;
            ViewHolder.textViewmessage.setText(message.getTarget().getMessage());
            ViewHolder.timeofmessage.setText(message.getCurrenttime);
        }else
        {
            ReceiverViewHolder ViewHolder=(ReceiverViewHolder) holder;
            ViewHolder.textViewmessage.setText(message.getMessage());
            ViewHolder.timeofmessage.setText(message.getCurrenttime);
        }

    }


    @Override
    public int getItemViewType(int position) {
       Message message=messageArrayList.get(position);
       if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getCallback()))
       {
           return Item_SEND;
       }else
       {
           return Item_RECEIVE;
       }

       }



    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewmessage;
        TextView timeofmessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timemassage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView textViewmessage;
        TextView timeofmessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timemassage);
        }
    }


}
