package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.classicBluetooth.LocalBinder;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Music extends AppCompatActivity {
    ImageButton MusicBackBtn, musicConnectBluetooth;
    View c_s, d_s, e_s, f_s, g_s, a_s, b_s;
    View c_d_s, d_e_s, f_g_s, g_a_s, a_b_s;
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    Timer timer;

    classicBluetooth bluemusic;
    boolean stateBond = false;

    void run_tone (int fre_tone) {
        shareFunction.runBuzzer(0,0,0, fre_tone,250);
        if (bluemusic.getInstance() != null) {
            bluemusic.getInstance().write(define.cmdRunModule);
        }
    }
    ServiceConnection musicConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            bluemusic = binder.getService();
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
                        musicConnectBluetooth.setBackgroundResource(R.drawable.ic_ble_on);
                        timer.cancel();
                    } else {
                        musicConnectBluetooth.setBackgroundResource(R.drawable.ic_ble_off);
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
        setContentView(R.layout.activity_music);
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

        MusicBackBtn = (ImageButton) findViewById(R.id.btnMusicBack);
        musicConnectBluetooth = (ImageButton)findViewById(R.id.btnConnectBluetooth) ;
        c_s = (View)findViewById(R.id.c_sound);
        d_s = (View)findViewById(R.id.d_sound);
        e_s = (View)findViewById(R.id.e_sound);
        f_s = (View)findViewById(R.id.f_sound);
        g_s = (View)findViewById(R.id.g_sound);
        a_s = (View)findViewById(R.id.a_sound);
        b_s = (View)findViewById(R.id.b_sound);

        c_d_s = (View)findViewById(R.id.c_d_sound);
        d_e_s = (View)findViewById(R.id.d_e_sound);
        f_g_s = (View)findViewById(R.id.f_g_sound);
        g_a_s = (View)findViewById(R.id.g_a_sound);
        a_b_s = (View)findViewById(R.id.a_b_sound);

        Intent intent = new Intent(this, classicBluetooth.class);
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
        check_connected();

        MusicBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Music.this, play.class));
            }
        });

        /* Handle connect button when clicked*/
        musicConnectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluemusic.statusConnect = false;
                startActivity(new Intent(Music.this, connectingBluetooth.class));

            }
        });

        c_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.C);
            }
        });
        d_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.D);
            }
        });
        e_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.E);
            }
        });
        f_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.F);
            }
        });
        g_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.G);
            }
        });
        a_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.A);
            }
        });
        b_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.B);
            }
        });

        c_d_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.C_D);
            }
        });

        d_e_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.D_E);
            }
        });

        f_g_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.F_G);
            }
        });

        g_a_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.G_A);
            }
        });

        a_b_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_tone(define.A_B);
            }
        });
    }
}

