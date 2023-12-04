package com.example.projetseg2505;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class  ViewRequestActivity extends AppCompatActivity {

    // Assuming you have a list of RequestItem
    List<RequestItem> itemList = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        // Create an instance of the CustomAdapter
        adapter = new CustomAdapter(this, R.layout.item_layout, R.id.textView, itemList);

        // Set the adapter to the ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Example: Add some initial items to the list
        itemList.add(new RequestItem("Item 1"));
        itemList.add(new RequestItem("Item 2"));
        itemList.add(new RequestItem("Item 3"));

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();

    }
}