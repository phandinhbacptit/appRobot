package com.example.myapplication;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.classicBluetooth.LocalBinder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class shareFunction extends AppCompatActivity {

    private static classicBluetooth blue;
    boolean stateBond = false;
    private static shareFunction mShareFunction;

    public static shareFunction getInstance ()
    {
        if (mShareFunction == null)
            mShareFunction = new shareFunction();
        return mShareFunction;
    }

    public static float byteArray2Float(byte[] bytes)  {
        int intBits = (((byte)bytes[3] & 0xFF) << 24) |
                (((byte)bytes[2] & 0xFF) << 16) |
                (((byte)bytes[1] & 0xFF) << 8) |
                ((byte)bytes[0] & 0xFF);
        return Float.intBitsToFloat(intBits);
    }

    public static void runBuzzer(int id, int port, int slot, int freq, int duration)
    {
        define.cmdRunModule[2] =  0x09;
        define.cmdRunModule[3] = (byte)id;
        define.cmdRunModule[5] = define.BUZZER;
        define.cmdRunModule[6] = (byte)port;
        define.cmdRunModule[7] = (byte)slot;
        define.cmdRunModule[8] = (byte)(freq  & (byte)0xff);
        define.cmdRunModule[9] = (byte)((freq  >> 8) & (byte)0xff);
        define.cmdRunModule[10] = (byte)(duration  & (byte)0xff);
        define.cmdRunModule[11] = (byte)((duration  >> 8) & (byte)0xff);
    }

    public static void runRGB(int id, int port, int slot, byte[] color)
    {
        define.cmdRunModule[2] =  0x09;
        define.cmdRunModule[3] = (byte)id;
        define.cmdRunModule[5] = define.LED_RGB;
        define.cmdRunModule[6] = (byte)port;
        define.cmdRunModule[7] = (byte)slot;
        define.cmdRunModule[8] = (byte)id;
        System.arraycopy(color, 0, define.cmdRunModule, 9, color.length);
    }

    public static void runMaTrix(int id, int port, int slot, byte[] effect, int duration)
    {
        define.cmdRunModule[2] =  0x0e;
        define.cmdRunModule[3] = (byte)id;
        define.cmdRunModule[5] = define.LED_MATRIX;
        define.cmdRunModule[6] = (byte)port;
        define.cmdRunModule[7] = (byte)slot;
        System.arraycopy(effect, 0, define.cmdRunModule, 8, effect.length);
        define.cmdRunModule[8 + effect.length] = (byte)duration;
    }

    public static void runJoystick(int id, int port, int slot, int leftSpeed, int  rightSpeed)
    {
        define.cmdRunModule[2] =  0x09;
        define.cmdRunModule[3] = (byte)id;
        define.cmdRunModule[5] = define.JOYSTICK;
        define.cmdRunModule[6] = (byte)port;
        define.cmdRunModule[7] = (byte)slot;
        define.cmdRunModule[8] = (byte)(leftSpeed  & (byte)0xff);
        define.cmdRunModule[9] = (byte)((leftSpeed  >> 8) & (byte)0xff);
        define.cmdRunModule[10] = (byte)(rightSpeed  & (byte)0xff);
        define.cmdRunModule[11] = (byte)((rightSpeed  >> 8) & (byte)0xff);
    }
}
