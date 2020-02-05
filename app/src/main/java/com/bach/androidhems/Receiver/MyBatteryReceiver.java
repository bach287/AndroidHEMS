package com.bach.androidhems.Receiver;

import android.content.Context;
import android.util.Log;

import com.bach.androidhems.R;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;

public class MyBatteryReceiver extends Battery.Receiver {
    public static Receivable onReceive;
    public static String operationStatus = "";
    public static String operationMode = "";
    public static String instantaneousValue = "";
    public static String totalRemaining = "";
    private Context context;

    public MyBatteryReceiver(Context context){
        this.context = context;
    }
    @Override
    protected void onGetOperationStatus(EchoObject eoj, short tid, byte esv,
                                        EchoProperty property, boolean success) {
        super.onGetOperationStatus(eoj, tid, esv, property, success);
        if (success){
            operationStatus = DataHandle.statusConverter(property.edt);
        }else{
            Log.d("Echo:", "onGetOperationStatus: Get Battery status failed!");
        }
    }
    @Override
    protected void onGetMeasuredInstantaneousChargeDischargeElectricEnergy(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetMeasuredInstantaneousChargeDischargeElectricEnergy(eoj, tid, esv, property, success);
        if(success){
            instantaneousValue = Long.toString(Long.parseLong(DataHandle.bytesToHex(property.edt),16));
        }else{
            Log.d("Echo:", " Fail to get battery instantaneousValue capacity");
        }
    }

    @Override
    protected void onGetRemainingStoredElectricity1(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetRemainingStoredElectricity1(eoj, tid, esv, property, success);
        if(success){
            totalRemaining = Long.toString(Long.parseLong(DataHandle.bytesToHex(property.edt),16));
        }else{
            Log.d("Echo:", " Fail to get battery total capacity");
        }
    }

    @Override
    protected boolean onGetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetProperty(eoj, tid, esv, property, success);
        if(!operationMode.equals("") && !instantaneousValue.equals("") && onReceive != null){
            onReceive.reGenerateMode(operationMode);
            onReceive.reGenerateInstantan(instantaneousValue);
        }
        return true;
    }

    @Override
    protected void onGetOperationModeSetting(EchoObject eoj, short tid, byte esv,
                                             EchoProperty property, boolean success) {
        super.onGetOperationModeSetting(eoj, tid, esv, property, success);
        if (success) {
            operationMode = DataHandle.modeConverter(property.edt);
        }else{
            Log.d("Echo:", " Fail to get Battery mode");
        }
    }

    @Override
    protected boolean onSetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {

        Log.d("Echo", "onSetProperty: " + "And:"+  property.epc);
        if(success){
            if(property.epc == DataHandle.hexStringToByteArray("ff")[0] ){
                onReceive.displayMessage(context.getResources().getString(R.string.msg_set_schedule_success));
            }
        }else{
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_schedule_failed));
        }

        return super.onSetProperty(eoj, tid, esv, property, success);
    }


    @Override
    protected void onSetOperationStatus(EchoObject eoj, short tid, byte esv,
                                        EchoProperty property, boolean success) {
        super.onGetOperationStatus(eoj, tid, esv, property, success);
        if (success) {
            onReceive.reGenerateStatus(operationStatus);
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_status_success));
        }else {
            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_status_failed));
        }
    }

    //------------------Getter-------------------
    public String getOperationStatus() {
        return operationStatus;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public String getInstantaneousValue() {
        return instantaneousValue;
    }

    public String getTotalRemaining() {
        return totalRemaining;
    }

}
