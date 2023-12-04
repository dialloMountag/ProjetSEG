package com.example.projetseg2505;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeActivity extends AppCompatActivity {
    TextView createLocationText;
    TextView manageLocationText;
    TextView viewRequestsText;
    Button btnLogout;
    TextView helloUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);



        //assign the buttons and stuff
        helloUser = findViewById(R.id.userDetails);
        createLocationText = findViewById(R.id.textViewCreateLocation);
        viewRequestsText = findViewById(R.id.textViewSeeRequests);
        manageLocationText = findViewById(R.id.textViewManageLocation);
        String username = getIntent().getStringExtra("name_key");
        helloUser.setText("Welcome, " + username + '!');
        btnLogout = findViewById(R.id.searchBtn);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        createLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("locations");
                Query checkLocationDB = reference.orderByChild("userName").equalTo(username);
                checkLocationDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(EmployeeActivity.this, "Account already associated to a location.", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), CreateLocationActivity.class);
                            intent.putExtra("name_key", getIntent().getStringExtra("name_key"));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        manageLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("locations");
                Query checkLocationDB = reference.orderByChild("userName").equalTo(username);
                checkLocationDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Intent intent = new Intent(getApplicationContext(), ManageLocationActivity.class);
                            intent.putExtra("name_key", getIntent().getStringExtra("name_key"));
                            startActivity(intent);
                        } else {
                            Toast.makeText(EmployeeActivity.this, "No location associated to this account, please create one.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        viewRequestsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewRequestActivity.class);
                startActivity(intent);
            }
        });
    }
}