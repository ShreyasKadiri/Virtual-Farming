package application.example.myfarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public ArrayList<ChatClass> arrayList;

    public ChatAdapter(ArrayList<ChatClass> chats){
        arrayList = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_item,parent,false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(v);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatClass chatClass = arrayList.get(position);
        holder.senderTextView.setText(chatClass.getSender());
        holder.messageTextView.setText(chatClass.getMessage());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateString = formatter.format(new Date((chatClass.getTimeStamp())));
        holder.timeTextView.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static  class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView senderTextView;
        public TextView messageTextView;
        public TextView timeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.chat_user);
            messageTextView = itemView.findViewById(R.id.chat_message);
            timeTextView = itemView.findViewById(R.id.chat_time);
        }
    }
}
