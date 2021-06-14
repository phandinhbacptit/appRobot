//package com.example.myapplication;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.widget.Toast;
//
//import com.example.myapplication.R;
//import com.example.myapplication.ThreadConnected;
//
//import java.io.IOException;
//import java.util.UUID;
//
//public class ThreadConnectBTdevice extends Thread {
//        private BluetoothSocket bluetoothSocket = null;
//        private final BluetoothDevice bluetoothDevice;
//        private UUID myUUID;
//        boolean statusConnect = false;
//        private final String UUID_STRING_WELL_KNOWN_SPP =
//                "00001101-0000-1000-8000-00805F9B34FB";
//         private static  ThreadConnected myThreadConnected;
//
//        public ThreadConnectBTdevice(BluetoothDevice device) {
//            bluetoothDevice = device;
//            myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
//            try {
//                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
//                statusConnect = true;
////                textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                statusConnect = false;
//                e.printStackTrace();
//            }
//        }
////        private static  ThreadConnectBTdevice instance = new ThreadConnectBTdevice();
//
//        private   void startThreadConnected(BluetoothSocket socket)
//        {
//            if (myThreadConnected  == null) {
//                myThreadConnected = new ThreadConnected(socket);
//                myThreadConnected.start();
//            }
//        }
//
//        @Override
//        public void run() {
//            boolean success = false;
//            try {
//                bluetoothSocket.connect();
//                success = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//                final String eMessage = e.getMessage();
//                try {
//                    bluetoothSocket.close();
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            }
//            if (success) {
//                //connect successful
//                final String msgconnected = "connect successful: " + "BluetoothDevice: " + bluetoothDevice;
//                startThreadConnected(bluetoothSocket);
//            }
//            else {//fail
//            }
//        }
//        public void cancel()
//        {
//           // Toast.makeText(getApplicationContext(), "close bluetoothSocket", Toast.LENGTH_LONG).show();
//            try {
//                bluetoothSocket.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        public boolean getStatusConnect() {
//            return statusConnect;
//        }
////        public static ThreadConnectBTdevice getInstance() {
////            return instance;
////        }
//        public  static ThreadConnected getStateConnected() {
//           return myThreadConnected;
//        }
//    }