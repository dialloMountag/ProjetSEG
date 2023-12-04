package com.example.projetseg2505;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextPassword;

    TextView loginRedirect;

    Button buttonReg;

    RadioButton rbEmployee;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btnRegister);
        loginRedirect = findViewById(R.id.loginNow);
        rbEmployee = findViewById(R.id.rbEmployee);


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, type;
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                if(rbEmployee.isChecked()){
                    type = "employee";
                } else {
                    type = "client";
                }

                //validation du contenu dans le userName edit text
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                //username ne doit contenir que des chiffres et des lettres
                String accountNamePattern = "^[a-zA-Z0-9]+$";
                if(!username.matches(accountNamePattern)){
                    Toast.makeText(Register.this, "Username must only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                //doit inclure un mot de passe
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //mot de passe doit etre au moins 8 length
                if(password.length() < 8){
                    Toast.makeText(Register.this, "Password must length must be >= 8", Toast.LENGTH_SHORT).show();
                    return;
                }

                //get the database instance / reference
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                //create an Account object of the specified type
                Account newUser = new Account(username, password, type);

                //add the instance to the realtime database
                reference.child(username).setValue(newUser);

                Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();


            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}