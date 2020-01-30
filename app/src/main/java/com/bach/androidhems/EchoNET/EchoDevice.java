package com.bach.androidhems.EchoNET;

import java.util.ArrayList;

public class EchoDevice {
    private int image;
    private ArrayList<String> data;
    private String name;
    private String ipAddress;
    public EchoDevice(int image, ArrayList<String> data, String name, String ipAddress){
        this.image = image;
        this.data = data;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress(){
        return ipAddress.replace("/", "");
    }

    public String getName(){
        return name;
    }
    public ArrayList<String> getData(){
        return data;
    }
    public int getImage(){
        return image;
    }
    public String getFirstValue(){

        return data.get(0);
    }
    public String getSecondValue(){
        return data.get(1);
    }
    public String getThirdValue(){
        String output;
        try{
            output = data.get(2);
        }catch (IndexOutOfBoundsException ex){
            return null;
        }
        return output;
    }
    public String getForthValue(){
        String output;
        try{
            output = data.get(3);
        }catch (IndexOutOfBoundsException ex){
            return null;
        }
        return output;
    }




}
