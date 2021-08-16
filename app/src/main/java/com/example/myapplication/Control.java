package com.example.myapplication;

import com.example.myapplication.shareFunction;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.classicBluetooth.LocalBinder;
import com.example.myapplication.userConfig;
import com.example.myapplication.define;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.view.View.OnTouchListener;

public class Control extends AppCompatActivity {
    ImageButton backCtrBtn;
    ImageButton btnDance, btnLed, btnConnect, btnBuzzer, btnLedMatrix, btnRingLed;
    ImageButton btnGetSrf05, btnGetLine, btnGetColor, btnGetSound, btnGetLight, btnGetBtn;
    TextView textSrf05, textLight, textColor, textSound, textServo1, textServo2, lineRight, lineLeft;
    pl.droidsonroids.gif.GifImageView soundSignal;
    SeekBar servo1, servo2;
    boolean state_led = false;
    int ledColor = 0;
    int leftSpeed = 0, rightSpeed = 0;
    int buzzerFreq = 0, buzzerDuration = 0;
    int xPos, yPos, volumeSpeed;
    int index = 0;
    boolean stateGetSrf05, stateGetLightSensor, stateGetLine, stateGetButton, stateGetColor;
    int val = 0;
    int cnt_effect;
    Timer timer;
    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    TextView textView1, textView2, textView3, textView4, textView5;
    joystick js;
    byte[] fbData = {0, 0, 0, 0};
    public static final String mBroadcastGetData = "VrobotGetData";
    private IntentFilter mIntentFilter;

    classicBluetooth blueControl;
    boolean stateBond = false;
    final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

    private ServiceConnection ctrConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            blueControl = binder.getService();
            stateBond = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            stateBond = false;
        }
    };

    public void getData(int module) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (blueControl.getInstance() != null) {
                    define.cmd_get_valModule[5] = (byte) module;
                    blueControl.getInstance().write(define.cmd_get_valModule);
                }
                Log.i("hhhh", "Sent request read sensor data");
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer("Timer");
        if (module != 0) {
            timer.schedule(timerTask, 0, 500);
        } else {
            textSrf05.setText("");
            textLight.setText("");
            if (timer != null) {
                timer.cancel();
                ;
            }
        }
    }

    public void check_connected() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (connectingBluetooth.getStateConnectedBlue()) {
                        btnConnect.setBackgroundResource(R.drawable.ic_ble_on);
                        timer.cancel();
                    } else {
                        btnConnect.setBackgroundResource(R.drawable.ic_ble_off);
                    }
                } catch (NullPointerException ex) {
                }
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer("Timer");
        timer.schedule(timerTask, 0, 5000);
    }

    void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
//    void dutySend(long duration){
//        new CountDownTimer(6000, duration) {
//            @Override
//            public void onTick(long i) {
//                val++;
//                textLine.setText(String.format("%d",val));
//            }
//            @Override
//            public  void onFinish() {
//
//            }
//        }.start();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
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

        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);

