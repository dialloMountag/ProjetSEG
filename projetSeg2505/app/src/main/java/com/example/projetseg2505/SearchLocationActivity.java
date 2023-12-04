package com.example.projetseg2505;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationActivity extends AppCompatActivity {
    // Initialize Firebase Database reference
    private DatabaseReference branchesRef;

    // UI elements
    private EditText addressEditText;
    private EditText hoursEditText;
    private EditText servicesEditText;
    private RecyclerView locationsRecyclerView;

    private RecyclerView recyclerViewBranches;
    private LocationAdapter locationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        // Initialize Firebase Database
        branchesRef = FirebaseDatabase.getInstance().getReference("locations");

        // Initialize UI elements
        addressEditText = findViewById(R.id.editTextAddress);
        hoursEditText = findViewById(R.id.editTextHours);
        servicesEditText = findViewById(R.id.editTextServices);

        // Set up RecyclerView and adapter to display search results
        // ...
        recyclerViewBranches = findViewById(R.id.recyclerViewBranches);
        recyclerViewBranches.setLayoutManager(new LinearLayoutManager(this)); // Use LinearLayoutManager or your preferred layout manager

        // Initialize the adapter with an empty list
        locationAdapter = new LocationAdapter(new ArrayList<>());
        recyclerViewBranches.setAdapter(locationAdapter);

        // Set up a button or other UI element to trigger the search
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressEditText.getText().toString().trim().isEmpty() && servicesEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SearchLocationActivity.this, "Address/city is required", Toast.LENGTH_SHORT).show();
                } else {
                    performSearch();
                }
            }
        });
    }

    private void performSearch() {
        // Retrieve user input
        String addressQuery = addressEditText.getText().toString().trim();
        String hoursQuery = hoursEditText.getText().toString().trim();
        String servicesQuery = servicesEditText.getText().toString().trim();

        // Check if the address is an exact one or a city
        String[] addrParts = addressQuery.split(",");
        String city = addrParts[addrParts.length - 1];

        // Perform a query to Firebase based on user input
        DatabaseReference locationsRef = FirebaseDatabase.getInstance().getReference("locations");
        Query query = locationsRef;
        if (!addressQuery.isEmpty()) {
            if(addrParts.length > 1)
                query = query.orderByChild("address").equalTo(addressQuery);
            else
                query = query.orderByChild("city").equalTo(city);

        }



        /*
            // Add conditions for services if it is not empty
            if (!servicesQuery.isEmpty()) {
                // Assuming "offeredServices" is a property in your Location class
                query = query.orderByChild("offeredServices").equalTo(servicesQuery);
            }
            // Add conditions for services if it is not empty
            if (!hoursQuery.isEmpty()) {
                // Assuming "offeredServices" is a property in your Location class
                query = query.orderByChild("openingTime").equalTo(servicesQuery);
            }
        */


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> searchResults = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);

                    // Check if the hoursQuery falls within the opening and closing hours
                    if (!hoursQuery.isEmpty()) {
                        // Check if within working hours
                        if (!isWithinWorkingHours(location, hoursQuery)) {
                            continue;
                        }
                    }
                    if (!servicesQuery.isEmpty()) {
                        // Assuming "offeredServices" is a List<OfferedService> in your Location class
                        List<Service> offeredServices = location.getOfferedServices();
                        if (offeredServices != null) {
                            for (Service offeredService : offeredServices) {
                                String serviceName = offeredService.getServiceName();
                                if (serviceName != null && serviceName.equals(servicesQuery)) {
                                    searchResults.add(location);
                                    break;
                                }
                            }
                        }else {
                            continue;
                        }
                        continue;
                    }
                    searchResults.add(location);
                }
                // Update the RecyclerView adapter with search results
                LocationAdapter adapter = new LocationAdapter(searchResults);
                recyclerViewBranches.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }

    private boolean isWithinWorkingHours(Location location, String hoursQuery) {
        // Assuming openingTime and closingTime are in the format "HH:mm"
        String openingTime = location.getOpeningTime();
        String closingTime = location.getClosingTime();

        // Parse the strings into LocalTime objects
        LocalTime queryTime = LocalTime.parse(hoursQuery, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime openingLocalTime = LocalTime.parse(openingTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closingLocalTime = LocalTime.parse(closingTime, DateTimeFormatter.ofPattern("HH:mm"));

        // Compare the queryTime with openingLocalTime and closingLocalTime
        return !queryTime.isBefore(openingLocalTime) && !queryTime.isAfter(closingLocalTime);
    }

    // Other methods for displaying search results and handling user requests
    // ...
}
