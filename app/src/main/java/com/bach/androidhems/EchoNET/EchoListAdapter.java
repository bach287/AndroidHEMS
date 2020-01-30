package com.bach.androidhems.EchoNET;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bach.androidhems.ConfigDevice;
import com.bach.androidhems.R;

import java.util.List;


public class EchoListAdapter extends BaseAdapter {
    private List<EchoDevice> devices;

    private Context context;

    private int resource;

    public EchoListAdapter(Context context, int resource,  List<EchoDevice> devices) {
        this.context = context;
        this.resource = resource;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        if(devices.isEmpty()){
            return 0;
        }
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        }
        GridLayout deviceLayout = convertView.findViewById(R.id.device_layout);
        //Get View item
        TextView statusTextView = deviceLayout.findViewById(R.id.status_text);
        TextView modeTextView = deviceLayout.findViewById(R.id.mode_text);
        TextView modeLabel = deviceLayout.findViewById(R.id.mode_label);
        TextView instantaneousTextView = deviceLayout.findViewById(R.id.instantan_text);
        TextView totalTextView = deviceLayout.findViewById(R.id.total_text);
        TextView totalLabel = deviceLayout.findViewById(R.id.total_label);
        ImageView image = deviceLayout.findViewById(R.id.device_image);
        TextView instantanLabel = deviceLayout.findViewById(R.id.instantan_label);

        final EchoDevice device = devices.get(position);
        //Set Image
        image.setImageResource(device.getImage());

        //Format data

                //Set status text
        statusTextView.setText(operationConverter(device.getFirstValue()));


        switch (device.getName()){
            case "ev":
            case "battery":
                {
                modeTextView.setVisibility(View.VISIBLE);
                totalTextView.setVisibility(View.VISIBLE);
                modeLabel.setVisibility(View.VISIBLE);
                totalLabel.setVisibility(View.VISIBLE);
                //Set mode text
                modeTextView.setText(modeConverter(device.getSecondValue()));
                //Set instantaneous text
                String batteryInst = String.valueOf(Long.parseLong(device.getThirdValue(),16)).concat(" W");
                instantaneousTextView.setText(batteryInst);
                //Set total text
                String batteryTotal = String.valueOf(Long.parseLong(device.getForthValue(),16)).concat(" Wh");
                totalTextView.setText(batteryTotal);
                //set onClick
                deviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent configDevice = new Intent(parent.getContext(), ConfigDevice.class);
                        configDevice.putExtra("device",device.getName());
                        configDevice.putExtra("deviceData", device.getData());
                        configDevice.putExtra("ipAddress", device.getIpAddress());
                        configDevice.putExtra("image", device.getImage());
                        parent.getContext().startActivity(configDevice);
                    }
                });
                break;
                }
            case "solar":
                String solarInstantan = device.getSecondValue().concat(" W");
                instantaneousTextView.setText(solarInstantan);
                modeTextView.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                break;
            case "light":
                modeTextView.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                instantanLabel.setVisibility(View.GONE);
                instantaneousTextView.setVisibility(View.GONE);
                break;
        }
        return deviceLayout;
    }

    private String modeConverter(String input){
        String outPut = "";
        switch (input){
            case "40":
                outPut = "Other";
                break;
            case "42":
                outPut = "Charging";
                break;
            case "43":
                outPut = "Discharging";
                break;
            case "44":
                outPut = "Standby";
                break;
            case "47":
                outPut = "Idle";
                break;
        }
        return outPut;
    }

    private String operationConverter(String input){
        String outPut = "";
        switch (input){
            case "30":
                return "ON";
            case "31":
                return "OFF";
        }
        return outPut;
    }
}
