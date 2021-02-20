package com.cyberlabs.linkshortener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class linkmain extends AppCompatActivity {
    EditText slink;
    EditText lurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkmain);
        slink=findViewById(R.id.shorturl);
        lurl=findViewById(R.id.longurl);
    }

    public void copy(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Short URL",slink.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    public void paste(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData=clipboard.getPrimaryClip();
        ClipData.Item item=clipData.getItemAt(0);
        lurl.setText(item.getText().toString());
    }
}