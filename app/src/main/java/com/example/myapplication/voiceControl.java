package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class voiceControl extends AppCompatActivity {
    pl.droidsonroids.gif.GifImageView voiceSignal;
    ImageButton getVoice, backVoiceCtrBtn;
    TextView textSpeech, textHeader;
    ListView commandList;
    Timer timer;
    boolean stateGetCommand = false;
    ArrayAdapter<String> arrayAdapter;
    String command[] = {"turn left", "turn right", "forward", "backward", "stop", "dance", "red", "green", "blue", "mix color", "sound mode"};

    void resetText ()
    {
        commandList.setAdapter(null);
        textHeader.setText("");
        textSpeech.setText("");
    }
    void díplayGuidle()
    {
        if (!stateGetCommand) {
            resetText();
            commandList.setAdapter(arrayAdapter);
            textHeader.setText("Ranzer không nghe rõ, thử nói!");
        }
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
        voiceSignal = (pl.droidsonroids.gif.GifImageView)findViewById(R.id.btnShowAffectVoice);
        getVoice = (ImageButton)findViewById(R.id.btnGetVoice);
        backVoiceCtrBtn = (ImageButton)findViewById(R.id.btnVCtrBack);
        textSpeech = (TextView)findViewById(R.id.textSpeech);
        textHeader = (TextView)findViewById(R.id.headerText);
        commandList = (ListView)findViewById(R.id.listCommand);
        SpeechRecognizer speechRecognizer;
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_command, R.id.textView, command);



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

        getVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetText();
                stateGetCommand = false;
                getVoice.setBackgroundResource(R.drawable.mic_run);
                voiceSignal.setBackgroundResource(R.drawable.have_sound);
                speechRecognizer.startListening(speedRecognizerIntent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        voiceSignal.setBackgroundResource(R.drawable.ic_sound);
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
                for (int i = 0; i < command.length; i++) {
                    if (data.get(0).equals(command[i])) {
                        resetText();
                        textSpeech.setText(data.get(0));
                        stateGetCommand = true;
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
}