//        btnDance = (ImageButton)findViewById(R.id.btn_dance);
        btnLed = (ImageButton) findViewById(R.id.btn_ledRGB);
        btnLedMatrix = (ImageButton) findViewById(R.id.btn_ledmatrix);
        btnRingLed = (ImageButton) findViewById(R.id.btn_ringled);
        backCtrBtn = (ImageButton) findViewById(R.id.btnCtrBack);
        btnConnect = (ImageButton) findViewById(R.id.btnConnectBluetooth);
        btnBuzzer = (ImageButton) findViewById(R.id.btn_buzzer);

        btnGetSrf05 = (ImageButton) findViewById(R.id.btnIconSrf05);
        btnGetColor = (ImageButton) findViewById(R.id.btnIconColor);
        btnGetLight = (ImageButton) findViewById(R.id.btnIconLight);
        btnGetLine = (ImageButton) findViewById(R.id.btnIconLine);
        btnGetBtn = (ImageButton) findViewById(R.id.btnMode1);
        btnGetColor = (ImageButton) findViewById(R.id.btnIconColor);

        textSrf05 = (TextView) findViewById(R.id.text_srf05);
        textSrf05.setTypeface(null, Typeface.BOLD);

        lineLeft = (TextView)findViewById(R.id.line_left);
        lineRight = (TextView)findViewById(R.id.line_right);

        textLight = (TextView) findViewById(R.id.text_light);
        textLight.setTypeface(null, Typeface.BOLD);

        textColor = (TextView) findViewById(R.id.text_color);
        textColor.setTypeface(null, Typeface.BOLD);

        textServo1 = (TextView) findViewById(R.id.val_servo1);
        textServo1.setTypeface(null, Typeface.BOLD);
        servo1 = (SeekBar) findViewById(R.id.sbServo1);

        textServo2 = (TextView) findViewById(R.id.val_servo2);
        textServo2.setTypeface(null, Typeface.BOLD);
        servo2 = (SeekBar) findViewById(R.id.sbServo2);
        soundSignal = (pl.droidsonroids.gif.GifImageView) findViewById(R.id.btnSound);

        /* Handle back button when clicked */
        backCtrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Control.this, MainActivity.class));
            }
        });
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastGetData);
        Intent intent = new Intent(this, classicBluetooth.class);
        bindService(intent, ctrConnection, Context.BIND_AUTO_CREATE);
        /* Handle connect button when clicked*/
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!bAdapter.isEnabled()){
//                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
//                    Toast.makeText(getApplicationContext(),"Bluetooth Turned ON",Toast.LENGTH_SHORT).show();
//                }

                startActivity(new Intent(Control.this, connectingBluetooth.class));
                check_connected();
//                delay(2000);
//                PendingIntent pendingResult = createPendingResult(100, new Intent(), 0);
//                Intent intent = new Intent(Control.this, classicBluetooth.class);
//                intent.putExtra("requestCode", pendingResult);
//                startService(intent);

//                if (blueControl.get_state_blue_connect()) {
//                    btnConnect.setBackgroundResource(R.drawable.ic_ble_on);
//                    //Toast.makeText(Control.this, "Connect successfull", Toast.LENGTH_LONG).show();
//                } else {
//                    btnConnect.setBackgroundResource(R.drawable.ic_ble_off);
//                    //Toast.makeText(Control.this, "Connect fail", Toast.LENGTH_LONG).show();
//                }
            }
        });

