package com.bach.androidhems.Receiver;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public final class DataHandle {
    public static String[] modesArray = {"Standby","Charging", "Discharging", "Idle"};

    //------------------Handle Functions--------------

    protected static String statusConverter(byte[] input){
        if(bytesToHex(input).equals("30")){
            return "ON";
        }else if(bytesToHex(input).equals("31")){
            return "OFF";
        }
        return "";
    }

    protected static String modeConverter(byte[] input){
        switch (bytesToHex(input)){
            case "42":
                return "Charging";
            case "43":
                return "Discharging";
            case "44":
                return "Standby";
            case "47":
                return "Idle";
            default:
                return "Other";
        }
    }

    public static String modeReverter(String input){
        switch (input){
            case "Charging":
                return "42";
            case "Discharging":
                return "43";
            case "Standby":
                return "44";
            case "Idle":
                return "47";
            default:
                return null;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String timeHandle(String input){
        return String.format("%02x", Integer.parseInt(input.split(":")[0]) & 0xFF).concat(String.format("%02x", Integer.parseInt(input.split(":")[1]) & 0xFF));
    }


}
