package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bach.androidhems.EchoNET.EchoStart;
import com.bach.androidhems.Receiver.DemoReceivable;
import com.bach.androidhems.Receiver.DevicesFoundList;
import com.bach.androidhems.Receiver.MyBatteryReceiver;
import com.bach.androidhems.Receiver.MyEVReceiver;
import com.bach.androidhems.Receiver.MyLightReceiver;
import com.bach.androidhems.Receiver.MySolarReceiver;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DemonstrationActivity extends AppCompatActivity{

    EchoStart echoStart;
    private ImageView evImg;
    private TextView evTextView;
    private ImageView evArrow;
    private TextView evInstantanTextView;
    private ImageView batteryImg;
    private TextView batteryTextView;
    private ImageView batteryArrow;
    private TextView batteryInstantanTextView;
    private ImageView solarImg;
    private TextView solarTextView;
    private ImageView solaryArrow;
    private ImageView lightImg;
    private TextView lightTextView;
    private TextView homeTotalTextView;
    private TextView solarInstantanTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demonstration);


        //EV layout components
        evImg = findViewById(R.id.demo_ev_img);
        evTextView = findViewById(R.id.demo_ev_text_view);
        evArrow = findViewById(R.id.demo_ev_arrow);
        evInstantanTextView = findViewById(R.id.demo_ev_instantan_label);
        //Battery layout components
        batteryImg = findViewById(R.id.demo_battery_img);
        batteryTextView = findViewById(R.id.demo_battery_text_view);
        batteryArrow = findViewById(R.id.demo_battery_arrow);
        batteryInstantanTextView = findViewById(R.id.demo_battery_instantan_label);
        //Solar layout components
        solarImg = findViewById(R.id.demo_solar_img);
        solarTextView = findViewById(R.id.demo_solar_text_view);
        solaryArrow = findViewById(R.id.demo_solar_arrow);
        solarInstantanTextView = findViewById(R.id.demo_solar_instantan_label);
        //Light layout components
        lightImg = findViewById(R.id.demo_light_img);
        lightTextView = findViewById(R.id.demo_light_text_view);
        //
        homeTotalTextView = findViewById(R.id.demo_home_text_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Echo.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                echoStart = new EchoStart(getBaseContext());
            }
        });
        lookForDevice();
        Log.d("Echo:", "run: "+DevicesFoundList.getSize());
    }

    private void lookForDevice(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        echoStart.addDeviceList();
                        //Generate Devices
                        long homeTotalElectric = 0;
                        for(DeviceObject object : DevicesFoundList.deviceObjects){
                            switch (object.getEchoClassCode()){
                                case 638:
                                    homeTotalElectric = generateEV(homeTotalElectric);
                                    break;
                                case 637:
                                    homeTotalElectric = generateBattery(homeTotalElectric);
                                    break;
                                case 633:
                                    homeTotalElectric = generateSolar(homeTotalElectric);
                                    break;
                                case 656:
                                    generateLight();
                                    break;
                            }
                        }
                        homeTotalTextView.setText(String.valueOf(homeTotalElectric));
                    }
                });
            }
        },100);
    }

    public long generateEV(long total){
        evImg.setImageResource(R.mipmap.ev_foreground);
        evTextView.setText(MyEVReceiver.instantaneousValue);
        evInstantanTextView.setVisibility(View.VISIBLE);
        if(MyEVReceiver.operationStatus.equals("ON")){
            if(MyEVReceiver.operationMode.equals("Charging")){
                evArrow.setVisibility(View.VISIBLE);
                evArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                evArrow.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.charging_top));
                return total - Long.parseLong(MyEVReceiver.instantaneousValue);
            }
            if(MyEVReceiver.operationMode.equals("Discharging")){
                evArrow.setVisibility(View.VISIBLE);
                evArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                evArrow.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.discharging_top));
                return total + Long.parseLong(MyEVReceiver.instantaneousValue);
            }
        }
        return total;
    }

    public long generateBattery(long total){
        batteryImg.setImageResource(R.mipmap.battery_foreground);
        batteryTextView.setText(MyBatteryReceiver.instantaneousValue);
        batteryInstantanTextView.setVisibility(View.VISIBLE);
        if(MyBatteryReceiver.operationStatus.equals("ON")){
            if(MyBatteryReceiver.operationMode.equals("Charging")){
                batteryArrow.setVisibility(View.VISIBLE);
                batteryArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                batteryArrow.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.charging_top));
                return total - Long.parseLong(MyBatteryReceiver.instantaneousValue);
            }
            if(MyBatteryReceiver.operationMode.equals("Discharging")){
                batteryArrow.setVisibility(View.VISIBLE);
                batteryArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                batteryArrow.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.discharging_top));
                return total + Long.parseLong(MyBatteryReceiver.instantaneousValue);
            }
        }
        return total;
    }

    public long generateSolar(long total){
        solarImg.setImageResource(R.mipmap.solar_foreground);
        solarTextView.setText(MySolarReceiver.instantaneousValue);
        solarInstantanTextView.setVisibility(View.VISIBLE);
        if(MySolarReceiver.operationStatus.equals("ON") && !MySolarReceiver.instantaneousValue.equals("0")){
            solaryArrow.setVisibility(View.VISIBLE);
            solaryArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
            solaryArrow.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.discharging_bottom));

            return total + Long.parseLong(MySolarReceiver.instantaneousValue);
        }
        return total;
    }

    public void generateLight(){
        lightImg.setImageResource(R.mipmap.light_foreground);
        lightTextView.setText(MyLightReceiver.operationStatus);
    }
}


