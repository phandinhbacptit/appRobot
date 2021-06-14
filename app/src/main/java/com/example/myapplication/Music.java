package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Set;

public class Music extends AppCompatActivity {
    ImageButton MusicBackBtn, musicConnectBluetooth;
    View c_s, d_s, e_s, f_s, g_s, a_s, b_s;
    View c_d_s, d_e_s, f_g_s, g_a_s, a_b_s;
    BluetoothAdapter bluetoothAdapter;
//    ThreadConnectBTdevice myThreadConnectBTdevice;
    byte[] C = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x0d,0x02,(byte)0xfa,0x00};
    byte[] C_D = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x2a,0x02,(byte)0xfa,0x00};
    byte[] D = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x4b,0x02,(byte)0xfa,0x00};
    byte[] D_E = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x06e,0x02,(byte)0xfa,0x00};
    byte[] E = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, (byte)0x93,0x02,(byte)0xfa,0x00};
    byte[] F = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, (byte)0xba,0x02,(byte)0xfa,0x00};
    byte[] F_G = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, (byte)0xe4,0x02,(byte)0xfa,0x00};
    byte[] G = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x10,0x03,(byte)0xfa,0x00};
    byte[] G_A = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x3e,0x03,(byte)0xfa,0x00};
    byte[] A = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, 0x70,0x03,(byte)0xfa,0x00};
    byte[] A_B = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, (byte)0xa4,0x03,(byte)0xfa,0x00};
    byte[] B = {(byte)0xff, 0x55, 0x07, 0x0, 0x02, 0x09, (byte)0xdb,0x03,(byte)0xfa,0x00};
    private static final int REQUEST_ENABLE_BT = 1;
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

        MusicBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Music.this, MainActivity.class));
            }
        });

        /* Handle connect button when clicked*/
        musicConnectBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Toast.makeText(Music.this,
                            "Bluetooth is not supported on this hardware platform",
                            Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(Music.this,"Bluetooth is diable", Toast.LENGTH_LONG).show();
//                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
//                setup();
            }
        });

        c_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(C);
//                }
            }
        });
        d_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(D);
//                }
            }
        });
        e_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(E);
//                }
            }
        });
        f_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(F);
//                }
            }
        });
        g_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(G);
//                }
            }
        });
        a_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(A);
//                }
            }
        });
        b_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(B);
//                }
            }
        });

        c_d_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(C_D);
//                }
            }
        });

        d_e_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(D_E);
//                }
            }
        });

        f_g_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(F_G);
//                }
            }
        });

        g_a_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(G_A);
//                }
            }
        });

        a_b_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myThreadConnectBTdevice. getStateConnected()  != null) {
//                    myThreadConnectBTdevice. getStateConnected() .write(A_B);
//                }
            }
        });
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            Toast.makeText(Music.this,
//                    "Bluetooth is not supported on this hardware platform",
//                    Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
    }

    private void setup( )
    {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("Robox")) {
                    Toast.makeText(Music.this, "Start thread connect to bluetooth device", Toast.LENGTH_LONG).show();
//                    if (myThreadConnectBTdevice == null) {
//                        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
//                    }
//                    myThreadConnectBTdevice.start();
//                    if (myThreadConnectBTdevice.getStatusConnect())
//                        musicConnectBluetooth.setBackgroundResource(R.drawable.ic_ble_on);
//                    else
//                        musicConnectBluetooth.setBackgroundResource(R.drawable.ic_ble_off);
                }
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == Activity.RESULT_OK) {
//                setup();
//            }
//            else {
//                Toast.makeText(this, "BlueTooth NOT enabled", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
}

