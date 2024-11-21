package com.example.dropcapture;

import static android.app.Activity.RESULT_OK;
import static com.example.dropcapture.MainActivity.BLUETOOTH_CONNECT_PERMISSION_CODE;
import static com.example.dropcapture.MainActivity.BLUETOOTH_SCAN_PERMISSION_CODE;
import static com.example.dropcapture.MainActivity.REQUEST_ENABLE_BT;
import static com.example.dropcapture.MainActivity.BTConnection;
import static com.example.dropcapture.MainActivity.brightness;
import static com.example.dropcapture.MainActivity.checkPermission;
import static com.example.dropcapture.MainActivity.getResId;
import static com.example.dropcapture.MainActivity.mBluetoothAdapter;
import static com.example.dropcapture.MainActivity.screen;
import static com.example.dropcapture.MainActivity.toast;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Connect extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    Button on, search, plus, minus;
    TextView brightnessText;
    ImageView plate;

    public Connect() {
    }

    public static Connect newInstance(String param1, String param2) {
        Connect fragment = new Connect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_connect, container, false);

        checkPermission(android.Manifest.permission.BLUETOOTH_CONNECT, BLUETOOTH_CONNECT_PERMISSION_CODE);
        checkPermission(android.Manifest.permission.BLUETOOTH_SCAN, BLUETOOTH_SCAN_PERMISSION_CODE);

        brightnessText = view.findViewById(R.id.connectBrightnessText);
        brightnessText.setText(brightness + "/5");

        // ON Button
        on = view.findViewById(R.id.connectOn);
        on.setOnClickListener(v -> {
            if (!mBluetoothAdapter.isEnabled()) {
                toast("Включение BlueTooth...");

                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            } else
                toast("Bluetooth уже включен");
        });

        // SEARCH Button
        search = view.findViewById(R.id.connectSearch);
        search.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction().replace(screen.getId(), new Search()).commit());

        // Increase Button
        plus = view.findViewById(R.id.connectBrightnessPlus);
        plus.setOnClickListener(v -> {
            brightness = Math.min(brightness+1, 5);
            brightnessText.setText(brightness + "/5");

            SendCode.brightness();
        });

        // Decrease Button
        minus = view.findViewById(R.id.connectBrightnessMinus);
        minus.setOnClickListener(v -> {
            brightness = Math.max(brightness-1, 0);
            brightnessText.setText(brightness + "/5");

            SendCode.brightness();
        });

        // IMAGES
        plate = view.findViewById(R.id.connectIcon);
        if (BTConnection != null)
            plate.setImageResource(getResId("connect", R.drawable.class));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                toast("Bluetooth включен");
            } else
                toast("Не удалось включить Bluetooth");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}