package com.evolutyzitservices.projects.bloodbank2.ChatMessage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evolutyzitservices.projects.bloodbank2.R;

import java.util.List;

/**
 * Created by evolutyz on 21/02/18.
 */

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
   TextView authorTextView;
    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }


        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message = getItem(position);
        messageTextView.setText(message.getText());

        authorTextView.setText(message.getName());

        return convertView;
    }
}
