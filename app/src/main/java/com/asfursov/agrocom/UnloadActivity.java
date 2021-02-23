package com.asfursov.agrocom;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UnloadActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unload);

        mTextView = (TextView) findViewById(R.id.text);

    }
}