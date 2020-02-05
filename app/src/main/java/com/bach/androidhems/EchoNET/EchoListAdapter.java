package com.bach.androidhems.EchoNET;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bach.androidhems.ConfigDevice;
import com.bach.androidhems.R;
import com.bach.androidhems.Receiver.MyBatteryReceiver;
import com.bach.androidhems.Receiver.MyEVReceiver;
import com.bach.androidhems.Receiver.MyLightReceiver;
import com.bach.androidhems.Receiver.MySolarReceiver;
import com.sonycsl.echo.eoj.device.DeviceObject;

import java.util.List;


public class EchoListAdapter extends BaseAdapter {
    private List<DeviceObject> devices;
    private Context context;

    private int resource;

    public EchoListAdapter(Context context, int resource, List<DeviceObject> devices) {
        this.context = context;
        this.resource = resource;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        if (devices.isEmpty()) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
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
        //------------------Set Visible -----------
        instantaneousTextView.setVisibility(View.VISIBLE);
        instantanLabel.setVisibility(View.VISIBLE);
        modeTextView.setVisibility(View.VISIBLE);
        totalTextView.setVisibility(View.VISIBLE);
        modeLabel.setVisibility(View.VISIBLE);
        totalLabel.setVisibility(View.VISIBLE);

        final DeviceObject device = devices.get(position);
        String eoj = Integer.toString(device.getEchoClassCode(),16);
        Log.d("Echo:", "getView: " + eoj);
        deviceLayout.setOnClickListener(null);
        switch (eoj) {
            case "27e":
                final MyEVReceiver ev = (MyEVReceiver)device.getReceiver();
                //---------------Set layout component---------------
                if(ev == null){
                    return null;
                }
                image.setImageResource(R.mipmap.ev_round);
                statusTextView.setText(ev.getOperationStatus());
                modeTextView.setText(ev.getOperationMode());
                instantaneousTextView.setText(ev.getInstantaneousValue());
                totalTextView.setText(ev.getTotalRemaining());
                deviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent configDevice = new Intent(parent.getContext(), ConfigDevice.class);
                    configDevice.putExtra("name", "ev");
                    configDevice.putExtra("position", position);
                    parent.getContext().startActivity(configDevice);
                    }
                });
                break;
            case "27d":
                final MyBatteryReceiver battery = (MyBatteryReceiver)device.getReceiver();
                //---------------Set layout component---------------
                if(battery == null){
                    return null;
                }
                image.setImageResource(R.mipmap.battery_round);
                statusTextView.setText(battery.getOperationStatus());
                modeTextView.setText(battery.getOperationMode());
                instantaneousTextView.setText(battery.getInstantaneousValue());
                totalTextView.setText(battery.getTotalRemaining());
                deviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent configDevice = new Intent(parent.getContext(), ConfigDevice.class);
                        configDevice.putExtra("name", "battery");
                        configDevice.putExtra("position", position);
                        parent.getContext().startActivity(configDevice);
                    }
                });
                break;
            case "279":
                MySolarReceiver solar = (MySolarReceiver) device.getReceiver();
                //---------------Set layout component---------------
                if(solar == null){
                    return null;
                }
                image.setImageResource(R.mipmap.solar_round);
                statusTextView.setText(solar.getOperationStatus());
                instantaneousTextView.setText(solar.getInstantaneousValue());

                modeTextView.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                deviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent configDevice = new Intent(parent.getContext(), ConfigDevice.class);
                        configDevice.putExtra("name", "solar");
                        configDevice.putExtra("position", position);
                        parent.getContext().startActivity(configDevice);
                    }
                });
                break;
            case "290":
                MyLightReceiver light = (MyLightReceiver) device.getReceiver();
                //---------------Set layout component---------------
                if(light == null){
                    return null;
                }
                image.setImageResource(R.mipmap.light_round);
                statusTextView.setText(light.getOperationStatus());

                modeTextView.setVisibility(View.GONE);
                totalTextView.setVisibility(View.GONE);
                instantaneousTextView.setVisibility(View.GONE);
                instantanLabel.setVisibility(View.GONE);
                modeLabel.setVisibility(View.GONE);
                totalLabel.setVisibility(View.GONE);
                deviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent configDevice = new Intent(parent.getContext(), ConfigDevice.class);
                        configDevice.putExtra("name", "light");
                        configDevice.putExtra("position", position);
                        parent.getContext().startActivity(configDevice);
                    }
                });
                break;
        }
        return deviceLayout;
    }
}
