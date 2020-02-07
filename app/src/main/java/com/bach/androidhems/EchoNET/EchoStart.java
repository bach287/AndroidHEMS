package com.bach.androidhems.EchoNET;

import android.content.Context;
import android.util.Log;

import com.bach.androidhems.Receiver.DevicesFoundList;
import com.bach.androidhems.Receiver.MyBatteryReceiver;
import com.bach.androidhems.Receiver.MyEVReceiver;
import com.bach.androidhems.Receiver.MyLightReceiver;
import com.bach.androidhems.Receiver.MySolarReceiver;
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

public class EchoStart {

    public EchoStart(final Context context){
        try {
            Echo.start(new DefaultNodeProfile(), new DeviceObject[]{
                    new DefaultController()
            });

            Echo.addEventListener(new Echo.EventListener() {
                @Override
                public void onNewElectricVehicle(ElectricVehicle device) {
                    super.onNewElectricVehicle(device);
                    Log.d("Echo:", "Electric vehicle found.");
                    device.setReceiver(new MyEVReceiver(context));
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
                    device.setReceiver(new MyBatteryReceiver(context));
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
                    device.setReceiver(new MySolarReceiver(context));
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
    }

    public void addDeviceList() {
//        ArrayList<DeviceObject> devices = new ArrayList<>();
        DevicesFoundList.removeAll();
        for (EchoNode node : Echo.getNodes()) {
            Log.d("Echo", "Node profile:" + node.getNodeProfile());
            if (node != Echo.getSelfNode()) {
                DeviceObject[] objects = node.getDevices();
                for (DeviceObject object : objects) {
                    Log.d("Echo", "Class code:" + object.getEchoClassCode());
                    DevicesFoundList.addDevice(object);
                }
            }
        }
    }



}
