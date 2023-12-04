package com.example.projetseg2505;


import java.util.*;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class CustomAdapter extends ArrayAdapter<RequestItem> {

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<RequestItem> items) {
        super(context, resource, textViewResourceId, items);
    }

    private ButtonClickListener buttonClickListener;

    public interface ButtonClickListener {
        void onButtonClick(View view, int position, int buttonId);
    }

    public void setButtonClickListener(ButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);

        // Your custom logic for handling buttons and their onClickListeners
        Button btnValider = itemView.findViewById(R.id.btnValider);
        Button btnSupprimer = itemView.findViewById(R.id.btnSupprimer);

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur le bouton Valider
                Toast.makeText(getContext(), "Valider clicked for item at position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lors du clic sur le bouton Supprimer
                remove(getItem(position)); // Remove the item from the list
                notifyDataSetChanged(); // Notify the adapter of the change
                Toast.makeText(getContext(), "Supprimer clicked for item at position " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return itemView;
    }
}



