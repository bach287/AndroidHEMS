package com.bach.androidhems.EchoNET;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.bach.androidhems.EchoNET.EchoAsyncTask.bytesToHex;
import static com.bach.androidhems.EchoNET.EchoAsyncTask.hexStringToByteArray;

public class EchoConfAsyncTask extends AsyncTask<Void, Void, String> {
    private HashMap map;
    public EchoConfAsyncTask(HashMap map) {
        this.map = map;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(map == null){
            return null;
        }
        Log.d("Echo", "doInBackground - map: "+ map);
        String data = dataHandle(map);
        Log.d("Echo", "doInBackground - data: "+ data);
        String ipAddress = map.get("ipAddress").toString();
        byte[] buf = hexStringToByteArray(data);
        byte[] receiveBuf = new byte[100];
        byte[] concattedBuf;
        String hexData;
        int port = 3610;
        try {
            DatagramSocket socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind( new InetSocketAddress(port));
            InetAddress address = InetAddress.getByName(ipAddress);
            Log.d("Echo", "IP Address :" + ipAddress);
            //packet to send
            DatagramPacket packet = new DatagramPacket(buf, buf.length,address,port);
            //packet to receive data
            DatagramPacket receivePacket = new DatagramPacket(receiveBuf, 100);
            socket.setSoTimeout(1000);
            socket.send(packet);
            socket.receive(receivePacket);
            socket.close();
            int dataLength = receivePacket.getLength();
            concattedBuf = Arrays.copyOfRange(receiveBuf, 0, dataLength);
            hexData = bytesToHex(concattedBuf);
            Log.d("Echo", "doInBackground: hexData"+ hexData);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String data = EchoNETLiteData.getEVSetStatusString().concat(maps.)
        return null;
    }

    private String dataHandle(HashMap map){
        String data;
        //Set Status
        if(map.size() == 3){
            switch (map.get("device").toString()){
                case "ev":
                    data = EchoNETLiteData.getEVSetStatusString().concat(map.get("status").toString());
                    return data;
                case "battery":
                    data = EchoNETLiteData.getBatterySetStatusString().concat(map.get("status").toString());
                    return data;
            }
        }
        return null;
    }
}
