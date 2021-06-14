//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Set;
//import java.util.UUID;
//
//public class bluetoothHandler extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bluetooth_handler);
//    }
//    byte[] buffer = new byte[1024];
//    ThreadConnectBTdevice myThreadConnectBTdevice;
//    BluetoothAdapter bluetoothAdapter;
//    private static final int REQUEST_ENABLE_BT = 1;
//
//    private void setup( ) {
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                if (device.getName().equals("Robox")) {
//                    Toast.makeText(this, "Start thread connect to bluetooth device", Toast.LENGTH_LONG).show();
//                    if (myThreadConnectBTdevice == null) {
//                        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
//                    }
//                    myThreadConnectBTdevice.start();
//                }
//            }
//        }
//    }
//
//    public boolean connect_to_bluetooth() {
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        //Toast.makeText(this,"Connect to bluetooth",Toast.LENGTH_LONG).show();
//        if (bluetoothAdapter == null) {
//            //Toast.makeText(this,"Bluetooth is not supported on this hardware platform",Toast.LENGTH_LONG).show();
//            finish();
//            return false;
//        }
//        if (!bluetoothAdapter.isEnabled()) {
//            Toast.makeText(bluetoothHandler.this,"Bluetooth is diable", Toast.LENGTH_LONG).show();
////            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
////            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }
////        setup();
//        return true;
//        // return myThreadConnectBTdevice.getStatusConnect();
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == Activity.RESULT_OK) {
//                //setup();
//            }
//            else {
//                Toast.makeText(this, "BlueTooth NOT enabled", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
////        if ((buffer[1] ==  (byte)0x01)) { // get SRF05
////            for (int i = 0; i < 4; i++) {
////                byteSrf05[i] = buffer[i+2];
////            }
////            textSrf05.setText(Float.toString(byteArray2Float(byteSrf05)));
////        textLine.setText(" ");
////        Control.texLine.setText("");
////}
//
//    //Called in ThreadConnectBTdevice once connect successed
//    //to start ThreadConnected
//
//    private class ThreadConnectBTdevice extends Thread {
//
//        private BluetoothSocket bluetoothSocket = null;
//        private final BluetoothDevice bluetoothDevice;
//        private ThreadConnected myThreadConnected;
//        private UUID myUUID;
//        boolean statusConnect = false;
//        private final String UUID_STRING_WELL_KNOWN_SPP =
//                "00001101-0000-1000-8000-00805F9B34FB";
//
//        private void startThreadConnected(BluetoothSocket socket)
//        {
//            myThreadConnected = new ThreadConnected(socket);
//            myThreadConnected.start();
//        }
//        private ThreadConnectBTdevice(BluetoothDevice device) {
//            bluetoothDevice = device;
//            myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
//            try {
//                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
////                btnConnect.setBackgroundResource(R.drawable.ic_ble_on);
//            } catch (IOException e) {
////                btnConnect.setBackgroundResource(R.drawable.ic_ble_off);
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
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
//            Toast.makeText(getApplicationContext(), "close bluetoothSocket", Toast.LENGTH_LONG).show();
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
//        //        public static ThreadConnectBTdevice getInstance() {
////            return instance;
////        }
//    }
//
//    /*
//    ThreadConnected:
//    Background Thread to handle Bluetooth data communication
//    after connected
//     */
//    private class ThreadConnected extends Thread {
//        private final BluetoothSocket connectedBluetoothSocket;
//        private final InputStream connectedInputStream;
//        private final OutputStream connectedOutputStream;
//
//        public ThreadConnected(BluetoothSocket socket) {
//            connectedBluetoothSocket = socket;
//            InputStream in = null;
//            OutputStream out = null;
//
//            try {
//                in = socket.getInputStream();
//                out = socket.getOutputStream();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            connectedInputStream = in;
//            connectedOutputStream = out;
//        }
//
//        @Override
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//            float srf05Val = 0;
//
//            while (true) {
//                try {
//                    bytes = connectedInputStream.read(buffer);
//                    //Log.d("buffer", "data: " + (byte)buffer[0] + (byte)buffer[1] + buffer[2] + buffer[3] + buffer[4] + buffer[5] + buffer[6] + buffer[7] + buffer[8] + buffer[9]);
//                    if ((buffer[1] ==  (byte)0x01)) { // get SRF05
////                        for (int i = 0; i < 4; i++) {
////                            byteSrf05[i] = buffer[i+2];
////                        }
////                        textSrf05.setText(Float.toString(byteArray2Float(byteSrf05)));
////                        textLine.setText(" ");
////                        textLight.setText(" ");
//                    }
//                    if ((buffer[1] == (byte)0x02)) { // Get Line Sensor value
////                        for (int i = 0; i < 4; i++) {
////                            byteLine[i] = buffer[i+2];
////                        }
////                        textLine.setText(Float.toString(byteArray2Float(byteLine)));
////                        textSrf05.setText(" ");
////                        textLight.setText(" ");
//                    }
//                    if ((buffer[1] ==  (byte)0x03)) { // get Light Sensor value
////                        for (int i = 0; i < 4; i++) {
////                            byteLightSensor[i] = buffer[i+2];
////                        }
////                        textLight.setText(Float.toString(byteArray2Float(byteLightSensor)));
////                        textLine.setText(" ");
////                        textSrf05.setText(" ");
//                    }
//                    if ((buffer[1] ==  (byte)0x06)) { // get Light Sensor value
////                        for (int i = 0; i < 4; i++) {
////                            byteBtn[i] = buffer[i+2];
////                        }
////                        textColor.setText(Float.toString(byteArray2Float(byteBtn)));
//////                        textColor.setText("hello");
//                    }
//
//
//                    //String strReceived = new String(buffer, 0, bytes);
////                   final String msgReceived = String.valueOf(bytes) + " bytes received:\n" ;
////                    final String length = String.valueOf(bytes);
//                    final String msgReceived = String.format("%02X", buffer[0]) + String.format("%02X", buffer[1]) + String.format("%02X", buffer[2])
//                            + String.format("%02X", buffer[3]) + String.format("%02X", buffer[4]) + String.format("%02X", buffer[5])
//                            +  String.format("%02X", buffer[6]) + String.format("%02X", buffer[7]) ;
////                    final String msgReceived = String.format("%02X", byteSrf05[0]) + String.format("%02X", byteSrf05[1]) + String.format("%02X", byteSrf05[2])
////                                                                    + String.format("%02X", byteSrf05[3])  ;
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
//////                            textLine.setText(msgReceived);
//////                         textColor.setText(msgReceived);
////
////                        }});
//
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    final String msgConnectionLost = "Connection lost:\n"+ e.getMessage();
////                   runOnUiThread(new Runnable(){
////
////                        @Override
////                        public void run() {
////                           textStatus.setText(msgConnectionLost);
////                        }});
//                }
//            }
//        }
//
//        public void write(byte[] buffer) {
//            try {
//                connectedOutputStream.write(buffer);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        public void cancel() {
//            try {
//                connectedBluetoothSocket.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//}