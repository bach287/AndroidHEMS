package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bach.androidhems.EchoNET.EchoAsyncTask;
import com.sonycsl.echo.Echo;

import java.io.IOException;
import java.util.ArrayList;


public class FindDevices extends AppCompatActivity {
    public static ArrayList<String> devices ;
    EchoAsyncTask echoAsyncTask;
//    Intent intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);
        echoAsyncTask = new EchoAsyncTask(FindDevices.this);
        echoAsyncTask.execute();
    }

    public void onClickRefresh(View view){
        finish();
        overridePendingTransition(0, 0);
        startActivity(this.getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Echo.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //if none of device is found, display error screen
    public void deviceNotFound(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout mainLinear = findViewById(R.id.activity_find_devices);
        View view = layoutInflater.inflate(R.layout.device_not_found, mainLinear, false);
        view.setMinimumHeight(new DisplayMetrics().heightPixels);
        mainLinear.addView(view);
    }

    //add View of device to screen

}
