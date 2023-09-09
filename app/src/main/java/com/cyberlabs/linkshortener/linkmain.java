package com.cyberlabs.linkshortener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class linkmain extends AppCompatActivity {
    EditText slink;
    EditText lurl, etalias;
    RadioButton cuttly, lab;
    ProgressBar pb;
    String longURL;
    String shortURL;
    int st;

    public class BG2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Pre", "onPreExecute: ran");
        }
        @Override
        protected String doInBackground(String... urls) {
            String output="";
            try {

                URL url = new URL(urls[0]);
                String jsonData = urls[1];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
                conn.setRequestProperty("Content-Length", String.valueOf(jsonDataBytes.length));

                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                outputStream.write(jsonDataBytes);
                outputStream.flush();
                outputStream.close();

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                JSONObject obj = new JSONObject(br.readLine());
                int status = obj.getInt("status");
                st=status;
                if(status==2){
                    output = obj.getString("shortLink");
                }

                conn.disconnect();

            } catch (MalformedURLException e) {

                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);

            } catch (IOException e) {

                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);
            }
            Log.d("Bg", "doInBackground: ran");
            return output;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Post", "onPostExecute: ran");
            shortURL=s;
            if(st==1){
                Toast.makeText(linkmain.this, "The link has already been shortened",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==2){

                Toast.makeText(linkmain.this, "Link has been shortened",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==3){
                Toast.makeText(linkmain.this, "The preferred link name is already taken",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==4){
                Toast.makeText(linkmain.this, "Invalid API key",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==5){
                Toast.makeText(linkmain.this, "The link includes invalid characters",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==6){
                Toast.makeText(linkmain.this, "The link provided is from a blocked domain",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            slink.setText(shortURL);
            pb.setVisibility(View.INVISIBLE);
            if(!shortURL.matches("")){
                updatedb();
                Toast.makeText(linkmain.this, "Link has been shortened",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class BG extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Pre", "onPreExecute: ran");
        }
        @Override
        protected String doInBackground(String... urls) {
            String output="";
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                JSONObject obj = new JSONObject(br.readLine());
                int status = obj.getJSONObject("url").getInt("status");
                st=status;
                if(status==7){
                    output = obj.getJSONObject("url").getString("shortLink");
                }

                conn.disconnect();

            } catch (MalformedURLException e) {

                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);

            } catch (IOException e) {

                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                pb.setVisibility(View.INVISIBLE);
            }
            Log.d("Bg", "doInBackground: ran");
            return output;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Post", "onPostExecute: ran");
            shortURL=s;
            if(st==1){
                Toast.makeText(linkmain.this, "The link has already been shortened",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==2){

                Toast.makeText(linkmain.this, "The entered link is not a link",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==3){
                Toast.makeText(linkmain.this, "The preferred link name is already taken",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==4){
                Toast.makeText(linkmain.this, "Invalid API key",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==5){
                Toast.makeText(linkmain.this, "The link includes invalid characters",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
            else if(st==6){
                Toast.makeText(linkmain.this, "The link provided is from a blocked domain",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
            }
                slink.setText(shortURL);
                pb.setVisibility(View.INVISIBLE);
                if(!shortURL.matches("")){
                    updatedb();
                    Toast.makeText(linkmain.this, "Link has been shortened",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updatedb() {
        String s= Calendar.getInstance().getTime().toString();
        String currtime=s.substring(8,10) + s.substring(3,7) + s.substring(s.length()-5,s.length()) + s.substring(10,19);
        FirebaseUser usr= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Ref = db.getReference("uid");
        DatabaseReference uidRef = Ref.child(usr.getUid());
        uidRef.child(currtime);
        DatabaseReference timeRef = uidRef.child(currtime);
        timeRef.child("originalurl").setValue(longURL);
        timeRef.child("shorturl").setValue(shortURL);
        timeRef.child("time").setValue(currtime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkmain);
        slink=findViewById(R.id.shorturl);
        lurl=findViewById(R.id.longurl);
        etalias=findViewById(R.id.eAlias);
        pb=findViewById(R.id.progressBar);
        cuttly=findViewById(R.id.cuttly);
        lab=findViewById(R.id.lab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int sid=item.getItemId();
        if(sid==R.id.prof){
            startActivity(new Intent(linkmain.this,Profile.class));
            return true;
        }
        else if(sid==R.id.his){
            startActivity(new Intent(linkmain.this,history.class));
            return true;
        }
        else if(sid==R.id.sout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(linkmain.this,MainActivity.class));
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void copy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Short URL",slink.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(linkmain.this, "link copied!",
                Toast.LENGTH_SHORT).show();
    }

    public void paste(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=clipboard.getPrimaryClip();
        ClipData.Item item=clipData.getItemAt(0);
        lurl.setText(item.getText().toString());
    }

    public void gen(View view) {
        longURL=lurl.getText().toString().trim();
        String alias = etalias.getText().toString().trim();
        if(alias == null) alias = "";

        if(longURL.matches("")){
            Toast.makeText(linkmain.this, "Please enter a link",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            if(cuttly.isChecked()){
                String req="https://cutt.ly/api/api.php?key=84dd6a433d68145a5e406b6ebf23bf3321bc4&short="+longURL+"&name="+alias;
                pb.setVisibility(View.VISIBLE);
                BG myTask = new BG();
                myTask.execute(req);
            }
            else{
                String req = "https://labwired.tech/shorten";
                String jsonData = "{\"url\":\"" + longURL + "\",\"alias\":\"" + alias + "\"}";
                pb.setVisibility(View.VISIBLE);
                BG2 myTask = new BG2();
                myTask.execute(req, jsonData);
            }
        }
    }
}