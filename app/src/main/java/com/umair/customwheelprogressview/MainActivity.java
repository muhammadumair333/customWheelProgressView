package com.umair.customwheelprogressview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.umair.customwheelprogresslib.ProgressView;

public class MainActivity extends AppCompatActivity {

    private ProgressView velocimeter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        velocimeter2 = findViewById(R.id.velocimeter2);
        velocimeter2.setValue(45f);
    }
}