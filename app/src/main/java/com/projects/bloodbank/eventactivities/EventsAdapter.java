package com.projects.bloodbank.eventactivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.bloodbank.R;

import java.util.List;

/**
 * Created by USER on 2/20/2018.
 */

public class EventsAdapter extends ArrayAdapter<EventItem> {
    private Activity context;
    private List<EventItem> eventList;

    EventsAdapter(@NonNull Activity context, List<EventItem> eventList) {
        super(context, R.layout.event_item, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"})
        View listviewitem = layoutInflater.inflate(R.layout.event_item, null, true);
        TextView textView = listviewitem.findViewById(R.id.txtDate);
        TextView textView1 = listviewitem.findViewById(R.id.txtLocation);
        TextView textView2 = listviewitem.findViewById(R.id.txtEvent);
        ImageView imageView = listviewitem.findViewById(R.id.imageView1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventList.get(position).getDate1())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventList.get(position).getDate1())
                .putExtra(CalendarContract.Events.TITLE, eventList.get(position).getName())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, eventList.get(position).getLocation())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "");
                context.startActivity(intent);
            }
        });
        EventItem event = eventList.get(position);
        textView.setText(event.getDate1());
        textView1.setText( event.getLocation());
        textView2.setText(event.getName());
        return  listviewitem;
    }

}
