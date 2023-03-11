package com.inbitart.kebolapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inbitart.kebolapp.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by XandrOSS on 09/08/2016.
 */
public class ChatSessionAdapter extends
        RecyclerView.Adapter<ChatSessionAdapter.ChatMessageViewHolder> {
    private Context mContext;
    private ArrayList<ChatMessage> messages;
    private static ChatMessageItemClickListener chatMessageItemClickListener;


    public ChatSessionAdapter(Context context, ArrayList<ChatMessage> messages) {
        this.messages = messages;
        mContext = context;
    }


    public static class ChatMessageViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context vContext;
        private TextView txtMessageState;
        private TextView txtSentTime;
        private TextView txtChatMessage;
        private CardView cardView;


        public ChatMessageViewHolder(Context context, View itemView) {
            super(itemView);
            vContext = context;

//            YoYo.with(Techniques.RotateInUpLeft)
//                    .duration(500)
//                    .playOn(itemView);

            itemView.setOnClickListener(this);

            txtMessageState = (TextView) itemView.findViewById(R.id.txtMessageState);
            txtSentTime = (TextView) itemView.findViewById(R.id.txtSentTime);
            txtChatMessage = (TextView) itemView.findViewById(R.id.txtChatMessage);
        }

        public void bindChatMessage(final ChatMessage message) {

            if (message.isForUser("Xandross")) {
                itemView.setBackgroundResource(R.drawable.comments48left);
            } else {
                itemView.setBackgroundResource(R.drawable.comments48right);
            }

            String messageState = "";

           // int stateId = message.getState();

            if (message.getState() == ChatMessage.STATE_SENT) {
                messageState = vContext.getResources()
                        .getString(R.string.chat_message_state_sent);
            } else if (message.getState() == ChatMessage.STATE_QUEUED) {
                messageState = vContext.getResources()
                        .getString(R.string.chat_message_state_queued);
            } else if (message.getState() == ChatMessage.STATE_RECEIVED) {
                messageState = vContext.getResources()
                        .getString(R.string.chat_message_state_received);
            } else if (message.getState() == ChatMessage.STATE_ERROR) {
                messageState = vContext.getResources()
                        .getString(R.string.chat_message_state_error);
            } else messageState = vContext.getResources()
                        .getString(R.string.chat_message_state_unknow);

            txtMessageState.setText(messageState);

            txtSentTime.setText("7pm");
            txtChatMessage.setText(message.getContent());
        }

        @Override
        public void onClick(View v) {
            chatMessageItemClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ChatMessageItemClickListener chatMessageItemClickListener) {
        this.chatMessageItemClickListener = chatMessageItemClickListener;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_row_layout, parent, false);
        ChatMessageViewHolder chatMsgVH = new ChatMessageViewHolder(mContext, itemView);
        return chatMsgVH;
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        ChatMessage item = messages.get(position);
        holder.bindChatMessage(item);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void refreshAdapterData(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }
}
