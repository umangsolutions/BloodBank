package com.projects.bloodbank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Objects;

public class LinksActivity extends AppCompatActivity {
    String[] links;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Links");
        listView=findViewById(R.id.linkslist);
        links = getResources().getStringArray(R.array.links);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, links);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<links.length){
                    Uri uri = Uri.parse(links[position]);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//finish();
            onBackPressed();
        }
        return true;
    }
}
