package com.example.myapplication;

import java.util.Vector;

public class define {

    /*Define sensor feedback data*/
    public static final int NONE = 0;
    public static final int SRF05 = 1;
    public static final int LINE = 2;
    public static final int LIGHT = 3;
    public static final int COLOR = 4;
    public static final int JOYSTICK = 5;
    public static final int MODE_BTN = 6;
    public static final int LED_MATRIX = 7;
    public static final int LED_RGB = 8;
    public static final int BUZZER = 9;
    public static final int SOUND = 10;
    public static final int RING_LED = 11;


    /*Define RGB LED color*/
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;
    public static final int YELLOW = 4;
    public static final int PURPLE = 5;
    public static final int PINK = 6;
    public static final int WHITE = 7;
    public static final int BLACK = 8;


    /*Define Code RGB LED color*/
    public static byte RED_COLOR[] = {(byte)0xff, (byte)0x00, (byte)0x00};
    public static byte GREEN_COLOR[] = {(byte)0x33, (byte)0xff, (byte)0x33};
    public static byte BLUE_COLOR[] = {(byte)0x33,(byte)0x33,(byte)0xff};
    public static byte YELLOW_COLOR[] = {(byte)0xFF,(byte)0xFF,(byte)0x00};
    public static byte PURPLE_COLOR[] = {(byte)0x69,(byte)0x3b,(byte)0xb3};
    public static byte PINK_COLOR[] = {(byte)0xff,(byte)0x46,(byte)0xA0};
    public static byte BLACK_COLOR[] = {(byte)0x00,(byte)0x00,(byte)0x00};
    public static byte WHITE_COLOR[] = {(byte)0xff,(byte)0xff,(byte)0xff};

    public static final int GET = 1;
    public static final int RUN = 2;
    public static final int RESET = 4;
    public static final int START = 5;

    /*Define line sensor*/
    public static final int ALL_ON = 0;
    public static final int LEFT_ON = 1;
    public static final int RIGHT_ON = 2;

    /*Define state mode button*/
    public static final int MODE_OFF = 0;
    public static final int MODE_1 = 1;
    public static final int MODE_2 = 2;
    public static final int MODE_3 = 3;


    /*Define command send to ESP32 to get value of sensor module*/
    public static byte[] cmd_get_valModule = {(byte)0xff, 0x55, 0x04, 0x00, GET, 0x00, 0,0,0,0,};

    /*Define led matrix effect*/
    public static byte motion_effect [][] = {
            {0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00},
            {0x00, 0x00, 0x3C, 0x3C, 0x3C, 0x3C, 0x00, 0x00},
            {0x00, 0x7E, 0x7E, 0x7E, 0x7E, 0x7E, 0x7E, 0x00},
            {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF},
            {0x18,0x3C,0x66,0x66,0x7E,0x66,0x66,0x66},//A
            {0x78,0x64,0x68,0x78,0x64,0x66,0x66,0x7C},//B
            {0x3C,0x62,0x60,0x60,0x60,0x62,0x62,0x3C},//C
            {0x78,0x64,0x66,0x66,0x66,0x66,0x64,0x78},//D
            {0x7E,0x60,0x60,0x7C,0x60,0x60,0x60,0x7E},//E
            {0x18,0x3c,0x7e,(byte)0xff,0x18,0x18,0x18,0x18}, // Mui ten di len
            {0x18,0x18,0x18,0x18,(byte)0xff,0x7e,0x3c,0x18}, // Mui ten di xuong
            {0x08,0x0c,0x0e,(byte)0xff,(byte)0xff,0x0e,0x0c,0x08}, // Mui ten sang phai
    };
    public static int NUM_RING_EFFECT = 1;
    /*Define Ring led effect*/
    public static long ring_led_effect [][] = {
            {(long)0x000001, (long)0x000000 , (long)0x000000 , (long)0x000000, (long)0x000000, (long)0x000000, (long)0x000000 , (long)0x000000, (long)0x000000, (long)0x000000, (long)0x000000, (long)0x000000},
    };
    /*Define song for buzzer*/
    public static int [] hpbdSong= {525, 525, 587, 525, 698, 659, 525,525, 587, 525,784, 698, 525, 525, 987, 880, 698, 659, 587, 932, 932, 880, 698, 784, 698};
    /* header1(0) header2(1) length(2) id(3) action(4) device(5)
    * action: GET: 1, RUN: 2, RESET: 4, START: 5
    * device: SR05: 1, LINE: 2, LIGHT: 3, COLOR: 4, JOYSTIC: 5, BTN_MODE: 6, LEDMATRIX: 7, RGB: 8, BUZZER: 9
    *
    * LED_RGB: header1(0) header2(1) length(2) id(3) action(4) device(5) port(6) slot(7) idx(8) r(9) g(10) b(11)
    * JOY_STICK: header1(0) header2(1) length(2) id(3) action(4) device(5) left_speed(6<<7) right_speed(8<<9)
    * LED_MATRIX: header1(0) header2(1) length(2) id(3) action(4) device(5) dataMatrix(6->13) duration(14)
    * BUZZER: header1(0) header2(1) length(2) id(3) action(4) device(5) port(6) slot(7) freq(8<<9) duration(10<<11)*/
    public static byte[] cmdRunModule = {(byte)0xff, 0x55, 0, 0, RUN, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                                            , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public  static final int MAX_STEP_RANZER = 24;

}
