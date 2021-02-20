package com.cyberlabs.linkshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText fname;
    EditText lname;
    EditText dob;
    EditText mail;
    EditText pass;
    EditText rpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        dob=findViewById(R.id.dob);
        mail=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        rpass=findViewById(R.id.rpass);
    }

    public void signup(View view) {
        String firstName,lastName,dateOfBirth,email,password,rpassword;
        firstName=fname.getText().toString().trim();
        lastName=lname.getText().toString().trim();
        dateOfBirth=dob.getText().toString().trim();
        email=mail.getText().toString().trim();

    }
}