package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bach.androidhems.EchoNET.EchoConfAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigDevice extends AppCompatActivity {

    private String device;
    private ArrayList<String> data;
    private String ipAddress;
    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_device);
        Intent intent = getIntent();
        data = intent.getStringArrayListExtra("deviceData");
        device = intent.getStringExtra("device");
        ipAddress = intent.getStringExtra("ipAddress");
        image = intent.getIntExtra("image",0);
        //Layout handle

        ImageView imageView = findViewById(R.id.device_image);
        TextView status = findViewById(R.id.status_text);
        TextView modeLabel = findViewById(R.id.mode_label);
        TextView mode = findViewById(R.id.mode_text);
        TextView instantanLabel = findViewById(R.id.instantan_label);
        TextView instantan = findViewById(R.id.instantan_text);
        TextView totalLabel = findViewById(R.id.total_label);
        TextView total = findViewById(R.id.total_text);

        switch (device){
            case "ev":
                imageView.setImageResource(R.mipmap.ev);
                break;
        }
    }

    public void onClickStatusOn(View view){
        HashMap map = new HashMap();
        map.put("device",device);
        map.put("status","30");
        map.put("ipAddress",ipAddress);
        new EchoConfAsyncTask(map).execute();
    }

    public void onClickStatusOff(View view){
        HashMap map = new HashMap();
        map.put("device",device);
        map.put("status","31");
        map.put("ipAddress",ipAddress);
        new EchoConfAsyncTask(map).execute();
    }
}
