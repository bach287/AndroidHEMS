package com.bach.androidhems.Receiver;

import com.sonycsl.echo.eoj.device.DeviceObject;

import java.util.ArrayList;

public final class DevicesFoundList {
    public static ArrayList<DeviceObject> deviceObjects = new ArrayList<>();

    public DevicesFoundList() {

    }

    public void addDevice(DeviceObject object){
        if(object != null)
            deviceObjects.add(object);
    }

    public ArrayList<DeviceObject> getDevicesList(){
        return deviceObjects;
    }

    public int getSize(){
        return deviceObjects.size();
    }

    public void removeAll(){
        deviceObjects.clear();
    }
}
