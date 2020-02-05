package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bach.androidhems.EchoNET.EchoListAdapter;
import com.bach.androidhems.Receiver.DevicesFoundList;
import com.bach.androidhems.Receiver.MyBatteryReceiver;
import com.bach.androidhems.Receiver.MyEVReceiver;
import com.bach.androidhems.Receiver.MyLightReceiver;
import com.bach.androidhems.Receiver.MySolarReceiver;
import com.bach.androidhems.Receiver.Refreshable;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import com.sonycsl.echo.eoj.device.housingfacilities.GeneralLighting;
import com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.processing.defaults.DefaultController;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class FindDevices extends AppCompatActivity implements Refreshable {

    //    Intent intent ;
    DevicesFoundList devices = new DevicesFoundList();
    Refreshable refreshable = this;
    LinearLayout mainLinear;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_devices);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Echo:", "onResume: ");
        findDevice();
    }

    private void findDevice() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Echo.start(new DefaultNodeProfile(), new DeviceObject[]{
                            new DefaultController()
                    });

                    Echo.addEventListener(new Echo.EventListener() {

                        @Override
                        public void onNewElectricVehicle(ElectricVehicle device) {
                            super.onNewElectricVehicle(device);
                            Log.d("Echo:", "Electric vehicle found.");

                            device.setReceiver(new MyEVReceiver(getBaseContext()));
                            try {
                                device.get().reqGetOperationStatus().send();
                                device.get().reqGetOperationModeSetting().send();
                                device.get().reqGetMeasuredInstantaneousChargeDischargeElectricEnergy().send();
                                device.get().reqGetRemainingBatteryCapacity1().send();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNewBattery(Battery device) {
                            super.onNewBattery(device);
                            Log.d("Echo:", "Battery found.");
                            device.setReceiver(new MyBatteryReceiver(getBaseContext()));
                            try {
                                device.get().reqGetOperationStatus().send();
                                device.get().reqGetOperationModeSetting().send();
                                device.get().reqGetMeasuredInstantaneousChargeDischargeElectricEnergy().send();
                                device.get().reqGetRemainingStoredElectricity1().send();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNewGeneralLighting(GeneralLighting device) {
                            super.onNewGeneralLighting(device);
                            Log.d("Echo:", "GeneralLighting found.");
                            device.setReceiver(new MyLightReceiver());
                            try {
                                device.get().reqGetOperationStatus().send();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNewHouseholdSolarPowerGeneration(HouseholdSolarPowerGeneration device) {
                            super.onNewHouseholdSolarPowerGeneration(device);
                            Log.d("Echo:", "HouseholdSolarPowerGeneration found.");
                            device.setReceiver(new MySolarReceiver(getBaseContext()));
                            try {
                                device.get().reqGetOperationStatus().send();
                                device.get().reqGetMeasuredInstantaneousAmountOfElectricityGenerated().send();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                    NodeProfile.informG().reqInformSelfNodeInstanceListS().send();

                } catch (Exception e) {
                    Log.d("Echo: ", "run: " + e.getMessage());
                }
                //-----------
                generateLayout();
                Log.d("Echo:", "generateLayout");
            }
        }).start();

    }
    int i = 0;
    private void generateLayout(){
        i++;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceList();
                        Log.d("Echo:", "Device Size: "+devices.getSize());
                        if (devices == null || devices.getSize() == 0) {
                            deviceNotFound(getBaseContext());
                        }else{
                            try{
                                mainLinear.removeView(findViewById(R.id.device_not_found));
                            }catch (NullPointerException ex){
                                Log.d("Echo:", "Found devices!");
                            }
                        }
                        EchoListAdapter adapter = new EchoListAdapter(getBaseContext(), R.layout.device_layout, devices.getDevicesList());
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

    private void getDeviceList() {
//        ArrayList<DeviceObject> devices = new ArrayList<>();
        devices.removeAll();
        for (EchoNode node : Echo.getNodes()) {
            Log.d("Echo", "Node profile:" + node.getNodeProfile());
            if (node != Echo.getSelfNode()) {
                DeviceObject[] objects = node.getDevices();
                for (DeviceObject object : objects) {
                    Log.d("Echo", "Class code:" + object.getEchoClassCode());
                    devices.addDevice(object);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            Echo.clear();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    //if none of device is found, display error screen
    public void deviceNotFound(Context context) {
        if(mainLinear != null) {
            mainLinear.removeView(findViewById(R.id.device_not_found));
        }
        Log.d("Echo:", "deviceNotFound: check");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mainLinear = findViewById(R.id.activity_find_devices);
        View view = layoutInflater.inflate(R.layout.device_not_found, mainLinear, true);
    }

    @Override
    public void reGenerateTotal(String input) {
        if(input != null){
            Log.d("Echo", "reGenerateTotal: "+input);
            TextView totalTextView = findViewById(R.id.total_text);
            totalTextView.setText(input);
        }
    }

    //add View of device to screen

}
