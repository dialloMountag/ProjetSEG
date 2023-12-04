package com.example.projetseg2505;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateLocationActivity extends AppCompatActivity{
    //instantiate the elements of the create location layout
    EditText editTextLocationName;
    EditText editTextLocationAddress;
    DatabaseReference servicesDB;
    DatabaseReference locationsDB;
    EditText editTextOpeningTime;
    EditText editTextClosingTime;
    String userName;
    ListView listViewServices;
    ListView listViewForButton;
    List<Service> services;
    Button btnCreateLocation;
    List<Service> selectServices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        services = new ArrayList<>();
        servicesDB = FirebaseDatabase.getInstance().getReference("services");
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextLocationAddress = findViewById(R.id.editTextLocationAddress);
        editTextOpeningTime = findViewById(R.id.editTextOpeningTime);
        editTextClosingTime = findViewById(R.id.editTextClosingTime);
        btnCreateLocation = findViewById(R.id.btnCreateLocation);
        userName = getIntent().getStringExtra("name_key");
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
        btnCreateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationName, locationAddress, locationCity, openTime, closingTime, userName; //pour enregistrer les arguments, construire objet location
                locationName = editTextLocationName.getText().toString();
                locationAddress = editTextLocationAddress.getText().toString();
                String[] locationAddrParts = locationAddress.split(",");
                locationCity = locationAddrParts[locationAddrParts.length-1].trim();
                openTime = editTextOpeningTime.getText().toString();
                closingTime = editTextClosingTime.getText().toString();
                ServiceListAdapter serviceListAdapter = new ServiceListAdapter(CreateLocationActivity.this, services);
                listViewForButton.setAdapter(serviceListAdapter);

                List<String> formattedHours = formatHours(openTime, closingTime);
                //all data has been collected to create the location
                String userNameFromIntent = getIntent().getStringExtra("name_key");
                Location newLocation = new Location(locationName, locationCity, locationAddress, selectServices, formattedHours.get(0), formattedHours.get(1), userNameFromIntent);

                //validate the location address
                boolean nameValid = validateName(locationName);
                boolean addressValid = validateAddress(locationAddress);
                boolean hoursValid = validateHours(openTime, closingTime);

                //if any of the validations are unsuccesful, return and print toast message
                if(!nameValid){
                    Toast.makeText(CreateLocationActivity.this, "Make sure the name of the location only contains alphanumerical characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!addressValid){
                    Toast.makeText(CreateLocationActivity.this, "Make sure the address follows the following format: 999 streetname streettype, city", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!hoursValid){
                    Toast.makeText(CreateLocationActivity.this, "Make sure the opening time is smaller than the closing time.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //add the new location to the database
                locationsDB = FirebaseDatabase.getInstance().getReference("locations");
                locationsDB.child(locationName).setValue(newLocation);
                Toast.makeText(CreateLocationActivity.this, "Location added to the Database", Toast.LENGTH_SHORT).show();

                //show the
                Intent intent = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(intent);
                finish();
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

        //make sure the value of the closing time is greater than the opening time
        return closeTimeAsInt > openTimeAsInt;
    }

    private List<String> formatHours (String openTime, String closingTime) {
        String[] openTimeParts = openTime.split(":");
        String[] closingTimeParts = closingTime.split(":");

        // Parse hours and minutes as Strings
        String openHours = (openTimeParts[0].length() == 1) ?  "0".concat(openTimeParts[0]) : openTimeParts[0];
        String openMinutes = (openTimeParts[1].length() == 1) ?  "0".concat(openTimeParts[1]) : openTimeParts[1];
        String closingHours = (closingTimeParts[0].length() == 1) ?  "0".concat(closingTimeParts[0]) : closingTimeParts[0];
        String closingMinutes = (closingTimeParts[1].length() == 1) ?  "0".concat(closingTimeParts[1]) : closingTimeParts[1];

        // Create an ArrayList with parsed opening and closing times as strings
        List<String> parsedTimeList = new ArrayList<>();
        parsedTimeList.add(openHours.concat(":").concat(openMinutes));
        parsedTimeList.add(closingHours.concat(":").concat(closingMinutes));

        return parsedTimeList;
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
                ServiceListAdapter serviceAdapter = new ServiceListAdapter(CreateLocationActivity.this, services);
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

