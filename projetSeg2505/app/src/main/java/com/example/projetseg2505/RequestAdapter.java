package com.example.projetseg2505;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends Fragment {

    private List<RequestItem> itemList;
    private ArrayAdapter<LauncherActivity.ListItem> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_request, container, false);

        // Création de la liste d'éléments
        itemList = new ArrayList<>();
        itemList.add(new RequestItem("Élément 1"));
        itemList.add(new RequestItem("Élément 2"));
        // ... Ajoutez autant d'éléments que vous le souhaitez

        // Create a custom adapter
        CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.item_layout, R.id.textView, itemList);

        // Set the adapter to your ListView
        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);


        return view;
    }
}

