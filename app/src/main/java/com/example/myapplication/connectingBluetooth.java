package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class connectingBluetooth extends AppCompatActivity {
    ImageButton btnCancle;
    classicBluetooth blueConnecting;
    boolean stateBond = false;
    Timer timer;
    static boolean stateConnected = false;
    private static final String mBroadcastGetData = "VrobotGetData";
    private IntentFilter mIntentFilter;
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter bAdapter;
    BluetoothDevice bluetoothDevice;
    ListView deviceList;
    ArrayList arrayList;
    private ArrayAdapter<String> btArrayAdapter;

    public void toastMsg(String mess){
        Toast.makeText(this,mess,Toast.LENGTH_SHORT).show();
    }

    public void check_connected()
    {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            try {
                if (blueConnecting.get_state_blue_connect()) {
                     Log.i("TAG","connected to the bluetooth Device");
                     finish();
                     timer.cancel();
                     stateConnected = true;
                }
                else {
                    if(!bAdapter.isDiscovering()){
                        //check BT permissions in manifest
                        checkBTPermissions();
                        bAdapter.startDiscovery();
                        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(mReceiver, discoverDevicesIntent);
                    }
                    blueConnecting.retry_connect();
                    Log.i("TAG","retry_connect");
                }
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
    public static boolean getStateConnectedBlue() {
        return stateConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_bluetooth);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        btnCancle = (ImageButton)findViewById(R.id.returnPrevious);
        deviceList = (ListView)findViewById(R.id.listDevice);
        arrayList = new ArrayList();

        btArrayAdapter = new ArrayAdapter<String>(connectingBluetooth.this, android.R.layout.simple_list_item_1, arrayList);
        deviceList.setAdapter(btArrayAdapter);

        ServiceConnection Connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                classicBluetooth.LocalBinder binder = (classicBluetooth.LocalBinder) service;
                blueConnecting = binder.getService();
                stateBond = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                stateBond = false;
            }
        };

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }

        if(bAdapter.isDiscovering()){
            bAdapter.cancelDiscovery();
            Log.i("TAG", "btnDiscover: Canceling discovery.");
            //check BT permissions in manifest
            checkBTPermissions();
            bAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, discoverDevicesIntent);
        }
        if(!bAdapter.isDiscovering()){
            //check BT permissions in manifest
            checkBTPermissions();
            bAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, discoverDevicesIntent);
        }

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter);

        Intent intent = new Intent(connectingBluetooth.this, classicBluetooth.class);
        bindService(intent, Connection, Context.BIND_AUTO_CREATE);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastGetData);
        Intent serviceIntent = new Intent(this, classicBluetooth.class);
        startService(serviceIntent);
        check_connected();

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueConnecting.stopService(serviceIntent);
//                bAdapter = BluetoothAdapter.getDefaultAdapter();
//                if(bAdapter.isDiscovering()){
//                bAdapter.cancelDiscovery();
//                stopService(serviceIntent);
//                    Log.i("TAG", "btnDiscover: Canceling discovery.");
//                }
                Log.i("TAG", "btnDiscover: Distroy everything.");
                finish();
//                unregisterReceiver(mReceiver);
//                unregisterReceiver(mBroadcastReceiver1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(this, "BlueTooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "BlueTooth NOT enabled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.i("TAG", "onReceive: ACTION FOUND.");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // Add the name and address to an array adapter to show in a ListView
            try {
                if (device.getName().equals("Robox")) {
//                    device.createBond();
//                    Method m = device.getClass()
//                            .getMethod("createBond", (Class[]) null);
//                    m.invoke(device, (Object[]) null);
                    arrayList.add(device.getName() + "\n" + device.getAddress());
                    btArrayAdapter.notifyDataSetChanged();
                    blueConnecting.connectToDevice(device);
                    bAdapter.cancelDiscovery();
                    Log.i("TAG", "btnDiscover: Canceling discovery.");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.i("TAG", "BroadcastReceiver: BOND_BONDED.");
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.i("TAG", "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.i("TAG", "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
        unregisterReceiver(mBroadcastReceiver1);
    }

    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.i("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}

