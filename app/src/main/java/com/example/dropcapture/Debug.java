package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.BTConnection;
import static com.example.dropcapture.MainActivity.detectorToggle;
import static com.example.dropcapture.MainActivity.getResId;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Debug extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;

    public Debug() {
    }

    public static Debug newInstance(String param1, String param2) {
        Debug fragment = new Debug();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_debug, container, false);

        // Debug 'Photo' Button
        Button dPhoto = view.findViewById(R.id.debugPhoto);
        dPhoto.setOnClickListener(v -> SendCode.setPhoto());

        // Debug 'Detector' Button
        Button dDetector = view.findViewById(R.id.debugDetector);
        if (detectorToggle)
            dDetector.setText("Выключить детектор");
        else
            dDetector.setText("Включить детектор");

        dDetector.setOnClickListener(v -> {
            detectorToggle = !detectorToggle;

            if (detectorToggle) {
                SendCode.detectorOn();
                dDetector.setText("Выключить детектор");
            } else {
                SendCode.detectorOff();
                dDetector.setText("Включить детектор");
            }
        });

        // IMAGES
        ImageView plate = view.findViewById(R.id.debugConnection);
        if (BTConnection != null)
            plate.setImageResource(getResId("connect", R.drawable.class));

        return view;
    }
}