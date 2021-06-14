//package com.example.myapplication;
//
//import android.bluetooth.BluetoothSocket;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//public class ThreadConnected extends Thread{
//    private final BluetoothSocket connectedBluetoothSocket;
//    private final InputStream connectedInputStream;
//    private final OutputStream connectedOutputStream;
//
//    public ThreadConnected(BluetoothSocket socket) {
//        connectedBluetoothSocket = socket;
//        InputStream in = null;
//        OutputStream out = null;
//
//        try {
//            in = socket.getInputStream();
//            out = socket.getOutputStream();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        connectedInputStream = in;
//        connectedOutputStream = out;
//    }
//
//    @Override
//    public void run() {
//        byte[] buffer = new byte[1024];
//        int bytes;
//        float srf05Val = 0;
//
//        while (true) {
//            try {
//                bytes = connectedInputStream.read(buffer);
//                //Log.d("buffer", "data: " + (byte)buffer[0] + (byte)buffer[1] + buffer[2] + buffer[3] + buffer[4] + buffer[5] + buffer[6] + buffer[7] + buffer[8] + buffer[9]);
////                if ((buffer[1] ==  (byte)0x01)) { // get SRF05
////                    for (int i = 0; i < 4; i++) {
////                        byteSrf05[i] = buffer[i+2];
////                    }
////                    textSrf05.setText(Float.toString(byteArray2Float(byteSrf05)));
////                    textLine.setText(" ");
////                    textLight.setText(" ");
////                }
////                if ((buffer[1] == (byte)0x02)) { // Get Line Sensor value
////                    for (int i = 0; i < 4; i++) {
////                        byteLine[i] = buffer[i+2];
////                    }
////                    textLine.setText(Float.toString(byteArray2Float(byteLine)));
////                    textSrf05.setText(" ");
////                    textLight.setText(" ");
////                }
////                if ((buffer[1] ==  (byte)0x03)) { // get Light Sensor value
////                    for (int i = 0; i < 4; i++) {
////                        byteLightSensor[i] = buffer[i+2];
////                    }
////                    textLight.setText(Float.toString(byteArray2Float(byteLightSensor)));
////                    textLine.setText(" ");
////                    textSrf05.setText(" ");
////                }
////                if ((buffer[1] ==  (byte)0x06)) { // get Light Sensor value
////                    for (int i = 0; i < 4; i++) {
////                        byteBtn[i] = buffer[i+2];
////                    }
////                    textColor.setText(Float.toString(byteArray2Float(byteBtn)));
//////                        textColor.setText("hello");
////                }
//
//
//                //String strReceived = new String(buffer, 0, bytes);
////                   final String msgReceived = String.valueOf(bytes) + " bytes received:\n" ;
////                    final String length = String.valueOf(bytes);
//                final String msgReceived = String.format("%02X", buffer[0]) + String.format("%02X", buffer[1]) + String.format("%02X", buffer[2])
//                        + String.format("%02X", buffer[3]) + String.format("%02X", buffer[4]) + String.format("%02X", buffer[5])
//                        +  String.format("%02X", buffer[6]) + String.format("%02X", buffer[7]) ;
////                    final String msgReceived = String.format("%02X", byteSrf05[0]) + String.format("%02X", byteSrf05[1]) + String.format("%02X", byteSrf05[2])
////                                                                    + String.format("%02X", byteSrf05[3])  ;
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
//////                            textLine.setText(msgReceived);
//////                         textColor.setText(msgReceived);
////
////                    }});
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                final String msgConnectionLost = "Connection lost:\n"+ e.getMessage();
////                   runOnUiThread(new Runnable(){
////
////                        @Override
////                        public void run() {
////                           textStatus.setText(msgConnectionLost);
////                        }});
//            }
//        }
//    }
//
//    public void write(byte[] buffer) {
//        try {
//            connectedOutputStream.write(buffer);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public void cancel() {
//        try {
//            connectedBluetoothSocket.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//}
