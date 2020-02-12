package com.bach.androidhems.Receiver;

public interface DemoReceivable {
    void demoFoundEV(String instantanValue);
    void demoEVDisplayMode(String mode);
    void demoFoundBattery(String instantanValue);
    void demoBatteryDisplayMode(String mode);
    void demoFoundSolar(String instantanValue);
    void demoSolarDisplayMode(String mode);
    void demoFoundLight(String status);
}
