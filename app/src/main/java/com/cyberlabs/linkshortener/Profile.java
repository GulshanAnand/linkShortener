package com.cyberlabs.linkshortener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Profile extends AppCompatActivity {
    EditText name;
    EditText email;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=(EditText) findViewById(R.id.name11);
        email=(EditText) findViewById(R.id.email11);
        pb=findViewById(R.id.pb);
        setUserInfo();

    }
    void setUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());

        } else {
            startActivity(new Intent(Profile.this,MainActivity.class));
        }
    }

    public void chpass(View view) {
        startActivity(new Intent(Profile.this,changepass.class));
    }

    public void editName(View view) {
        name.setEnabled(true);
        name.requestFocus();
        InputMethodManager imm = (InputMethodManager) Profile.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        name.setSelection(name.getText().length());

    }

    public void editEmail(View view) {
        email.setEnabled(true);
        email.requestFocus();
        InputMethodManager imm = (InputMethodManager) Profile.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        email.setSelection(email.getText().length());
    }

    public void updateProfile(View view) {
        String mynewname=name.getText().toString().trim();
        String mynewemail=email.getText().toString().trim();

        if(mynewname.isEmpty()){
            name.setError("Please enter your Name");
            name.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mynewemail).matches()||mynewemail.isEmpty()){
            email.setError("Please Enter a valid email");
            email.requestFocus();
        }
        else{
            pb.setVisibility(View.VISIBLE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mynewname)
                    .setPhotoUri(null)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                user.updateEmail(mynewemail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    pb.setVisibility(View.INVISIBLE);
                                                    name.setEnabled(false);
                                                    email.setEnabled(false);
                                                    Toast.makeText(Profile.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                                                    Log.d("user email", "User email address updated.");
                                                }
                                                else{
                                                    pb.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                Log.d("user profile", "User profile updated.");
                            }
                            else{
                                pb.setVisibility(View.INVISIBLE);
                                Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
}