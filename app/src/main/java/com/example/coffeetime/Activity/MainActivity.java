package com.example.coffeetime.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.coffeetime.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int a = 5;
        System.out.println("               "+a);
    }
}