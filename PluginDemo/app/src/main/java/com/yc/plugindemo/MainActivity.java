package com.yc.plugindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hhhhh", "----------"+R.layout.sssss);
        setContentView(R.layout.sssss);

        Log.i("hhhhh", "----------"+ ((android.widget.TextView)this.findViewById(R.id.tv_hh)).getText());
    }
}
