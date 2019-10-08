package com.example.e_shop_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button joinNowbtn,mainLoginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLoginbtn = findViewById(R.id.mainLoginbtn);
        joinNowbtn = findViewById(R.id.joinNowbtn);

    }
}
