package com.jce.ant.project;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

public class OrderForm extends AppCompatActivity {
    EditText name, mail, phone, address, city, zip, pob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        zip = (EditText) findViewById(R.id.zip);
        pob = (EditText) findViewById(R.id.pob);
        mail = (EditText) findViewById(R.id.mail);
        phone = (EditText) findViewById(R.id.phone);



        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8
        // Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                //String possibleEmail = account.name;
                mail.setText(account.name);

            }
        }

       /* TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();*/
    }
}
