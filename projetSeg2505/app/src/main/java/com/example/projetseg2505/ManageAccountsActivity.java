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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageAccountsActivity extends AppCompatActivity {
    DatabaseReference accountsDB;
    ListView listViewAccounts;
    List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        //instantiating all required objects for this class
        accountsDB = FirebaseDatabase.getInstance().getReference("users");
        listViewAccounts = findViewById(R.id.listViewAccounts);
        accounts = new ArrayList<>();

        //set on long click listener for the accounts
        listViewAccounts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = accounts.get(position);
                showUpdateDeleteDialog(account.getUserName(), account.getPassword());
                return true;
            }
        });


    }

    protected void onStart(){
        super.onStart();

        //attaching a value event listener to keep acccounts list up to date
        accountsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clearing the previous accounts list
                accounts.clear();

                //iterating through all the nodes of the accounts path
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    //get account
                    Account acc = postSnapshot.getValue(Account.class);
                    //add it to the list
                    accounts.add(acc);

                }
                //create an adapter
                AccountList accountAdapter = new AccountList(ManageAccountsActivity.this, accounts);
                //attach the adapter to the list view for display
                listViewAccounts.setAdapter(accountAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showUpdateDeleteDialog(final String accountName, String password) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextUsername = (EditText) dialogView.findViewById(R.id.editTextInfo1);
        editTextUsername.setText(accountName);
        final EditText editTextPassword  = (EditText) dialogView.findViewById(R.id.editTextInfo2);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(accountName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                //validation du contenu dans le userName edit text
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(ManageAccountsActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                //username ne doit contenir que des chiffres et des lettres
                String accountNamePattern = "^[a-zA-Z0-9]+$";
                if(!name.matches(accountNamePattern)){
                    Toast.makeText(ManageAccountsActivity.this, "Username must only contain letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                //doit inclure un mot de passe
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(ManageAccountsActivity.this, "Enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //mot de passe doit etre au moins 8 length
                if(password.length() < 8){
                    Toast.makeText(ManageAccountsActivity.this, "Password must length must be >= 8", Toast.LENGTH_SHORT).show();
                    return;
                }
                    updateUser(name, password);
                    b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(accountName);
                b.dismiss();
            }
        });
    }

    private boolean deleteProduct(String id) {
        // get the reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);

        //remove the id
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "account deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    private void updateUser(String id, String password) {
        //get reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(id);

        //updating product
        reference.child("userName").setValue(id);
        reference.child("password").setValue(password);

        Toast.makeText(getApplicationContext(), "account updated", Toast.LENGTH_LONG).show();
    }
}