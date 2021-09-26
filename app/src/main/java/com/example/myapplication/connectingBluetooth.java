package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.ImageButton;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class connectingBluetooth extends AppCompatActivity {
    ImageButton btnBack;
    classicBluetooth blueConnecting;
    boolean stateBond = false;
    Timer timer;
    static boolean stateConnected = false;
    private static final String mBroadcastGetData = "VrobotGetData";
    private IntentFilter mIntentFilter;

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
                     Log.i("hhhh","connected to the bluetooth Device");
                     finish();
                     timer.cancel();
                     stateConnected = true;
                }
                else {
                     blueConnecting.retry_connect();
                     Log.i("hhhh","retry_connect");
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
        btnBack = (ImageButton)findViewById(R.id.returnPrevious);
        ServiceConnection Connection = new ServiceConnection()
        {
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

        Intent intent = new Intent(connectingBluetooth.this, classicBluetooth.class);
        bindService(intent, Connection, Context.BIND_AUTO_CREATE);
//
//        PendingIntent pendingResult = createPendingResult(100, new Intent(), 0);
////        Intent intent1 = new Intent(connectingBluetooth.this, classicBluetooth.class);
//        intent.putExtra("requestCode", pendingResult);
//        startService(intent);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastGetData);
        Intent serviceIntent = new Intent(this, classicBluetooth.class);
        startService(serviceIntent);

        check_connected();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                stateConnected =  false;
            }
        });
    }
}