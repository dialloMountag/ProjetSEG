package com.example.projetseg2505;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageServiceActivity extends AppCompatActivity {
    DatabaseReference servicesDB;
    EditText editTextServiceName;
    EditText editTextRequirements;
    Button btnAdd;
    ListView listViewServices;
    List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service);
        servicesDB = FirebaseDatabase.getInstance().getReference("services");

        //assign the components of manage service layout
        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextRequirements = findViewById(R.id.editTextRequirements);
        btnAdd = findViewById(R.id.btnAdd);
        listViewServices = findViewById(R.id.listViewServices);
        services = new ArrayList<>();

        //adding onClickListen to add button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });

        //adding onLongClickListener for the services
        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Service service = services.get(position);
                showUpdateDeleteDialog(service.getServiceName());
                return true;
            }
        });
    }

    protected void onStart(){
        super.onStart();
        //attaching value event listener
        servicesDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clearing the previous services list
                services.clear();

                //iterating through all the nodes, refreshes the app
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    //get service
                    Service serv = postSnapshot.getValue(Service.class);
                    //add it to the list
                    services.add(serv);
                }

                //create adapter
                ServiceListAdapter serviceAdapter = new ServiceListAdapter(ManageServiceActivity.this, services);
                //attach the adapter to the listView
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showUpdateDeleteDialog(final String serviceName) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog2, null);
        dialogBuilder.setView(dialogView);
        TextView serviceNameView = dialogView.findViewById(R.id.serviceNameView);
        serviceNameView.setText(serviceName);
        final EditText editTextRequirements  = (EditText) dialogView.findViewById(R.id.editTextNewReq);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateService);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteService);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = serviceNameView.getText().toString().trim();
                String requirements = editTextRequirements.getText().toString();

                //validate the text fields for updating a service
                //validate the text fields when adding a service
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(ManageServiceActivity.this, "Enter something in the Service name text field", Toast.LENGTH_SHORT).show();
                    return;
                }
                //service name ne doit pas contenir de caracteres speciaux
                String accountNamePattern = "^[a-zA-Z0-9\\s]+$";
                if(!name.matches(accountNamePattern)){
                    Toast.makeText(ManageServiceActivity.this, "Service name must only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                //doit inclure des informations
                if(TextUtils.isEmpty(requirements)){
                    Toast.makeText(ManageServiceActivity.this, "Please enter the updated requirements for this service", Toast.LENGTH_SHORT).show();
                    return;
                }

                //doit etre separe de virgules
                if(!requirements.contains(",")){
                    Toast.makeText(ManageServiceActivity.this, "Make sure to seperate requirements with commas, or include more than 1 requirement", Toast.LENGTH_LONG).show();
                    return;
                }

                //after validating, you can finally update the service
                    updateService(name, requirements);
                    b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(serviceName);
                b.dismiss();
            }
        });
    }

    private void updateService(String name, String req) {
        //get reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(name);

        //updating product
        Service serv = new Service(name, req);
        dR.setValue(serv);

        Toast.makeText(getApplicationContext(), "Service updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteService(String id) {
        // get the reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(id);

        //remove the id
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "service deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addService() {
        String name = editTextServiceName.getText().toString().trim();
        String requirements = editTextRequirements.getText().toString();

        //validate the text fields when adding a service
        if(TextUtils.isEmpty(name)){
            Toast.makeText(ManageServiceActivity.this, "Enter something in the Service name text field", Toast.LENGTH_SHORT).show();
            return;
        }
        //service name ne doit pas contenir de caracteres speciaux
        String accountNamePattern = "^[a-zA-Z0-9\\s]+$";
        if(!name.matches(accountNamePattern)){
            Toast.makeText(ManageServiceActivity.this, "Service name must only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        //doit inclure des informations
        if(TextUtils.isEmpty(requirements)){
            Toast.makeText(ManageServiceActivity.this, "Enter a Password", Toast.LENGTH_SHORT).show();
            return;
        }

        //doit etre separe de virgules
        if(!requirements.contains(",")){
            Toast.makeText(ManageServiceActivity.this, "Make sure to seperate requirements with commas, or include more than 1 requirement", Toast.LENGTH_LONG).show();
            return;
        }

        //test si le service existe deja
        for(Service service : services){
            if(name.equalsIgnoreCase(service.getServiceName())){
                Toast.makeText(ManageServiceActivity.this, "Please make sure a service with the same name does not exist already!", Toast.LENGTH_LONG).show();
                return;
            }
        }

            //creating a Service obj
            Service serv = new Service(name, requirements);

            //send it to the db
            servicesDB.child(name).setValue(serv);

            //reset the editTexts
            editTextServiceName.setText("");
            editTextRequirements.setText("");

            //display a success toast
            Toast.makeText(this, "service added", Toast.LENGTH_LONG).show();


    }
}