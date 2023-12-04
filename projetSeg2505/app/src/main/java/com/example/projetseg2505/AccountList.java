package com.example.projetseg2505;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AccountList extends ArrayAdapter<Account> {

    private Activity context;

    List<Account> accounts;

    public AccountList(Activity context, List<Account> accounts){
        //constructor
        super(context, R.layout.activity_manage_accounts, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_account_list, null, true);

        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);

        Account account = accounts.get(position);
        textViewUsername.setText(account.getUserName());
        return listViewItem;

    }
}

