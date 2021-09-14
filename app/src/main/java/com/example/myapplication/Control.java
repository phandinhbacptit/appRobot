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
import android.provider.ContactsContract;
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
    ImageButton btnModeSrf05, btnModeSoundDetect, btnModeFollwingLine, btnModeRunCircle;
    TextView textSrf05, textLight, textColor, textSound, textServo1, textServo2, lineRight, lineLeft;
    ImageButton showMode1, showMode2, showMode3;
    pl.droidsonroids.gif.GifImageView soundSignal;
    SeekBar servo1, servo2;
    boolean state_led = false;
    int ledColor = 0;
    int leftSpeed = 0, rightSpeed = 0;
    int buzzerFreq = 0, buzzerDuration = 0;
    int xPos, yPos, volumeSpeed;
    int index = 0;
    boolean stateGetSrf05, stateGetLightSensor, stateGetLine, stateGetButton, stateGetColor, stateGetSound;
    boolean stateRunFollowingLine, stateRunInCircle, stateRunSrf05, stateRunSoundMode;
    int val = 0;
    int cnt_effect, cnt_effect_ring;
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
    void sendRunCommand()
    {
        if (blueControl.getInstance() != null) {
            blueControl.getInstance().write(define.cmdRunModule);
        }
    }
    void sendGetCommand(int module, int stateModule) {
        if (blueControl.getInstance() != null) {
            define.cmd_get_valModule[5] = (byte) module;
            define.cmd_get_valModule[6] = (byte) stateModule;
            blueControl.getInstance().write(define.cmd_get_valModule);
        }
    }
    int _module = 0;
    public void getData(int module) {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendGetCommand(module, define.ON_MODULE);
                Log.i("hhhh", "Sent request read sensor data");
            }
        };
        if (timer != null)
            timer.cancel();
        timer = new Timer("Timer");
        if (module != 0) {
            timer.schedule(timerTask, 0, 50);
            _module = module;
        }
        else {
            sendGetCommand(_module, define.OFF_MODULE);
            textSrf05.setText("");
            textLight.setText("");
            if (timer != null) {
                timer.cancel();
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

    void resetState(boolean state)
    {
        if (stateGetSrf05 != state)
            stateGetSrf05 = false;
        if (stateGetLightSensor != state)
            stateGetLightSensor = false;
        if (stateGetLine != state)
            stateGetLine = false;
        if (stateGetButton != state)
            stateGetButton = false;
        if (stateGetColor != state)
            stateGetColor = false;
        if (stateGetSound != state)
            stateGetSound = false;
        if (stateRunFollowingLine != state)
            stateRunFollowingLine = false;
        if (stateRunInCircle != state)
            stateRunInCircle = false;
        if (stateRunSrf05 != state)
            stateRunSrf05 = false;
        if (stateRunSoundMode != state)
            stateRunSoundMode = false;
    }

    void resetBackground()
    {
        btnModeSrf05.setBackgroundResource(R.drawable.ic_srf05_mode);
        btnModeSoundDetect.setBackgroundResource(R.drawable.ic_sound_mode);
        btnModeFollwingLine.setBackgroundResource(R.drawable.ic_line_mode);
        btnModeRunCircle.setBackgroundResource(R.drawable.ic_round_mode);
        btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05);
        btnGetLine.setBackgroundResource(R.drawable.ic_read_line);
        btnGetLight.setBackgroundResource(R.drawable.ic_read_light);
        btnGetBtn.setBackgroundResource(R.drawable.ic_read_button);
        btnGetSound.setBackgroundResource(R.drawable.ic_read_sound);
        lineLeft.setBackgroundResource(R.drawable.ic_line_off);
        lineRight.setBackgroundResource(R.drawable.ic_line_off);
        btnGetColor.setBackgroundResource(R.drawable.ic_read_color);
        textColor.setText("");
        textLight.setText("");
        textSrf05.setText("");
        getData(define.NONE);
    }

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
        btnGetBtn = (ImageButton) findViewById(R.id.btnIconButton);
        btnGetColor = (ImageButton) findViewById(R.id.btnIconColor);
        btnGetSound = (ImageButton) findViewById(R.id.btnIconSound);

        btnModeFollwingLine = (ImageButton) findViewById(R.id.btnLineMode);
        btnModeRunCircle = (ImageButton) findViewById(R.id.btnRoundMode);
        btnModeSoundDetect = (ImageButton) findViewById(R.id.btnSoundMode);
        btnModeSrf05 = (ImageButton) findViewById(R.id.btnSRF05Mode);

        showMode1 = (ImageButton) findViewById(R.id.btnMode1);
        showMode2 = (ImageButton) findViewById(R.id.btnMode2);
        showMode3 = (ImageButton) findViewById(R.id.btnMode3);

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
                startActivity(new Intent(Control.this, play.class));
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
                sendRunCommand();
                ledColor++;
                if (ledColor > define.WHITE)
                    ledColor = 0;
            }
        });

        btnRingLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt_effect_ring++;
                if (cnt_effect_ring >= define.NUM_RING_EFFECT)
                    cnt_effect_ring = 0;
                shareFunction.runRingLed(0,0,0, define.ring_led_effect[cnt_effect_ring]);
                sendRunCommand();
            }
        });
        btnLedMatrix.setOnClickListener(new View.OnClickListener() {
            int duration = 0x1a;

            @Override
            public void onClick(View v) {
                cnt_effect++;
                if (cnt_effect >= 12)
                    cnt_effect = 0;
                shareFunction.runMaTrix(0, 0, 0, define.motion_effect[cnt_effect], duration);
                sendRunCommand();
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
                sendRunCommand();
                index++;
            }
        });

        btnModeSrf05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateRunSrf05);
                stateRunSrf05 = !stateRunSrf05;
                if (stateRunSrf05) {
                    define.cmdRunModule[5] = define.SRF05_RUN_MODE;
                    btnModeSrf05.setBackgroundResource(R.drawable.ic_srf05_mode_select);
                }
                else {
                    define.cmdRunModule[5] = define.NORMAL_MODE;
                }
                sendRunCommand();
            }
        });

        btnModeSoundDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateRunSoundMode);
                stateRunSoundMode = !stateRunSoundMode;
                if (stateRunSoundMode) {
                    define.cmdRunModule[5] = define.SOUND_FOLLOW_MODE;
                    btnModeSoundDetect.setBackgroundResource(R.drawable.ic_sound_mode_select);
                }
                else {
                    define.cmdRunModule[5] = define.NORMAL_MODE;
                }
                sendRunCommand();
            }
        });

        btnModeFollwingLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateRunFollowingLine);
                stateRunFollowingLine = !stateRunFollowingLine;
                if (stateRunFollowingLine) {
                    define.cmdRunModule[5] = define.LINE_DETECT_MODE;
                    btnModeFollwingLine.setBackgroundResource(R.drawable.ic_line_mode_select);
                }
                else {
                    define.cmdRunModule[5] = define.NORMAL_MODE;
                }
                sendRunCommand();
            }
        });

        btnModeRunCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateRunInCircle);
                stateRunInCircle = !stateRunInCircle;
                if (stateRunInCircle) {
                    define.cmdRunModule[5] = define.LINE_CIRCLE_MODE;
                    btnModeRunCircle.setBackgroundResource(R.drawable.ic_round_mode_select);
                }
                else {
                    define.cmdRunModule[5] = define.NORMAL_MODE;
                }
                sendRunCommand();
            }
        });

        btnGetSrf05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetSrf05);
                stateGetSrf05 = !stateGetSrf05;
                if (stateGetSrf05) {
                    getData(define.SRF05);
                    btnGetSrf05.setBackgroundResource(R.drawable.ic_read_srf05_select);
                } else {
                    getData(define.NONE);
                }
            }
        });

        btnGetLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetLine);
                stateGetLine = !stateGetLine;
                if (stateGetLine) {
                    getData(define.LINE);
                    btnGetLine.setBackgroundResource(R.drawable.ic_read_line_select);
                } else {
                    getData(define.NONE);
                }
            }
        });
        btnGetLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetLightSensor);
                stateGetLightSensor = !stateGetLightSensor;
                if (stateGetLightSensor) {
                    getData(define.LIGHT);
                    btnGetLight.setBackgroundResource(R.drawable.ic_read_light_select);
                } else {
                    getData(define.NONE);
                }
            }
        });
        btnGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetButton);
                stateGetButton = !stateGetButton;
                if (stateGetButton) {
                    getData(define.MODE_BTN);
                    btnGetBtn.setBackgroundResource(R.drawable.ic_read_button_select);
                } else {
                    getData(define.NONE);
                }
            }
        });
        btnGetSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetSound);
                stateGetSound = !stateGetSound;
                if (stateGetSound) {
                    getData(define.SOUND);
                    btnGetSound.setBackgroundResource(R.drawable.ic_read_sound_select);
                }
                else {
                    getData(define.NONE);
                }
            }
        });
        btnGetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBackground();
                resetState(stateGetColor);
                stateGetColor = !stateGetColor;
                if (stateGetColor) {
                    getData(define.COLOR);
                    textServo1.setText("hello");
                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_select);
                } else {
                    textServo1.setText("");
                    getData(define.NONE);
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
    @Override
    protected void onStart() {
        super.onStart();
    }

    byte[] handleData(byte[] inBuffer) {
        byte[] bufGet = {0, 0, 0, 0};

        for (int i = 0; i < 4; i++) {
            bufGet[i] = inBuffer[i + 3];
        }
        return bufGet;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    int state =  0;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Charset charset = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                charset = StandardCharsets.ISO_8859_1;
            }
            Log.i("hhhh"," on BroadcastReceiver");
            state++;
            if (intent.getAction().equals(mBroadcastGetData)) {
                String inputData = intent.getStringExtra("fbData");
                byte[] buffer = inputData.getBytes(charset);
//                byte[] buffer1 = blueControl.getInstance().read();
//                final String msgReceived = String.format(String.format(String.format("%02X", buffer1[0]) + String.format("%02X", buffer1[1]) + String.format("%02X", buffer1[2])
//                        + String.format("%02X", buffer1[3]) + String.format("%02X", buffer1[4]) + String.format("%02X", buffer1[5])
//                        + String.format("%02X", buffer1[6]) + String.format("%02X", buffer1[7]) + String.format("%02X", buffer1[8])));
//                Log.i("mByte ", "++" + msgReceived);
                fbData = handleData(buffer);
                String displayText = Float.toString(shareFunction.byteArray2Float(fbData));
                switch (buffer[2]) {
                    case define.SRF05: {
                        if (stateGetSrf05) {
                            textSrf05.setText(displayText);
//                            textColor.setText("");
//                            textLight.setText("");
                        }
                        break;
                    }
                    case define.LINE: {
                        if (stateGetLine) {
//                            textSrf05.setText("");
//                            textColor.setText("");
//                            textLight.setText("");
                            switch ((int) shareFunction.byteArray2Float(fbData)) {
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
                    }
                    case define.LIGHT: {
                        if (stateGetLightSensor) {
//                            textSrf05.setText("");
//                            textColor.setText("");
                            textLight.setText(displayText + "%");
                        }
                        break;
                    }
                    case define.COLOR: {
                        if (stateGetColor) {
//                            textSrf05.setText("");
//                            textLight.setText("");
                            switch ((int) shareFunction.byteArray2Float(fbData)) {
                                case define.RED:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_red);
                                    textColor.setText("Màu đỏ");
                                    break;
                                case define.GREEN:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_green);
                                    textColor.setText("Màu xanh lá");
                                    break;
                                case define.BLUE:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_blue);
                                    textColor.setText("Màu xanh lam");
                                    break;
                                case define.YELLOW:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_yellow);
                                    textColor.setText("Màu vàng");
                                    break;
                                case define.WHITE:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_white);
                                    textColor.setText("Màu trắng");
                                    break;
                                case define.BLACK:
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_black);
                                    textColor.setText("Màu đen");
                                    break;
                                default: {
                                    btnGetColor.setBackgroundResource(R.drawable.ic_read_color_select);
                                    textColor.setText("Màu ?");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case define.MODE_BTN:{
                        if (stateGetButton) {
//                            textSrf05.setText("");
//                            textColor.setText(displayText);
//                            textLight.setText("");
                            switch ((int) shareFunction.byteArray2Float(fbData)) {
                                case define.MODE_1:
                                    showMode1.setBackgroundResource(R.drawable.ic_mode2);
                                    showMode2.setBackgroundResource(R.drawable.ic_mode1);
                                    showMode3.setBackgroundResource(R.drawable.ic_mode1);
                                    break;
                                case define.MODE_2:
                                    showMode1.setBackgroundResource(R.drawable.ic_mode2);
                                    showMode2.setBackgroundResource(R.drawable.ic_mode2);
                                    showMode3.setBackgroundResource(R.drawable.ic_mode1);
                                    break;
                                case define.MODE_3:
                                    showMode1.setBackgroundResource(R.drawable.ic_mode2);
                                    showMode2.setBackgroundResource(R.drawable.ic_mode2);
                                    showMode3.setBackgroundResource(R.drawable.ic_mode2);
                                    break;
                                default:
                                    showMode1.setBackgroundResource(R.drawable.ic_mode1);
                                    showMode2.setBackgroundResource(R.drawable.ic_mode1);
                                    showMode3.setBackgroundResource(R.drawable.ic_mode1);
                                    break;
                            }
                        }
                        break;
                    }
                    case define.SOUND: {
                        if (stateGetSound) {
//                            textSrf05.setText("");
//                            textColor.setText("");
//                            textLight.setText("");

                            if (shareFunction.byteArray2Float(fbData) >= 1) {
                                soundSignal.setBackgroundResource(R.drawable.have_sound);
                            } else {
                                soundSignal.setBackgroundResource(R.drawable.ic_sound);
                            }
                        }
                        break;
                    }
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