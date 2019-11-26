package com.evolutyzitservices.projects.bloodbank2.DonarDetails;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.evolutyzitservices.projects.bloodbank2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 21-02-2018.
 */

public class DetailList extends ArrayAdapter<Details> {
    private Activity context;
    private List<Details> detailsList;
    private ArrayList<Details> details1;
    private TextView textView,textView2;
    private CheckBox checkBox;

    public DetailList(@NonNull Activity context, List<Details> detailsList){
        super(context, R.layout.custom_list_view,detailsList);
        this.context=context;
        this.detailsList=detailsList;
        details1=new ArrayList<Details>();
        details1.addAll(detailsList);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View listviewitem=layoutInflater.inflate(R.layout.custom_list_view,null,true);
        textView=(TextView)listviewitem.findViewById(R.id.textView);
        textView2=(TextView)listviewitem.findViewById(R.id.pincode);
        checkBox=listviewitem.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();

            }
        });
        textView.setText("Name:"+detailsList.get(position).getName());
        textView2.setText("Pincode:"+detailsList.get(position).getPincode());
        return listviewitem;

    }


}
