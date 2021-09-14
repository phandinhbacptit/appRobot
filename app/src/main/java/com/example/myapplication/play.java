package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class play extends AppCompatActivity {
    ImageView selectControl, selectMusic, selectVoiceControl;
    ImageButton btn_previous;
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