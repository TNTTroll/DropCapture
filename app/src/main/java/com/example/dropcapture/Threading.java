package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.BLUETOOTH_CONNECT_PERMISSION_CODE;
import static com.example.dropcapture.MainActivity.BTConnection;
import static com.example.dropcapture.MainActivity.checkPermission;
import static com.example.dropcapture.MainActivity.getPhoto;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataInputStream;
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
                Log.e("SEND", "Done");
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException ignored) {}
        }
    }

    public static class ThreadConnected extends Thread {
        private InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
        private BluetoothSocket socket = null;

        public ThreadConnected(BluetoothSocket _socket) {
            socket = _socket;

            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException ignored) {}

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            if (socket != null) {
                byte[] buffer = new byte[5];
                int bytes;

                StringBuilder fullMsg = new StringBuilder();

                while (true) {
                    Log.e("WHILE", "RUNNING");

                    try {
                        connectedInputStream = socket.getInputStream();

                        DataInputStream mmInStream = new DataInputStream(connectedInputStream);
                        bytes = mmInStream.read(buffer);
                        String readMessage = new String(buffer, 0, bytes, StandardCharsets.UTF_8);
                        char[] chars = readMessage.toCharArray();

                        Log.e("CHARS", readMessage);

                        for (char c : chars) {
                            fullMsg.append(c);

                            Log.e("DATA", fullMsg.toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException ignored) {}
        }
    }
}
