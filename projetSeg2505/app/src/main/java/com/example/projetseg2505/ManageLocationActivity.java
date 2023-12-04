package com.example.projetseg2505;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageLocationActivity extends AppCompatActivity{
    //instantiate the elements of the create location layout
    EditText editTextLocationName;
    EditText editTextLocationAddress;
    DatabaseReference servicesDB;
    DatabaseReference userRef;
    EditText editTextOpeningTime;
    EditText editTextClosingTime;
    ListView listViewServices;
    ListView listViewForButton;
    List<Service> services;
    Button btnUpdateLocation;
    List<Service> selectServices;
    String userName;
    Location currentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_location);
        services = new ArrayList<>();
        servicesDB = FirebaseDatabase.getInstance().getReference("services");
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextLocationAddress = findViewById(R.id.editTextLocationAddress);
        editTextOpeningTime = findViewById(R.id.editTextOpeningTime);
        editTextClosingTime = findViewById(R.id.editTextClosingTime);
        btnUpdateLocation = findViewById(R.id.btnUpdateLocation);
        userName = getIntent().getStringExtra("name_key");
        Log.d("PRINTS", userName);
        //get the datasnapshot for the current location
        userRef = FirebaseDatabase.getInstance().getReference("locations");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Location dsLocation = ds.getValue(Location.class);
                    Log.d("Prints", ds.getValue(Location.class).getUserName());
                    Log.d("PRINTBOOL", String.valueOf(ds.getValue(Location.class).getUserName().equals(userName)));
                    if(dsLocation.getUserName().equals(userName)){
                        currentLocation = dsLocation;
                        editTextLocationAddress.setText(currentLocation.getAddress());
                        editTextOpeningTime.setText(currentLocation.getOpeningTime());
                        editTextClosingTime.setText(currentLocation.getClosingTime());
                        editTextLocationName.setText(currentLocation.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editTextOpeningTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(editTextOpeningTime); // show the time picker and set the opening time
            }
        });

        editTextClosingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(editTextClosingTime); //show the time picker and set the closing time
            }
        });
        //populate the list
        listViewServices = findViewById(R.id.listViewServices);
        listViewForButton = findViewById(R.id.listViewServices);
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextLocationName = findViewById(R.id.editTextLocationName);
                editTextLocationAddress = findViewById(R.id.editTextLocationAddress);
                editTextOpeningTime = findViewById(R.id.editTextOpeningTime);
                editTextClosingTime = findViewById(R.id.editTextClosingTime);
                btnUpdateLocation = findViewById(R.id.btnUpdateLocation);
                listViewServices = findViewById(R.id.listViewServices);

                String name = editTextLocationName.getText().toString().trim();
                String address = editTextLocationAddress.getText().toString().trim();
                String openingTime = editTextOpeningTime.getText().toString().trim();
                String closingTime = editTextClosingTime.getText().toString().trim();

                // validation
                if (!validateName(name)) {
                    Toast.makeText(ManageLocationActivity.this, "Invalid name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateAddress(address)) {
                    Toast.makeText(ManageLocationActivity.this, "Invalid address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateHours(openingTime, closingTime)) {
                    Toast.makeText(ManageLocationActivity.this, "Invalid times", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateLocation(name, address, name, openingTime, closingTime);
            }
        });
    }
    private void updateLocation(String id, String address, String locationName, String openingT,
                                String closingT /*, List<Service> offeredServices*/) {

        //get reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("locations").child(id);

        //updating product
        reference.child("address").setValue(address);
        reference.child("closingTime").setValue(closingT);
        reference.child("name").setValue(locationName);
        reference.child("openingTime").setValue(openingT);

        Toast.makeText(getApplicationContext(), "Location infos updated", Toast.LENGTH_LONG).show();

    }




    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        servicesDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clearing the previous services list
                services.clear();

                //iterating through all the nodes, refreshes the app
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Service serv = postSnapshot.getValue(Service.class);
                    services.add(serv);
                }

                //create adapter
                ServiceListAdapter serviceAdapter = new ServiceListAdapter(ManageLocationActivity.this, services);
                //attach the adapter to the listView
                listViewServices.setAdapter(serviceAdapter);
                listViewServices.setOnItemClickListener(((parent, view, position, id) -> {
                    serviceAdapter.toggleSelection(position);
                    selectServices = serviceAdapter.getSelectedItems();
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private boolean validateHours(String openTime, String closingTime) {
        //method to validate the hours of a location
        String[] openTimeParts = openTime.split(":");
        String[] closingTimeParts = closingTime.split(":");

        // Parse hours and minutes as integers
        int openHours = Integer.parseInt(openTimeParts[0]);
        int openMinutes = Integer.parseInt(openTimeParts[1]);
        int closingHours = Integer.parseInt(closingTimeParts[0]);
        int closingMinutes = Integer.parseInt(closingTimeParts[1]);

        // Convert the time to an integer (e.g., HHmm format)
        int openTimeAsInt = openHours * 100 + openMinutes;
        int closeTimeAsInt = closingHours * 100 + closingMinutes;

        return closeTimeAsInt > openTimeAsInt;
    }

    private boolean validateAddress(String locationAddress) {
        //method to validate the address of a location
        String regex = "\\d+\\s+\\w+\\s+\\w+\\s*,\\s*\\w+\\s*"; //to match following format: 9999 streetname streettype, city
        return locationAddress.matches(regex);
    }

    private boolean validateName(String locationName) {
        //method to validate the name of the location
        String accountNamePattern = "^[a-zA-Z0-9]+$";

        return locationName.matches(accountNamePattern);
    }
    private void openTimeDialog(EditText editText) {
        TimePickerDialog timeDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
            }
        }, 15, 00, true);
        timeDialog.show();
    }

}
