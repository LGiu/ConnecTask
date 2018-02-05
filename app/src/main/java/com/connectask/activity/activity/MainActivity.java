package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.connectask.R;

public class MainActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(this, 3000);

    }

    @Override
    public void run() {
        Intent it = new Intent(MainActivity.this, Login.class);
        startActivity(it);
    }
}
