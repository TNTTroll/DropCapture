package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.BLUETOOTH_CONNECT_PERMISSION_CODE;
import static com.example.dropcapture.MainActivity.checkPermission;
import static com.example.dropcapture.MainActivity.toast;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Plate extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static BluetoothDevice innerInfo;

    private String mParam1;
    private String mParam2;

    View view;
    BluetoothDevice info;

    public Plate(BluetoothDevice _info) {
        info = _info;
    }

    public static Plate newInstance(String param1, String param2) {
        Plate fragment = new Plate(innerInfo);
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

    public void onClick(View v) {
        checkPermission(android.Manifest.permission.BLUETOOTH_CONNECT, BLUETOOTH_CONNECT_PERMISSION_CODE);

        toast("Подключено к  " + info.getName());

        if (MainActivity.BT != null)
            MainActivity.BT.cancel();
        if (MainActivity.BTConnection != null)
            MainActivity.BTConnection.interrupt();

        MainActivity.BT = new Threading.ThreadConnectBT(info);
        MainActivity.BT.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plate, container, false);

        TextView name = view.findViewById(R.id.plateName);
        name.setText(info.getName());

        Button btn = view.findViewById(R.id.plateButton);
        btn.setOnClickListener(this);

        Button bg = view.findViewById(R.id.plateBG);
        bg.setEnabled(false);

        return view;
    }
}