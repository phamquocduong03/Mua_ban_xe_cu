package com.example.mua_ban_xe_cu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mua_ban_xe_cu.Activity.ChatActivity;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Conversation;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private List<Conversation> conversationList;

    public ConversationAdapter(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        holder.tvOtherParty.setText(conversation.getOtherParty());
        holder.tvCarId.setText("Car ID: " + conversation.getCarId());
        holder.tvLastMessage.setText(conversation.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("receiver", conversation.getOtherParty());  // phải khớp với key mà ChatActivity đang lấy
            intent.putExtra("carId", conversation.getCarId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView tvOtherParty, tvCarId, tvLastMessage;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            tvOtherParty = itemView.findViewById(R.id.tvOtherParty);
            tvCarId = itemView.findViewById(R.id.tvCarId);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }
}
