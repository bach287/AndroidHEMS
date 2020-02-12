package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.bach.androidhems.EchoNET.EchoListAdapter;
import com.bach.androidhems.EchoNET.EchoStart;
import com.bach.androidhems.Receiver.DevicesFoundList;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class FindDevices extends AppCompatActivity {

    //    Intent intent ;
    ArrayList<DeviceObject> deviceObjects;
    LinearLayout mainLinear;
    ListView listView;
    EchoStart echoStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Echo:", "onResume: ");
        try {
            Echo.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        findDevice();
    }

    private void findDevice() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                echoStart = new EchoStart(getBaseContext());
                generateLayout();
                Log.d("Echo:", "generateLayout");
            }
        }).start();
    }
    private void generateLayout(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        echoStart.addDeviceList();
                        deviceObjects = DevicesFoundList.getDevicesList();
                        Log.d("Echo:", "Device Size: "+deviceObjects.size());
                        if (deviceObjects == null || deviceObjects.size() == 0) {
                            deviceNotFound(getBaseContext());
                        }else{
                            try{
                                mainLinear.removeView(findViewById(R.id.device_not_found));
                            }catch (NullPointerException ex){
                                Log.d("Echo:", "Found devices!");
                            }
                        }
                        EchoListAdapter adapter = new EchoListAdapter(getBaseContext(), R.layout.device_layout, deviceObjects);
                        listView = findViewById(R.id.deviceListView);
                        listView.setAdapter(adapter);
                    }
                });
            }
        }, 100);
    }

    public void onClickRefresh(View view) {
        try {
            Echo.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        findDevice();
    }

    //if none of device is found, display error screen
    public void deviceNotFound(Context context) {
        if(mainLinear != null) {
            mainLinear.removeView(findViewById(R.id.device_not_found));
        }
        Log.d("Echo:", "deviceNotFound: check");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mainLinear = findViewById(R.id.activity_find_devices);
        layoutInflater.inflate(R.layout.device_not_found, mainLinear, true);
    }


}
