package com.bach.androidhems.EchoNET;

import android.app.Application;

public class EchoNETLiteData extends Application {
    private static final String EHD1 = "10";
    private static final String EHD2 = "81";
    private static final String TID = "1234";
    private static final String SEOJ = "0ef001";
    private static final String EV_DEOJ = "027e01";
    private static final String BATT_DEOJ = "027d01";
    private static final String SOLAR_DEOJ = "027901";
    private static final String LIGHT_DEOJ = "029001";
    private static final String GET_ESV = "62";
    private static final String SET_ESV = "61";
    private static final String OPC = "01";
    private static final String OPERATION_EPC = "80";
    private static final String SET_PDC = "01";
    private static final String GET_PDC = "00";
    private static final String DA_EPC = "da";
    private static final String PDC2 = "00";
    private static final String D3_EPC = "d3";
    private static final String PDC3 = "00";
    private static final String E2_EPC = "e2";
    private static final String E4_EPC = "e4";
    private static final String E0_EPC = "e4";
    private static final String PDC4 = "00";

    public static String getEVSetStatusString() {
        return EHD1.concat(EHD2+TID+SEOJ+LIGHT_DEOJ+SET_ESV+OPC+OPERATION_EPC+SET_PDC);
    }

    public static String getBatterySetStatusString() {
        return EHD1.concat(EHD2+TID+SEOJ+BATT_DEOJ+SET_ESV+OPC+OPERATION_EPC+SET_PDC);
    }

    public static String getEvDeoj() {
        return EV_DEOJ;
    }

    public static String getBattDeoj() {
        return BATT_DEOJ;
    }

    public static String getSolarDeoj() {
        return SOLAR_DEOJ;
    }

    public static String getLightDeoj() {
        return LIGHT_DEOJ;
    }
    public static String getSetEsv() {
        return SET_ESV;
    }
    public static String getGetEsv() {
        return GET_ESV;
    }

    public static String getOPC() {
        return OPC;
    }

    public static String getOperationEpc() {
        return OPERATION_EPC;
    }

    public static String getGetPdc() {
        return GET_PDC;
    }

    public static String getDaEpc() {
        return DA_EPC;
    }

    public static String getPDC2() {
        return PDC2;
    }

    public static String getD3Epc() {
        return D3_EPC;
    }

    public static String getPDC3() {
        return PDC3;
    }

    public static String getE2Epc() {
        return E2_EPC;
    }

    public static String getE4Epc() {
        return E4_EPC;
    }

    public static String getE0Epc() {
        return E0_EPC;
    }

    public static String getPDC4() {
        return PDC4;
    }
}
