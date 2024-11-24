package com.example.dropcapture;

import static com.example.dropcapture.MainActivity.getResId;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Display extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    ImageView mask;

    public Display() {
    }

    public static Display newInstance(String param1, String param2) {
        Display fragment = new Display();
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

        view = inflater.inflate(R.layout.fragment_display, container, false);

        mask = view.findViewById(R.id.displayMask);

        Button btn = view.findViewById(R.id.displayBtn);
        btn.setOnClickListener(v -> SendCode.setPhoto());

        if (OpenCVLoader.initDebug())
            makeMask("connect");

        return view;
    }

    public void makeMask(String photo) {
        BitmapDrawable picture = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), getResId(photo, R.drawable.class), null);
        Bitmap bitmap = picture.getBitmap();

        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(mat, bitmap);

        mask.setImageBitmap(bitmap);
    }
}