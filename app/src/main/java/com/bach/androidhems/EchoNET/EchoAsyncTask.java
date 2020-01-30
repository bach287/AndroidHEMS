package com.bach.androidhems.EchoNET;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bach.androidhems.FindDevices;
import com.bach.androidhems.R;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.processing.defaults.DefaultController;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EchoAsyncTask extends AsyncTask<Void, Void, ArrayList<DeviceObject> > {

    @SuppressLint("StaticFieldLeak")
    private Activity context;

    enum devices{
        EV,BATTERY,SOLAR,LIGHT
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public EchoAsyncTask(Activity context){
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<DeviceObject> doInBackground(Void... voids) {
        ArrayList<DeviceObject> echoList = new ArrayList<>();

        try {
            Echo.start( new DefaultNodeProfile(), new DeviceObject[] {
                    new DefaultController()
            });
            // ノードに機器オブジェクトのリストを要求します。
            NodeProfile.getG().reqGetSelfNodeInstanceListS().send();
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            // すべてのノード
            EchoNode[] nodes = Echo.getNodes() ;
            // 自分のノード
            EchoNode local = Echo.getSelfNode() ;
            for ( EchoNode en : nodes ) {
                if ( en == local ) {
                    Log.d("Echo","Node id = " + en.getAddress().getHostAddress() + "(local)");
                    Log.d("Echo","Node profile = " + en.getNodeProfile() + "(local)");
                }
                else {
                    DeviceObject[] dos = en.getDevices();
                    for ( DeviceObject d : dos ) {
                        Log.d("Echo","  " + d.getEchoClassCode());
                        echoList.add(d);
                    }
                    Log.d("Echo","Node id = " + en.getAddress().getHostAddress());
                }

                Log.d("Echo"," Node Profile = " + en.getNodeProfile());


            }
        }
        catch( IOException e) {
            e.printStackTrace();
        }
        return echoList;
    }

    @Override
    protected void onPostExecute(ArrayList<DeviceObject> echoNodes) {
        super.onPostExecute(echoNodes);
        List<EchoDevice> deviceList = new ArrayList<>();
        if(echoNodes.size() == 0){
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout linearLayout = context.findViewById(R.id.activity_find_devices);
            View view = inflater.inflate(R.layout.device_not_found, linearLayout);
            return;
        }
        for (DeviceObject device: echoNodes) {
            try {
                device.get().reqGetOperationStatus().send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> deviceData;
            String ipAddress = device.getNode().getAddress().toString();
            EchoDevice echoDevice;
            switch (device.getEchoClassCode()){

                case (short)0x027d:
                    deviceData = getData(ipAddress,"battery");
                    echoDevice = new EchoDevice(R.mipmap.battery_round, deviceData, "battery", ipAddress);
                    Log.d("Echo", echoDevice.getName() + ", " + echoDevice.getFirstValue());
                    deviceList.add(echoDevice);
                    break;
                case (short)0x027e:
                    deviceData = getData(ipAddress,"ev");
                    echoDevice = new EchoDevice(R.mipmap.ev_round, deviceData, "ev", ipAddress);
                    Log.d("Echo", echoDevice.getName() + ", " + echoDevice.getFirstValue());
                    deviceList.add(echoDevice);

                    break;
                case (short)0x0279:
                    deviceData = getData(ipAddress,"solar");
                    echoDevice = new EchoDevice(R.mipmap.solar_round, deviceData, "solar", ipAddress);
                    Log.d("Echo", echoDevice.getName() + ", " + echoDevice.getFirstValue());
                    deviceList.add(echoDevice);
                    break;
                case (short)0x0290:
                    deviceData = getData(ipAddress,"light");
                    echoDevice = new EchoDevice(R.mipmap.light_round, deviceData, "light", ipAddress);
                    Log.d("Echo", echoDevice.getName() + ", " + echoDevice.getFirstValue());
                    deviceList.add(echoDevice);
                    break;
            }
        }
        EchoListAdapter adapter = new EchoListAdapter(context,R.layout.device_layout,deviceList);
        ListView listView = context.findViewById(R.id.deviceListView);
        listView.setAdapter(adapter);

    }

    private ArrayList<String> getData(String ip, String device){
        final String EHD1 = "10";
        final String EHD2 = "81";
        final String TID = "1234";
        final String SEOJ = "0ef001";
        final String EV_DEOJ = "027e01";
        final String BATT_DEOJ = "027d01";
        final String SOLAR_DEOJ = "027901";
        final String LIGHT_DEOJ = "029001";
        final String ESV = "62";
        final String OPC = "04";
        final String OPERATION_EPC = "80";
        final String PDC1 = "00";
        final String DA_EPC = "da";
        final String PDC2 = "00";
        final String D3_EPC = "d3";
        final String PDC3 = "00";
        final String E2_EPC = "e2";
        final String E4_EPC = "e4";
        final String E0_EPC = "e4";
        final String PDC4 = "00";
        String data = "";
        switch (device){
            case "battery":
                data = EHD1 + EHD2 + TID + SEOJ + BATT_DEOJ + ESV + OPC + OPERATION_EPC + PDC1 + DA_EPC + PDC2 + D3_EPC + PDC3 + E2_EPC + PDC4;
                break;
            case "ev":
                data = EHD1 + EHD2 + TID + SEOJ + EV_DEOJ + ESV + OPC + OPERATION_EPC + PDC1 + DA_EPC + PDC2 + D3_EPC + PDC3 + E2_EPC + PDC4;
                break;
            case "solar":
                data = EHD1 + EHD2 + TID + SEOJ + SOLAR_DEOJ + ESV + "02" + OPERATION_EPC + PDC1 + E0_EPC + PDC2;
                break;
            case "light":
                data = EHD1 + EHD2 + TID + SEOJ + SOLAR_DEOJ + ESV + "01" + OPERATION_EPC + PDC1;
                break;
        }
        String ipAddress = ip.replace("/","");
        byte[] buf = hexStringToByteArray(data);
        byte[] receiveBuf = new byte[100];
        byte[] concattedBuf;
        String hexData;
        int port = 3610;
        Log.d("Echo", "Package :" + data);
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
            Log.d("Echo", "Receive: " + hexData);
            Log.d("Echo", "Data Handled: " + dataHandle(hexData,device));
            return dataHandle(hexData,device);

        }catch (IOException ex){
            Log.d("Echo_data", ex.toString());
        }
        return null;
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

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private ArrayList<String> dataHandle(String pureData, String device){
        ArrayList<String> data = new ArrayList<>();
        String epc ;
        String edt ;
        String pdc ;
        //get Operation
        epc = pureData.substring(24,30); //8001xx
        edt = epc.substring(4,6); //get xx
        data.add(edt);
        if(device.equals("light")){
            return data;
        }
        //get E0 of solar
        if(device.equals("solar")){
            pdc = pureData.substring(32,34);
            int e0PDC = Integer.parseInt(pdc);
            if(e0PDC > 0){
                edt = pureData.substring(34, 34 + e0PDC * 2);
                data.add(edt);
            }else{
                data.add("0");
            }
            return data;
        }

        //get Mode
        epc = pureData.substring(30,36);
        edt = epc.substring(4,6);
        data.add(edt);

        //get D3
        pdc = pureData.substring(38,40);
        int d3PDC = Integer.parseInt(pdc);
        int nextStart = 40 + d3PDC * 2;
        if(d3PDC > 0){
            edt = pureData.substring(40, nextStart);
            data.add(edt);
        }else{
            data.add("0");
        }

        //get E2
        pdc = pureData.substring(nextStart + 2,nextStart + 4);
        int e2PDC = Integer.parseInt(pdc);
        nextStart += 4;
        if(e2PDC > 0){
            edt = pureData.substring(nextStart, nextStart + e2PDC * 2);
            data.add(edt);

        }else{
            data.add("0");
        }

        return data;
    }

}
