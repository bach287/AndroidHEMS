package com.bach.androidhems.Receiver;

import com.sonycsl.echo.eoj.device.DeviceObject;

import java.util.ArrayList;

public final class DevicesFoundList {
    public static ArrayList<DeviceObject> deviceObjects = new ArrayList<>();

    public DevicesFoundList() {

    }

    public static void addDevice(DeviceObject object){
        if(object != null)
            deviceObjects.add(object);
    }

    public static ArrayList<DeviceObject> getDevicesList(){
        return deviceObjects;
    }

    public static int getSize(){
        return deviceObjects.size();
    }

    public static void removeAll(){
        deviceObjects.clear();
    }
}
