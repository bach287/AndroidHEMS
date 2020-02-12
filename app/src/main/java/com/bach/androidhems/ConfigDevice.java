package com.bach.androidhems;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bach.androidhems.EchoNET.EchoNETLiteData;
import com.bach.androidhems.Receiver.DataHandle;
import com.bach.androidhems.Receiver.DevicesFoundList;
import com.bach.androidhems.Receiver.MyBatteryReceiver;
import com.bach.androidhems.Receiver.MyEVReceiver;
import com.bach.androidhems.Receiver.MyLightReceiver;
import com.bach.androidhems.Receiver.MySolarReceiver;
import com.bach.androidhems.Receiver.Receivable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration;
import com.sonycsl.echo.node.EchoNode;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;

public class ConfigDevice extends AppCompatActivity implements Receivable {

    private String name;
    private int position;
//    private String statusValue;
//    private String modeValue;
//    private String instantanValue;
//    private String totalValue;
    private DeviceObject deviceObject;

    private ImageView imageView;
    private TextView statusTextView;
    private TextView modeLabel ;
    private TextView modeTextView ;
    private TextView instantanLabel;
    private TextView instantanTextView;
    private TextView totalLabel;
    private TextView totalTextView;
    private Spinner modeSpinner;
    private TextInputLayout instantanInput;
    private TextInputEditText instantanInputEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_device);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        position  = intent.getIntExtra("position",-1);
        //Layout handle

        imageView = findViewById(R.id.device_image);
        statusTextView = findViewById(R.id.status_text);
        modeLabel = findViewById(R.id.mode_label);
        modeTextView = findViewById(R.id.mode_text);
        instantanLabel = findViewById(R.id.instantan_label);
        instantanTextView = findViewById(R.id.instantan_text);
        totalLabel = findViewById(R.id.total_label);
        totalTextView = findViewById(R.id.total_text);
        modeSpinner = findViewById(R.id.mode_spinner);
        instantanInput = findViewById(R.id.instantan_input);
        instantanInputEdit = findViewById(R.id.instantan_edit_text);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, DataHandle.modesArray);
        switch (name){
            case "ev":
                MyEVReceiver.onReceive = this;
                if(position != -1){
                    deviceObject = DevicesFoundList.deviceObjects.get(position);
                }
                imageView.setImageResource(R.mipmap.ev);
                statusTextView.setText(MyEVReceiver.operationStatus);
                modeTextView.setText(MyEVReceiver.operationMode);
                instantanTextView.setText(MyEVReceiver.instantaneousValue);
                totalTextView.setText(MyEVReceiver.totalRemaining);
                modeSpinner.setAdapter(spinnerAdapter);
                break;
            case "battery":
                //Assign interface to callback asynchronous function
                MyBatteryReceiver.onReceive = this;
                if(position != -1){
                    deviceObject = DevicesFoundList.deviceObjects.get(position);
                }
                imageView.setImageResource(R.mipmap.battery);
                statusTextView.setText(MyBatteryReceiver.operationStatus);
                modeTextView.setText(MyBatteryReceiver.operationMode);
                instantanTextView.setText(MyBatteryReceiver.instantaneousValue);
                totalTextView.setText(MyBatteryReceiver.totalRemaining);
                modeSpinner.setAdapter(spinnerAdapter);
                break;
            case "solar":
                MySolarReceiver.onReceive = this;
                if(position != -1){
                    deviceObject = DevicesFoundList.deviceObjects.get(position);
                }
                imageView.setImageResource(R.mipmap.solar);
                statusTextView.setText(MySolarReceiver.operationStatus);
                instantanTextView.setText(MySolarReceiver.instantaneousValue);
                modeTextView.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                findViewById(R.id.mode_spinner).setVisibility(View.GONE);
                break;
            case "light":
                MyLightReceiver.onReceive = this;
                if(position != -1){
                    deviceObject = DevicesFoundList.deviceObjects.get(position);
                }
                imageView.setImageResource(R.mipmap.light);
                statusTextView.setText(MySolarReceiver.operationStatus);
                modeTextView.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                instantanTextView.setVisibility(View.GONE);
                instantanLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                findViewById(R.id.end_time_picker).setVisibility(View.GONE);
                findViewById(R.id.start_time_picker).setVisibility(View.GONE);
                findViewById(R.id.instantan_input).setVisibility(View.GONE);
                findViewById(R.id.set_btn).setVisibility(View.GONE);
                findViewById(R.id.mode_spinner).setVisibility(View.GONE);

                break;

        }
    }

    public void onClickStatusOn(View view){
        try {
            deviceObject.set().reqSetOperationStatus(new byte[]{(byte) 0x30}).send();
        } catch (IOException e) {
            Log.d("Echo:", "onClickStatusOn Failed :" + e);
        }
    }

    public void onClickStatusOff(View view){
        try {
            deviceObject.set().reqSetOperationStatus(new byte[]{(byte) 0x31}).send();
        } catch (IOException e) {
            Log.d("Echo:", "onClickStatusOff Failed "+ e);
        }
    }

    public void onClickSetBtn(View view){
        String edt = "";
        String mode;
        Button startTimeBtn = findViewById(R.id.start_time_picker);
        Button endTimeBtn = findViewById(R.id.end_time_picker);
        String startTime = startTimeBtn.getText().toString();
        String endTime = endTimeBtn.getText().toString();
        Editable instantan = instantanInputEdit.getText();

        if(!startTime.equals("") && !endTime.equals("")){
            edt = DataHandle.timeHandle(startTime).concat(DataHandle.timeHandle(endTime));
        }else{
            displayError(getResources().getString(R.string.time_not_null));
            return;
        }

        switch (name){
            case "solar":
                if (instantan != null && instantan.length() != 0 && instantan.length() <= 9){
                    String instantanValue = instantan.toString();
                    instantanValue = Long.toHexString(Long.parseLong(instantanValue));
                    while (instantanValue.length() < 4){
                        instantanValue = "0".concat(instantanValue);
                    }
                    edt = edt.concat(instantanValue);
                    instantanInput.setError("");
                }else{
                    instantanInput.setError(getResources().getString(R.string.instantan_not_null));
                    return;
                }
                Log.d("Echo:", "onClickSetBtn: edt:" + edt);
                try {
                    ((HouseholdSolarPowerGeneration)deviceObject).set().reqSetProperty(DataHandle.hexStringToByteArray("ff")[0],DataHandle.hexStringToByteArray(edt)).send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "ev":
                mode = modeSpinner.getSelectedItem().toString();
                edt = edt.concat(DataHandle.modeReverter(mode));
                if( !mode.equals("Standby")){
                    if (instantan != null && instantan.length() != 0 && instantan.length() <= 9 ){
                        String instantanValue = instantan.toString();
                        instantanValue = Long.toHexString(Long.parseLong(instantanValue));
                        while (instantanValue.length() < 8){
                            instantanValue = "0".concat(instantanValue);
                        }
                        edt = edt.concat(instantanValue);
                    }else{
                        instantanInput.setError(getResources().getString(R.string.instantan_not_null));
                        return;
                    }
                }else{
                    edt = edt.concat("00000000");
                }
                instantanInput.setError("");
                Log.d("Echo:", "onClickSetBtn: edt:" + edt);
                try {
                    ((ElectricVehicle)deviceObject).set().reqSetProperty(DataHandle.hexStringToByteArray("ff")[0],DataHandle.hexStringToByteArray(edt)).send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "battery":
                mode = modeSpinner.getSelectedItem().toString();
                edt = edt.concat(DataHandle.modeReverter(mode));
                if(!mode.equals("Standby")){
                    if (instantan != null && instantan.length() != 0 && instantan.length() <= 9){
                        String instantanValue = instantan.toString();
                        instantanValue = Long.toHexString(Long.parseLong(instantanValue));
                        while (instantanValue.length() < 8){
                            instantanValue = "0".concat(instantanValue);
                        }
                        edt = edt.concat(instantanValue);
                    }else{
                        instantanInput.setError(getResources().getString(R.string.instantan_not_null));
                        return;
                    }
                }else{
                    edt = edt.concat("00000000");
                }
                instantanInput.setError("");
                Log.d("Echo:", "onClickSetBtn: edt:" + edt);
                try {
                    ((Battery)deviceObject).set().reqSetProperty(DataHandle.hexStringToByteArray("ff")[0],DataHandle.hexStringToByteArray(edt)).send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    public void onClickShowStartTimePicker(View view){
        final Button startTimeText = view.findViewById(R.id.start_time_picker);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTimeText.setText(hourOfDay + ":" + minute);
            }
        },hour,minute, true);
        mTimePicker.setTitle("Select Start Time");
        mTimePicker.show();
    }

    public void onClickShowEndTimePicker(View view){
        final Button endTimeText = view.findViewById(R.id.end_time_picker);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ConfigDevice.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTimeText.setText(hourOfDay + ":" + minute);
            }
        },hour,minute, true);
        mTimePicker.setTitle("Select End Time");
        mTimePicker.setContentView(R.layout.time_picker_layout);
        mTimePicker.show();
    }

    private void displayError(final String mess){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), mess,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void displayMessage(final String mess) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), mess,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void reGenerateStatus(String status) {
        statusTextView.setText(status);
    }

    @Override
    public void reGenerateMode(String mode) {
        modeTextView.setText(mode);
    }

    @Override
    public void reGenerateInstantan(String instantan) {
        instantanTextView.setText(instantan);
    }
}
