package com.cyberlabs.linkshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class linkmain extends AppCompatActivity {
    EditText slink;
    EditText lurl;
    ProgressBar pb;
    String longURL;
    String shortURL;
    int st;
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
                pb.setVisibility(View.GONE);

            } catch (IOException e) {

                e.printStackTrace();
                pb.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
                pb.setVisibility(View.GONE);
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
                pb.setVisibility(View.GONE);
            }
            else if(st==2){

                Toast.makeText(linkmain.this, "The entered link is not a link",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
            else if(st==3){
                Toast.makeText(linkmain.this, "The preferred link name is already taken",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
            else if(st==4){
                Toast.makeText(linkmain.this, "Invalid API key",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
            else if(st==5){
                Toast.makeText(linkmain.this, "The link includes invalid characters",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
            else if(st==6){
                Toast.makeText(linkmain.this, "The link provided is from a blocked domain",
                        Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
                slink.setText(shortURL);
                pb.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkmain);
        slink=findViewById(R.id.shorturl);
        lurl=findViewById(R.id.longurl);
        pb=findViewById(R.id.progressBar);
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
        if(longURL.matches("")){
            Toast.makeText(linkmain.this, "Please enter a link",
                    Toast.LENGTH_SHORT).show();
        }
        else{
        String req="https://cutt.ly/api/api.php?key=84dd6a433d68145a5e406b6ebf23bf3321bc4&short="+longURL+"&name=";
        pb.setVisibility(View.VISIBLE);
        BG myTask = new BG();
        myTask.execute(req);
        }
    }
}