//        btnDance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i <= 255 ; i = i + 1 ) {
//                    leftSpeed = -(255 - i);
//                    rightSpeed = -(255 - i);
//                    joyStick[6] = (byte) (leftSpeed & (byte) 0xff);
//                    joyStick[7] = (byte) ((leftSpeed >> 8) & (byte) 0xff);
//
//                    joyStick[8] = (byte) (rightSpeed & (byte) 0xff);
//                    joyStick[9] = (byte) ((rightSpeed >> 8) & (byte) 0xff);
//                    if (myThreadConnected != null) {
//                        myThreadConnected.write(joyStick);
//                    }
//                    for (int j = 0; j< 10000; j++) {
//                        for (int k = 0; k < 2000; k++) {}
//                    }
//                }
//            }
//        });
        btnLed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (ledColor) {
                    case define.RED:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_red);
                        shareFunction.runRGB(0, 0, 0, define.RED_COLOR);
                        break;
                    case define.GREEN:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_green);
                        shareFunction.runRGB(0, 0, 0, define.GREEN_COLOR);
                        break;
                    case define.BLUE:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_blue);
                        shareFunction.runRGB(0, 0, 0, define.BLUE_COLOR);
                        break;
                    case define.YELLOW:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_yellow);
                        shareFunction.runRGB(0, 0, 0, define.YELLOW_COLOR);
                        break;
                    case define.PURPLE:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_purple);
                        shareFunction.runRGB(0, 0, 0, define.PURPLE_COLOR);
                        break;
                    case define.PINK:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_pink);
                        shareFunction.runRGB(0, 0, 0, define.PINK_COLOR);
                        break;
                    case define.WHITE:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_white);
                        shareFunction.runRGB(0, 0, 0, define.WHITE_COLOR);
                        break;
                    default:
                        btnLed.setBackgroundResource(R.drawable.ic_rgb_off);
                        shareFunction.runRGB(0, 0, 0, define.BLACK_COLOR);
                        break;
                }
                if (blueControl.getInstance() != null) {
                    blueControl.getInstance().write(define.cmdRunModule);
                }
                ledColor++;
                if (ledColor > define.WHITE)
                    ledColor = 0;
            }
        });

        btnLedMatrix.setOnClickListener(new View.OnClickListener() {
            int duration = 0x0a;

            @Override
            public void onClick(View v) {
                cnt_effect++;
                if (cnt_effect >= 12)
                    cnt_effect = 0;
                shareFunction.runMaTrix(0, 0, 0, define.motion_effect[cnt_effect], duration);
                if (blueControl.getInstance() != null) {
                    blueControl.getInstance().write(define.cmdRunModule);
                }
            }
        });
        btnBuzzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index >= 25)
                    index = 0;
                buzzerFreq = define.hpbdSong[index];
                buzzerDuration = 250;
                shareFunction.runBuzzer(0, 0, 0, buzzerFreq, buzzerDuration);
                if (blueControl.getInstance() != null) {
                    blueControl.getInstance().write(define.cmdRunModule);
                }
                index++;
            }
        });

        btnGetSrf05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateGetSrf05 = !stateGetSrf05;
                if (stateGetSrf05) {
                    getData(define.SRF05);
                    btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05_select);
                    btnGetLine.setBackgroundResource(R.drawable.ic_read_line);
                    btnGetLight.setBackgroundResource(R.drawable.ic_read_light);
                } else {
                    getData(define.NONE);
                    btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05);
                }
            }
        });

        btnGetLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateGetLine = !stateGetLine;
                if (stateGetLine) {
                    getData(define.LINE);
                    btnGetLine.setBackgroundResource(R.drawable.ic_read_line_select);
                    btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05);
                    btnGetLight.setBackgroundResource(R.drawable.ic_read_light);
                } else {
                    getData(define.NONE);
                    btnGetLine.setBackgroundResource(R.drawable.ic_read_line);
                }
            }
        });
        btnGetLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateGetLightSensor = !stateGetLightSensor;
                if (stateGetLightSensor) {
                    getData(define.LIGHT);
                    btnGetLight.setBackgroundResource(R.drawable.ic_read_light_select);
                    btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05);
                    btnGetLine.setBackgroundResource(R.drawable.ic_read_line);
                } else {
                    getData(define.NONE);
                    btnGetLight.setBackgroundResource(R.drawable.ic_read_light);
                }
            }
        });

        btnGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateGetButton = !stateGetButton;
                if (stateGetButton) {
                    getData(define.MODE_BTN);
//                    btnGetBtn.setBackgroundResource(R.drawable.button);
                } else {
                    getData(define.NONE);
                    //btnGetBtn.setBackgroundResource(R.drawable.butto_off);
                }
            }
        });
        btnGetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateGetColor = !stateGetColor;
                if (stateGetColor) {
                    soundSignal.setBackgroundResource(R.drawable.have_sound);
                } else {
                    soundSignal.setBackgroundResource(R.drawable.ic_sound);
                }
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(Control.this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        servo1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textServo1.setText("Servo1 quay: " + progress + " độ");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        servo2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textServo2.setText("Servo2 quay: " + progress + " độ");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        js = new joystick(getApplicationContext(), layout_joystick, R.drawable.joystick_center);
        js.setStickSize(180, 180);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(255);
        js.setStickAlpha(255);
        js.setOffset(90);
        js.setMinimumDistance(20);

        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    xPos = (int) js.getX();
                    if (xPos > 255)
                        xPos = 255;

                    yPos = (int) js.getY();
                    if (yPos > 255)
                        yPos = 255;

                    volumeSpeed = (int) js.getDistance();
                    if (volumeSpeed > 255)
                        volumeSpeed = 255;

                    int direction = js.get8Direction();
                    switch (direction) {
                        case joystick.STICK_UP: {
                            leftSpeed = volumeSpeed - yPos;
                            rightSpeed = -(volumeSpeed - yPos);
                            break;
                        }
                        case joystick.STICK_UPRIGHT: {
                            leftSpeed = volumeSpeed - (-1 * xPos);
                            rightSpeed = -volumeSpeed;
                            break;
                        }
                        case joystick.STICK_RIGHT: {
                            leftSpeed = volumeSpeed + xPos;
                            rightSpeed = volumeSpeed;
                            break;
                        }
                        case joystick.STICK_DOWNRIGHT: {
                            leftSpeed = -(volumeSpeed - (-1 * xPos));
                            rightSpeed = volumeSpeed;
                            break;
                        }
                        case joystick.STICK_DOWN: {
                            leftSpeed = -(volumeSpeed + yPos);
                            rightSpeed = volumeSpeed + yPos;
                            break;
                        }
                        case joystick.STICK_DOWNLEFT: {
                            leftSpeed = -volumeSpeed;
                            rightSpeed = (volumeSpeed - xPos);
                            break;
                        }
                        case joystick.STICK_LEFT: {
                            leftSpeed = -volumeSpeed;
                            rightSpeed = -(volumeSpeed - xPos);
                            break;
                        }
                        case joystick.STICK_UPLEFT: {
                            leftSpeed = volumeSpeed;
                            rightSpeed = -(volumeSpeed - xPos);
                            break;
                        }
                        case joystick.STICK_NONE: {
                            leftSpeed = 0;
                            rightSpeed = 0;
                            break;
                        }
                        default:
                            leftSpeed = 0;
                            rightSpeed = 0;
                            break;
                    }
                }
                if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    leftSpeed = 0;
                    rightSpeed = 0;
                }
                shareFunction.runJoystick(0, 0, 0, leftSpeed, rightSpeed);
                if (blueControl.getInstance() != null) {
                    blueControl.getInstance().write(define.cmdRunModule);
                }
                return true;
            }
        });

    }
    ;
    @Override
    protected void onStart() {
        super.onStart();
    }

    byte[] handleData(byte[] inBuffer) {
        byte[] bufGet = {0, 0, 0, 0};

        for (int i = 0; i < 4; i++) {
            bufGet[i] = inBuffer[i + 2];
        }
        return bufGet;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Charset charset = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                charset = StandardCharsets.ISO_8859_1;
            }
            Log.i("hhhh"," on BroadcastReceiver");
            if (intent.getAction().equals(mBroadcastGetData)) {
                String inputData = intent.getStringExtra("fbData");
                byte[] buffer = inputData.getBytes(charset);
                fbData = handleData(buffer);
                String displayText = Float.toString(shareFunction.byteArray2Float(fbData));
                switch (buffer[1]) {
                    case define.SRF05: {
                        textSrf05.setText(displayText);
                        textColor.setText("");
                        textLight.setText("");
                        break;
                    }
                    case define.LINE: {
                        textSrf05.setText("");
                        textColor.setText("");
                        textLight.setText("");
                        switch ((int)shareFunction.byteArray2Float(fbData)) {
                            case define.ALL_ON:
                                lineLeft.setBackgroundResource(R.drawable.ic_line_on);
                                lineRight.setBackgroundResource(R.drawable.ic_line_on);
                                break;
                            case define.LEFT_ON:
                                lineLeft.setBackgroundResource(R.drawable.ic_line_on);
                                lineRight.setBackgroundResource(R.drawable.ic_line_off);
                                break;
                            case define.RIGHT_ON:
                                lineLeft.setBackgroundResource(R.drawable.ic_line_off);
                                lineRight.setBackgroundResource(R.drawable.ic_line_on);
                                break;
                            default:
                                lineLeft.setBackgroundResource(R.drawable.ic_line_off);
                                lineRight.setBackgroundResource(R.drawable.ic_line_off);
                                break;
                        }
                        break;
                    }
                    case define.LIGHT:
                        textSrf05.setText("");
                        textColor.setText("");
                        textLight.setText(displayText);
                        break;
                    case define.COLOR:
                        textSrf05.setText("");
                        textColor.setText(displayText);
                        textLight.setText("");
                        break;
                    case define.SOUND:
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
}
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        Charset charset = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            charset = StandardCharsets.ISO_8859_1;
//        }
//        Log.i("hhhh", "onActivityResult");
//        if (requestCode == 100 && resultCode == 200) {
//
//            String inputData = data.getStringExtra("fbData");
//            byte[] buffer = inputData.getBytes(charset);
//            fbData = handleData(buffer);
//            String displayText = Float.toString(shareFunction.byteArray2Float(fbData));
////            String msgReceived = "";
////            byte[] buffer = blueControl.getInstance().read();
////            final String msgReceived = String.format(String.format(String.format("%02X", buffer[0]) + String.format("%02X", buffer[1]) + String.format("%02X", buffer[2])
////                    + String.format("%02X", buffer[3]) + String.format("%02X", buffer[4]) + String.format("%02X", buffer[5])
////                    + String.format("%02X", buffer[6]) + String.format("%02X", buffer[7])));
////            textLight.setText(Float.toString(byteArray2Float(buffer)));
//            switch (buffer[1]) {
//                case define.SRF05: {
//                    textSrf05.setText(displayText);
////                    textLine.setText("");
//                    textColor.setText("");
//                    textLight.setText("");
//                    break;
//                }
//                case define.LINE:
//                    textSrf05.setText("");
////                    textLine.setText(displayText);
//                    textColor.setText(displayText);
//                    textLight.setText("");
//                    break;
//                case define.LIGHT:
//                    textSrf05.setText("");
////                    textLine.setText("");
//                    textColor.setText("");
//                    textLight.setText(displayText);
//                    break;
//                case define.COLOR:
//                    textSrf05.setText("");
////                    textLine.setText("");
//                    textColor.setText(displayText);
//                    textLight.setText("");
//                    break;
//                case define.SOUND:
//                    break;
//                default:
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}
// Create a BroadcastReceiver for ACTION_FOUND
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

