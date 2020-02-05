package com.bach.androidhems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Echo", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageButton button = this.findViewById(R.id.button);
        button.setClickable(true);
    }


    public void onClickFind(View view){
        ImageButton button = view.findViewById(R.id.button);
        button.setClickable(false);
        if(!isNetWorkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NetWork Error!");
            builder.setMessage("Please enable Wifi!");
            AlertDialog dialog = builder.create();
            dialog.show();
            button.setClickable(true);
            return;
        }
        Intent findDevice = new Intent(this,FindDevices.class);
        this.startActivity(findDevice);
    }

    public void onClickJapan(View view){
        Locale locale = new Locale("ja");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(
                config,getResources().getDisplayMetrics()
        );

        //It is required to recreate the activity to reflect the change in UI.
        recreate();
    }

    public void onClickVietnamese(View view){
        Locale locale = new Locale("vi");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(
                config,getResources().getDisplayMetrics()
        );

        //It is required to recreate the activity to reflect the change in UI.
        recreate();
    }

    public void onClickEnglish(View view){
        Locale locale = new Locale("");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(
                config,getResources().getDisplayMetrics()
        );

        //It is required to recreate the activity to reflect the change in UI.
        recreate();
    }

    public boolean isNetWorkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
