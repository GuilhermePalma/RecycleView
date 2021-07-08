package com.example.recyclerview.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button next_page = findViewById(R.id.btn_start);

        next_page.setOnClickListener(v -> {
            startActivity(
                    new Intent(this, RecyclerActivity.class));
            finish();
        });
    }
}