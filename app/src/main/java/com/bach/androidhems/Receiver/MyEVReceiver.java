package com.bach.androidhems.Receiver;

import android.content.Context;
import android.util.Log;
import com.bach.androidhems.R;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;

public final class MyEVReceiver extends ElectricVehicle.Receiver {
    public static Receivable onReceive;
    public static String operationStatus = "";
    public static String operationMode = "";
    public static String instantaneousValue = "";
    public static String totalRemaining = "";
    private Context context;

    public MyEVReceiver(Context context){
        this.context = context;
    }

    @Override
    protected void onGetOperationStatus(EchoObject eoj, short tid, byte esv,
                                        EchoProperty property, boolean success) {
        super.onGetOperationStatus(eoj, tid, esv, property, success);
        if(success && property.edt != null){
            operationStatus = DataHandle.statusConverter(property.edt);

        }else{
            Log.d("Echo:", " Fail to get EV status");
        }

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
    protected void onGetOperationModeSetting(EchoObject eoj, short tid, byte esv,
                                             EchoProperty property, boolean success) {
        super.onGetOperationModeSetting(eoj, tid, esv, property, success);
        if (success) {
            operationMode = DataHandle.modeConverter(property.edt);
        }else{
            Log.d("Echo:", " Fail to get EV mode");
        }
    }

//    @Override
//    protected void onSetOperationModeSetting(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
//        super.onSetOperationModeSetting(eoj, tid, esv, property, success);
//        if (success) {
//            onReceive.reGenerateMode(operationMode);
//            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_mode_success));
//        }else {
//            onReceive.displayMessage(context.getResources().getString(R.string.msg_set_mode_failed));
//        }
//    }

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

    @Override
    protected void onGetMeasuredInstantaneousChargeDischargeElectricEnergy(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetMeasuredInstantaneousChargeDischargeElectricEnergy(eoj, tid, esv, property, success);
        if(success){
            instantaneousValue = Long.toString(Long.parseLong(DataHandle.bytesToHex(property.edt),16));
        }else{
            Log.d("Echo:", " Fail to get EV Instantaneous value");
        }
    }

    @Override
    protected void onGetRemainingBatteryCapacity1(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
        super.onGetRemainingBatteryCapacity1(eoj, tid, esv, property, success);
        if(success){
            totalRemaining = Long.toString(Long.parseLong(DataHandle.bytesToHex(property.edt),16));
        }else{
            Log.d("Echo:", " Fail to get EV total capacity");
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
