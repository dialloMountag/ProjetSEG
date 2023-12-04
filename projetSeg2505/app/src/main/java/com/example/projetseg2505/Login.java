package com.example.projetseg2505;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    TextInputEditText loginUserName, loginPassword;

    TextView regRedirect;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUserName = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);
        regRedirect = findViewById(R.id.registerNow);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()){
                    Toast.makeText(Login.this, "check credentials", Toast.LENGTH_SHORT).show();
                } else {
                    checkUser();
                }
            }
        });

        regRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public Boolean validateUsername(){
        String val = loginUserName.getText().toString();
        if (val.isEmpty()){
            loginUserName.setError("Username cannot be empty");
            return false;
        } else {
            loginUserName.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userName = loginUserName.getText().toString();
        String userPassword = loginPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userName").equalTo(userName);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUserName.setError(null);
                    String passwordFromDB = snapshot.child(userName).child("password").toString();
                    String username = snapshot.child(userName).child("userName").toString();

                    if(passwordFromDB != userPassword){
                        loginUserName.setError(null);
                        String userType = snapshot.child(userName).child("type").getValue(String.class);
                        Intent intent;
                        if(Objects.equals(userType, "admin")){
                            intent = new Intent(getApplicationContext(), AdminActivity.class);
                        } else if(Objects.equals(userType, "employee")){
                            intent = new Intent(getApplicationContext(), EmployeeActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), ClientActivity.class);
                        }
                        Toast.makeText(Login.this, "login successful", Toast.LENGTH_SHORT).show();
                        intent.putExtra("name_key", userName);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("invalid credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUserName.setError("account does not exist");
                    loginUserName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}