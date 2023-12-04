package com.example.projetseg2505;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ServiceListAdapter extends ArrayAdapter<Service> {

    private Activity context;
    private SparseBooleanArray selectedItems;
    List<Service> services;



    public ServiceListAdapter(Activity context, List<Service> services){
        //constructor
        super(context, R.layout.layout_service_list, services);
        this.context = context;
        this.services = services;
        this.selectedItems = new SparseBooleanArray();
        }

    @Override
    public int getCount() {
       return services.size(); // return the length of the list of services
    }

    @Nullable
    @Override
    public Service getItem(int position) {
        return services.get(position); // return the service that is at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_service_list, null, true);

        TextView textViewService = (TextView) listViewItem.findViewById(R.id.textViewName);

        Service service = services.get(position);
        textViewService.setText(service.getServiceName());
        //Adjust the UI based on selection state
        if(selectedItems.get(position, false)){
            listViewItem.setBackgroundColor(ContextCompat.getColor(context, R.color.greenish));
        } else {
            listViewItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        return listViewItem;
    }

    public void toggleSelection(int position){
        boolean previousState = selectedItems.get(position, false);
        selectedItems.put(position, !previousState);
        notifyDataSetChanged();
    }

    public List<Service> getSelectedItems(){
        List<Service> selectedList = new ArrayList<>(); //create a new list to add the selectedItems
        for(int i = 0; i < services.size(); i++){
            if(selectedItems.get(i, false)){
                selectedList.add(services.get(i)); // add the services to list
            }
        }
        return selectedList; //return the list
    }
}

