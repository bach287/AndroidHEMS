package com.bach.androidhems.Receiver;

public interface Receivable {
//    void setOnReceive(onReceive onReceive);
    void displayMessage(String mess);
    void reGenerateStatus(String status);
    void reGenerateMode(String mode);
    void reGenerateInstantan(String instantan);
}
