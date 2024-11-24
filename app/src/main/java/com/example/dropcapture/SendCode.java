package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.BTConnection;
import static com.example.dropcapture.MainActivity.brightness;

import android.util.Log;

public class SendCode {
    public static void brightness() {
        Log.e("BT", "Свет на " + brightness);

        if (BTConnection != null) {
            byte[] bytesToSend = (brightness+"").getBytes();
            BTConnection.write(bytesToSend);
        }
    }

    public static void setPhoto() {
        Log.e("BT", "Сделано фото");

        if (BTConnection != null) {
            byte[] bytesToSend = "a".getBytes();
            BTConnection.write(bytesToSend);
        }
    }

    public static void detectorOn() {
        Log.e("BT", "Детектор включен");

        if (BTConnection != null) {
            byte[] bytesToSend = "b".getBytes();
            BTConnection.write(bytesToSend);
        }
    }

    public static void detectorOff() {
        Log.e("BT", "Детектора выключен");

        if (BTConnection != null) {
            byte[] bytesToSend = "c".getBytes();
            BTConnection.write(bytesToSend);
        }
    }

    public static void hello() {
        Log.e("BT", "Установлено соединение");

        if (BTConnection != null) {
            byte[] bytesToSend = "z".getBytes();
            BTConnection.write(bytesToSend);
        }
    }
}