//    private void setup( ) {
//        /*bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        //check to see if there is BT on the Android device at all
//        if (bluetoothAdapter == null){
//            int duration = Toast.LENGTH_SHORT;
//            Toast.makeText(this, "No Bluetooth on this handset", duration).show();
//        }
//        if (bluetoothAdapter.isDiscovering()){
//            bluetoothAdapter.cancelDiscovery();
//        }
//        //re-start discovery
//        bluetoothAdapter.startDiscovery();
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//        */
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                if (device.getName().equals("Robox")) {
//                    Toast.makeText(Control.this, "Start thread connect to bluetooth device", Toast.LENGTH_LONG).show();
//                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
//                    myThreadConnectBTdevice.start();
//                    if (myThreadConnectBTdevice.getStatusConnect())
//                        btnConnect.setBackgroundResource(R.drawable.ic_ble_on);
//                    else
//                        btnConnect.setBackgroundResource(R.drawable.ic_ble_off);
//                }
//            }
//        }
//    }
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
////        if (myThreadConnectBTdevice!=null) {
////            myThreadConnectBTdevice.cancel();
////        }
////        unregisterReceiver(mReceiver, filter);
//    }
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
    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
//    private void startThreadConnected(BluetoothSocket socket)
//    {
//        myThreadConnected = new ThreadConnected(socket);
//        myThreadConnected.start();
//    }
//    private class ThreadConnectBTdevice extends Thread {
//
//        private BluetoothSocket bluetoothSocket = null;
//        private final BluetoothDevice bluetoothDevice;
//
//        private ThreadConnectBTdevice(BluetoothDevice device) {
//            bluetoothDevice = device;
//            try {
//                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
//                btnConnect.setBackgroundResource(R.drawable.ic_ble_on);
////                textStatus.setText("bluetoothSocket: \n" + bluetoothSocket);
//            } catch (IOException e) {
//                btnConnect.setBackgroundResource(R.drawable.ic_ble_off);
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
//    }

    /*
    ThreadConnected:
    Background Thread to handle Bluetooth data communication
    after connected
     */
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
//
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
//                        for (int i = 0; i < 4; i++) {
//                            byteSrf05[i] = buffer[i+2];
//                        }
//                        textSrf05.setText(Float.toString(byteArray2Float(byteSrf05)));
//                        textLine.setText(" ");
//                        textLight.setText(" ");
//                    }
//                    if ((buffer[1] == (byte)0x02)) { // Get Line Sensor value
//                        for (int i = 0; i < 4; i++) {
//                            byteLine[i] = buffer[i+2];
//                        }
//                        textLine.setText(Float.toString(byteArray2Float(byteLine)));
//                        textSrf05.setText(" ");
//                        textLight.setText(" ");
//                    }
//                    if ((buffer[1] ==  (byte)0x03)) { // get Light Sensor value
//                        for (int i = 0; i < 4; i++) {
//                            byteLightSensor[i] = buffer[i+2];
//                        }
//                        textLight.setText(Float.toString(byteArray2Float(byteLightSensor)));
//                        textLine.setText(" ");
//                        textSrf05.setText(" ");
//                    }
//                    if ((buffer[1] ==  (byte)0x06)) { // get Light Sensor value
//                        for (int i = 0; i < 4; i++) {
//                            byteBtn[i] = buffer[i+2];
//                        }
//                        textColor.setText(Float.toString(byteArray2Float(byteBtn)));
////                        textColor.setText("hello");
//
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
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            textLine.setText(msgReceived);
////                         textColor.setText(msgReceived);
//
//                        }});
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
//
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