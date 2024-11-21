package com.example.dropcapture;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    //Variables
    public static Context context;

    Button connect, display;
    public static View screen;

    public static int brightness = 0;
    public static boolean detectorToggle = true;

    // Slide menu
    static final int MIN_DISTANCE = 400;
    private boolean userMode = true;
    private float x1;

    //Bluetooth
    public static BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 0;
    public static final int BLUETOOTH_CONNECT_PERMISSION_CODE = 100;
    public static final int BLUETOOTH_SCAN_PERMISSION_CODE = 101;

    public static Threading.ThreadConnectBT BT = null;
    public static Threading.ThreadConnected BTConnection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screen = (View) findViewById(R.id.menuScreen);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        connect = (Button) findViewById(R.id.menuConnect);
        connect.setOnClickListener(v ->
                getSupportFragmentManager().beginTransaction().replace(screen.getId(), new Connect()).commit());

        display = (Button) findViewById(R.id.menuDisplay);
        display.setOnClickListener(v ->
                getSupportFragmentManager().beginTransaction().replace(screen.getId(), new Display()).commit());

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) screen.getLayoutParams();
        layoutParams.height = (int)(displayMetrics.heightPixels*.75);
        screen.setLayoutParams(layoutParams);
    }


    // Slide menu
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;

                if ( Math.abs(deltaX) > MIN_DISTANCE )
                    if (x1 > x2 && !userMode) {
                        getSupportFragmentManager().beginTransaction().replace(screen.getId(), new Connect()).commit();
                        toast("Режим: Пользователь");
                        userMode = true;
                    } else if (x1 < x2 && userMode) {
                        getSupportFragmentManager().beginTransaction().replace(screen.getId(), new Debug()).commit();
                        toast("Режим: Разработчик");
                        userMode = false;
                    }
                break;
        }

        return super.onTouchEvent(event);
    }


    // Bluetooth functions
    public static void getPhoto(int[] string) {
        Log.e("PHOTO", string[0] + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                toast("Bluetooth включен");
            } else
                toast("Не удалось включить Bluetooth");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.context, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) MainActivity.context, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // Helpers
    public static void toast(String text) {
        Toast.makeText(MainActivity.context, text, Toast.LENGTH_SHORT).show();
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception ignored) {
            return -1;
        }
    }
}