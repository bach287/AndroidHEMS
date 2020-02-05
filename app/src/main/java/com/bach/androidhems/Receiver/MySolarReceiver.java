package com.bach.androidhems.Receiver;

import android.content.Context;
import android.util.Log;

import com.bach.androidhems.R;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration;

import java.io.Serializable;

public class MySolarReceiver extends HouseholdSolarPowerGeneration.Receiver implements Serializable {
    public static Receivable onReceive;
    public static String operationStatus = "";
    public static String instantaneousValue = "";
    private Context context;


    public MySolarReceiver(Context context){
        this.context = context;
    }
    @Override
    protected void onGetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetOperationStatus(eoj, tid, esv, property, success);
        if(success){
            operationStatus = DataHandle.statusConverter(property.edt);
        }else{
            Log.d("Echo:", " Fail to get Solar status");
        }
    }

    @Override
    protected void onGetMeasuredInstantaneousAmountOfElectricityGenerated(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetMeasuredInstantaneousAmountOfElectricityGenerated(eoj, tid, esv, property, success);
        if(success){
            instantaneousValue = Long.toString(Long.parseLong(DataHandle.bytesToHex(property.edt),16));
        }else{
            Log.d("Echo:", " Fail to get Solar Instantaneous value");
        }
    }

    @Override
    protected boolean onGetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetProperty(eoj, tid, esv, property, success);
        if(!instantaneousValue.equals("") && onReceive != null){
            onReceive.reGenerateInstantan(instantaneousValue);
        }
        return true;
    }

    @Override
    protected void onSetOperationStatus(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onSetOperationStatus(eoj, tid, esv, property, success);
        if (success) {
            onReceive.reGenerateStatus(operationStatus);
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_status_success));
        }else {
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_status_failed));
        }
    }

    @Override
    protected boolean onSetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onSetProperty(eoj, tid, esv, property, success);
        Log.d("Echo", "onSetProperty: " + "And:"+  property.epc);
        if(success){
            if(property.epc == DataHandle.hexStringToByteArray("ff")[0] ){
                onReceive.displayMessage(context.getResources().getString(R.string.msg_set_schedule_success));
            }
        }else{
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_schedule_failed));
        }

        return true;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public String getInstantaneousValue() {
        return instantaneousValue;
    }

}
