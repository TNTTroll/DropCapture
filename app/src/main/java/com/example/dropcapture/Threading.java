package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.BLUETOOTH_CONNECT_PERMISSION_CODE;
import static com.example.dropcapture.MainActivity.BTConnection;
import static com.example.dropcapture.MainActivity.checkPermission;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Threading {
    public static UUID thisUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static class ThreadConnectBT extends Thread {
        private BluetoothSocket bluetoothSocket = null;

        public ThreadConnectBT(BluetoothDevice device) {
            try {
                checkPermission(android.Manifest.permission.BLUETOOTH_CONNECT, BLUETOOTH_CONNECT_PERMISSION_CODE);
                bluetoothSocket = device.createRfcommSocketToServiceRecord(thisUUID);
            } catch (IOException ignored) {}
        }

        @Override
        public void run() {
            boolean success = false;

            try {
                checkPermission(android.Manifest.permission.BLUETOOTH_CONNECT, BLUETOOTH_CONNECT_PERMISSION_CODE);
                bluetoothSocket.connect();
                success = true;
            }
            catch (IOException ignored0) {
                try {
                    bluetoothSocket.close();
                } catch (IOException ignored1) {}
            }

            if (success) {
                BTConnection = new ThreadConnected(bluetoothSocket);
                BTConnection.start();

                SendCode.hello();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException ignored) {}
        }
    }

    public static class ThreadConnected extends Thread {
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket _socket) {
            BluetoothSocket socket = _socket;

            OutputStream out = null;

            try {
                out = socket.getOutputStream();
            } catch (IOException ignored) {}

            connectedOutputStream = out;
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException ignored) {}
        }
    }
}
