package com.example.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;
import com.example.myapplication.Control;
import android.bluetooth.BluetoothClass.Device;

import org.jetbrains.annotations.NotNull;

public class classicBluetooth  extends Service {
    private BluetoothAdapter mBluetoothAdapter;
    public static final String BT_DEVICE = "Robox";
    public static final String B_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    public static final String mBroadcastGetData = "VrobotGetData";
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    boolean statusConnect = false;

    private ConnectBtThread mConnectThread;
    private static ConnectedBtThread mConnectedThread;

    private static Handler mHandler = null;
    public static int mState = STATE_NONE;
    public static String deviceName;
    public static BluetoothDevice sDevice = null;
    public Vector<Byte> packData = new Vector<>(2048);
    public static Device device = null;
    Timer timer;
    private PendingIntent data;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void toast(String mess){
        Toast.makeText(this,mess,Toast.LENGTH_SHORT).show();
    }
    private final IBinder mBinder = new LocalBinder();

    void delay_ms(int ms) {
        try {
            Thread.sleep(ms);
        } catch(
                Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(classicBluetooth.this, "Turn on bluetooth in your device", Toast.LENGTH_SHORT).show();
            mBluetoothAdapter.getDefaultAdapter().enable();
        }
        boolean state;
        connectToDevice(BT_DEVICE);
        return START_STICKY;
    }

//    // Create a BroadcastReceiver for ACTION_FOUND
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                textStatus.append("\n unpair" + device.getName() + device.getAddress() );
//                Log.i("ctr", "hello" );
//                if (device.getName() == "ESP32_LED_Control") {
//                        textStatus.append(" get Device ");
//                        try {
//                            Method method = device.getClass().getMethod("createBond,", (Class[]) null);
//                            method.invoke(device, (Object[]) null);
//                        } catch  (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//    };

    private synchronized boolean connectToDevice(String nameRobot)
    {
//        //check to see if there is BT on the Android device at all
//        if (mBluetoothAdapter == null){
//            int duration = Toast.LENGTH_SHORT;
//            Toast.makeText(this, "No Bluetooth on this handset", duration).show();
//        }
//        if (mBluetoothAdapter.isDiscovering()){
//            mBluetoothAdapter.cancelDiscovery();
//        }
//        //re-start discovery
//        mBluetoothAdapter.startDiscovery();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //Toast.makeText(classicBluetooth.this,"pairedDv" + pairedDevices.size() ,Toast.LENGTH_SHORT).show();
        Log.i("hhhh","pairedDv" + pairedDevices.size());
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(nameRobot)) {

                    if (mState == STATE_CONNECTING) {
                        if (mConnectThread != null) {
                            mConnectThread.cancel();
                            mConnectThread = null;
                        }
                    }
                    if (mConnectedThread != null) {
                        mConnectedThread.cancel();
                        mConnectedThread = null;
                    }
                    mConnectThread = new ConnectBtThread(device);
                   // toast("connecting");
                    mConnectThread.start();
                    setState(STATE_CONNECTING);
                    return false;
                }
            }
        }
        return true;
    }

    private void setState(int state)
    {
        mState = state;
        if (mHandler != null){
            // mHandler.obtainMessage();
        }
    }
    public synchronized void stop()
    {
        setState(STATE_NONE);
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mBluetoothAdapter != null){
            mBluetoothAdapter.cancelDiscovery();
        }
        stopSelf();
    }

    public void sendData(String message)
    {
        if (mConnectedThread != null) {
            mConnectedThread.write(message.getBytes());
            toast("sent data");
        } else {
            Toast.makeText(classicBluetooth.this,"Failed to send data",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean stopService(Intent name)
    {
        setState(STATE_NONE);
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mBluetoothAdapter.cancelDiscovery();
        return super.stopService(name);
    }

    public boolean get_state_blue_connect()
    {
        return statusConnect;
    }
    public void retry_connect()
    {
        connectToDevice(BT_DEVICE);
    }

    public  static  ConnectedBtThread getInstance ()
    {
        return mConnectedThread;
    }
    public class LocalBinder extends Binder {
        classicBluetooth getService() {
            // Return this instance of LocalService so clients can call public methods
            return classicBluetooth.this;
        }
    }
    private class ConnectBtThread extends Thread{
        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;

        public ConnectBtThread(BluetoothDevice device){
            mDevice = device;
            BluetoothSocket socket = null;
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(B_UUID));
            } catch (IOException e) {
                e.printStackTrace();
                statusConnect = false;
            }
            mSocket = socket;
        }

        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
                Log.d("service","connect thread run method (connected)");
                SharedPreferences pre = getSharedPreferences("BT_NAME",0);
                pre.edit().putString("bluetooth_connected",mDevice.getName()).apply();

            } catch (IOException e) {

                try {
                    mSocket.close();
                    Log.d("service","connect thread run method ( close function)");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            //connected(mSocket);
            mConnectedThread = new ConnectedBtThread(mSocket);
            mConnectedThread.start();
        }

        public void cancel() {
            try {
                mSocket.close();
                Log.i("hhhh","connect thread cancel method");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ConnectedBtThread extends Thread{
        private final BluetoothSocket cSocket;
        private final InputStream inS;
        private final OutputStream outS;
        private int mByte;
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream( );
        byte[] tmpData = new byte[100];
        byte[] sendData =  new byte[100];

        public ConnectedBtThread(@NotNull BluetoothSocket socket){
            cSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            inS = tmpIn;
            outS = tmpOut;
            statusConnect = true;
            Log.i("hhhh","statusConnect = true");
        }

        @Override
        public void run() {
            Intent result = new Intent();
            Charset charset = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                charset = StandardCharsets.ISO_8859_1;
            }
            while (true) {
                try {
                    mByte = inS.read(tmpData);
                    inputStream.write(tmpData,0, mByte);
                    if(inputStream.size() >= 9) {
                        sendData = inputStream.toByteArray();
//                        final String msgReceived = String.format(String.format(String.format("%02X", sendData[0]) + String.format("%02X", sendData[1]) + String.format("%02X", sendData[2])
//                                + String.format("%02X", sendData[3]) + String.format("%02X", sendData[4]) + String.format("%02X", sendData[5])
//                                + String.format("%02X", sendData[6]) + String.format("%02X", sendData[7])
//                                + String.format("%02X", sendData[8])));
//
//                        Log.i("mByte ", "--" + msgReceived);
                        String dataSend = new String(sendData, charset);
                        result.setAction(mBroadcastGetData);
                        result.putExtra("fbData", dataSend);
                        sendBroadcast(result);
                        inputStream.reset();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("hhhh ", "errr");
                }
            }
        }

        public void write(byte[] buff){
            try {
                outS.write(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public byte[] read() {
            return sendData;
        }
        public String readString() {
            return "hello";
        }
        public  int readInt() {
            return mByte;
        }
        private void cancel(){
            try {
                cSocket.close();
                Log.d("hhhh","connected thread cancel method");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        this.stop();
        super.onDestroy();
    }
    int numberSend = 10;
    void send_Data() {
        numberSend++;
        Intent result = new Intent();
        result.putExtra("fbData", String.valueOf(numberSend));
        try {
            data.send(classicBluetooth.this, 200, result);
            Log.i("hhhh", "----------------------------");
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    public void task()
    {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    send_Data();
                }
                catch (NullPointerException ex) {
                }
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer("Timer");
        timer.schedule(timerTask, 0, 2000);
    }
}