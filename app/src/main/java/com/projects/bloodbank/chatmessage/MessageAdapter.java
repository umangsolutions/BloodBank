package com.projects.bloodbank.chatmessage;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projects.bloodbank.R;

import java.util.List;

/**
 * Created by anil on 21/02/18.
 */

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
    MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }


        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message = getItem(position);
        assert message != null;
        messageTextView.setText(message.getText());

        authorTextView.setText(message.getName());

        return convertView;
    }
}
