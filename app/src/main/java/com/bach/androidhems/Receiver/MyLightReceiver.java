package com.bach.androidhems.Receiver;

import android.util.Log;

import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.housingfacilities.GeneralLighting;

import java.io.Serializable;

public class MyLightReceiver extends GeneralLighting.Receiver implements Serializable {
    public static Receivable onReceive;
    public static String operationStatus = "";

    public MyLightReceiver(){

    }

    @Override
    protected void onGetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetOperationStatus(eoj, tid, esv, property, success);
        if(success){
            operationStatus = DataHandle.statusConverter(property.edt);
        }else{
            Log.d("Echo:", " Fail to get Light status");
        }
    }

    @Override
    protected void onSetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onSetOperationStatus(eoj, tid, esv, property, success);
        if(success){
            onReceive.reGenerateStatus(operationStatus);
            onReceive.displayMessage("Set Status Successful!");
        }else{
            onReceive.displayMessage("Set Mode Failed!");
        }
    }

    public String getOperationStatus() {
        return operationStatus;
    }

}
