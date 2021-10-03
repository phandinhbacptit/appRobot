package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class play extends AppCompatActivity {
    ImageView selectControl, selectMusic, selectVoiceControl;
    ImageButton btn_previous, btn_connectBlue;
    classicBluetooth bluePlay;
    Timer timer;
    boolean stateBond = false;

    ServiceConnection playConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            classicBluetooth.LocalBinder binder = (classicBluetooth.LocalBinder) service;
            bluePlay = binder.getService();
            stateBond = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            stateBond = false;
        }
    };

        private void check_connected() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (connectingBluetooth.getStateConnectedBlue()) {
                        btn_connectBlue.setBackgroundResource(R.drawable.ic_ble_on);
                        timer.cancel();
                    } else {
                        btn_connectBlue.setBackgroundResource(R.drawable.ic_ble_off);
                    }
                } catch (NullPointerException ex) {
                }
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer("Timer");
        timer.schedule(timerTask, 0, 1000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
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
        selectControl = (ImageView) findViewById(R.id.btn_control);
        selectMusic = (ImageView) findViewById(R.id.play_music);
        selectVoiceControl = (ImageView) findViewById(R.id.voice_control);
        btn_previous = (ImageButton) findViewById(R.id.btnBackPlay);
        btn_connectBlue = (ImageButton) findViewById(R.id.btnConnBluePlay);

        Intent intent = new Intent(this, classicBluetooth.class);
        bindService(intent, playConnection, Context.BIND_AUTO_CREATE);
        check_connected();

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(play.this, MainActivity.class));
            }
        });


        selectControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openControl();
            }
        });

        selectMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMusic();
            }
        });

        selectVoiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVoiceControl();
            }
        });

        btn_connectBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluePlay.statusConnect = false;
                startActivity(new Intent(play.this, connectingBluetooth.class));
            }
        });
    }

    public void openControl() {
        Intent intent = new Intent(this, Control.class);
        startActivity(intent);
    }
    public void openMusic() {
        Intent intent = new Intent(this, Music.class);
        startActivity(intent);
    }
    public void openVoiceControl() {
        Intent intent = new Intent(this, voiceControl.class);
        startActivity(intent);
    }
}