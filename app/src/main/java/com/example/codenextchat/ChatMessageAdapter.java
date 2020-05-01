package com.example.codenextchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_message_item, parent, false);
        ChatMessage chatMessage = getItem(position);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        messageTextView.setText(chatMessage.getMessage());
        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        usernameTextView.setText(chatMessage.getUsername());
        CircleImageView profilePicImageview = convertView.findViewById(R.id.messengerImageView);

        if(chatMessage.getProfilePic() != null) {
            Picasso.get().load(chatMessage.getProfilePic()).into(profilePicImageview);
        } else {
           profilePicImageview.setImageResource(R.drawable.ic_account_circle_black_36dp);
        }

        return convertView;
    }
}
