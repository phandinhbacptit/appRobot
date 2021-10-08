package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class voiceControl extends AppCompatActivity {
    pl.droidsonroids.gif.GifImageView getVoice;
    ImageButton backVoiceCtrBtn, vcBlueConnection;
    TextView textSpeech, textHeader, test, textWait;
    ListView commandList;
    Timer timer;
    boolean stateGetCommand = false;
    ArrayAdapter<String> arrayAdapter;
    String listDisplay[] = {"turn left/quay trái","turn right/quay phải", "forward/tiến", "backward/lùi", "stop/dừng lại",
            "dance/múa", "red/sáng đỏ", "green/sáng xanh lá", "blue/sáng xanh lam", "yellow/sáng vàng", "pink/sáng hồng",
            "off/tắt", "mix color", "sound mode", "ring", "ring off" };
    String command[] = {"turn left", "quay trái","turn right", "quay phải", "forward", "Tiến", "backward", "lùi", "stop","dừng lại",
            "dance", "Red", "sáng đỏ", "green", "sáng xanh lá", "blue", "sáng xanh lam",
            "yellow", "sáng vàng", "pink", "sáng hồng", "off", "tắt", "mix color", "sound mode", "ring", "ring off"};

    classicBluetooth blueVoiceControl;
    boolean stateBond = false;
    boolean state = false;

    void run_motor (int leftSpeed, int rightSpeed) {
        shareFunction.runJoystick(0,0,0, leftSpeed,rightSpeed);
        if (blueVoiceControl.getInstance() != null) {
            blueVoiceControl.getInstance().write(define.cmdRunModule);
        }
    }

    void run_rgb(byte[] color)
    {
        shareFunction.runRGB(0, 0, 0, color);
        if (blueVoiceControl.getInstance() != null) {
            blueVoiceControl.getInstance().write(define.cmdRunModule);
        }
    }
    void run_ring_led()
    {
        shareFunction.runRingLed(0,0,0, define.ring_led_effect[0]);
        if (blueVoiceControl.getInstance() != null) {
            blueVoiceControl.getInstance().write(define.cmdRunModule);
        }
    }
    void resetText ()
    {
        commandList.setAdapter(null);
        textHeader.setText("");
        textSpeech.setText("");
        textWait.setText("");
    }
    void díplayGuidle()
    {
        if (!stateGetCommand) {
            resetText();
            commandList.setAdapter(arrayAdapter);
            textHeader.setText("Ranzer không nghe rõ, thử nói!");
        }
    }
    /*********************************Handle bluetooth connection******************************/
    ServiceConnection musicConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            classicBluetooth.LocalBinder binder = (classicBluetooth.LocalBinder) service;
            blueVoiceControl = binder.getService();
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
                    if (blueVoiceControl.get_state_blue_connect()) {
                        vcBlueConnection.setBackgroundResource(R.drawable.ic_ble_on);
                        timer.cancel();
                    } else {
                        vcBlueConnection.setBackgroundResource(R.drawable.ic_ble_off);
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
        setContentView(R.layout.activity_voice_control);
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
//        voiceSignal = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.btnShowAffectVoice);
        getVoice = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.btnGetVoice);
        vcBlueConnection = (ImageButton)findViewById(R.id.btnConnVoiceBluetooth);
        backVoiceCtrBtn = (ImageButton)findViewById(R.id.btnVCtrBack);
        textSpeech = (TextView)findViewById(R.id.textSpeech);
        textHeader = (TextView)findViewById(R.id.headerText);
        textWait = (TextView)findViewById(R.id.textWait);
        commandList = (ListView)findViewById(R.id.listCommand);
        test = (TextView)findViewById(R.id.textTest);

        Intent intent = new Intent(this, classicBluetooth.class);
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);
        check_connected();
        /******************************************************************************************/
        SpeechRecognizer speechRecognizer;
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_command, R.id.textView, listDisplay);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO},1);
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speedRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        backVoiceCtrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(voiceControl.this, play.class));
            }
        });
        vcBlueConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_connected();
                startActivity(new Intent(voiceControl.this, connectingBluetooth.class));
            }
        });

        getVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetText();
                stateGetCommand = false;
                getVoice.setBackgroundResource(R.drawable.mic_run);
                textWait.setText("Xin mời nói...");
                speechRecognizer.startListening(speedRecognizerIntent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textWait.setText(" ");
                        getVoice.setBackgroundResource(R.drawable.mic);
                        speechRecognizer.stopListening();
                        díplayGuidle();
                    }
                }, 2500);
            }
        });
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {


                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                test.setText(data.get(0));
                for (int i = 0; i < command.length; i++) {
                    if (data.get(0).equals(command[i])) {
                        resetText();
                        textHeader.setText("Ranzer thực hiện lệnh");
                        textSpeech.setText(data.get(0));
                        stateGetCommand = true;
                        if (data.get(0).equals("turn left") || data.get(0).equals("quay trái"))
                            run_motor(150,150);
                        if (data.get(0).equals("turn right") || data.get(0).equals("quay phải"))
                            run_motor(-150,-150);
                        if (data.get(0).equals("forward") || data.get(0).equals("Tiến"))
                            run_motor(150,-150);
                        if (data.get(0).equals("backward") || data.get(0).equals("lùi"))
                            run_motor(-150,150);
                        if (data.get(0).equals("stop") || data.get(0).equals("dừng lại"))
                            run_motor(0,0);
                        if (data.get(0).equals("red") || data.get(0).equals("sáng đỏ"))
                            run_rgb(define.RED_COLOR);
                        if (data.get(0).equals("green") || data.get(0).equals("sáng xanh lá"))
                            run_rgb(define.GREEN_COLOR);
                        if (data.get(0).equals("blue") || data.get(0).equals("sáng xanh lam"))
                            run_rgb(define.BLUE_COLOR);
                        if (data.get(0).equals("yellow") || data.get(0).equals("sáng vàng"))
                            run_rgb(define.YELLOW_COLOR);
                        if (data.get(0).equals("pink") || data.get(0).equals("sáng hồng"))
                            run_rgb(define.PINK_COLOR);
                        if (data.get(0).equals("off") || data.get(0).equals("tắt"))
                            run_rgb(define.BLACK_COLOR);
                        if (data.get(0).equals("ring"))
                            run_ring_led();
                    }
                }
                díplayGuidle();

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }
}