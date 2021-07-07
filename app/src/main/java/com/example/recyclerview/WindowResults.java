package com.example.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class WindowResults extends AppCompatActivity {

    private TextView name;
    private TextView age;
    private TextView item;
    private Button delete;
    private Button return_home;

    private String string_name;
    private int int_age, int_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_results);

        // Refenrencia com os campos do XML
        return_home = findViewById(R.id.btn_home);
        delete =  findViewById(R.id.btn_delete);
        name = findViewById(R.id.txt_name);
        age = findViewById(R.id.txt_age);
        item = findViewById(R.id.txt_item);

        // Obtem os valores passados
        Intent intent = getIntent();
        string_name = intent.getStringExtra("name");
        int_age = intent.getIntExtra("age",0);
        int_position = intent.getIntExtra("position",0);

        listenersButtons();
        formattedTexts();
    }

    private void formattedTexts() {
        // Substitui os Caracteres pelas Variaveis
        name.setText(getString(R.string.label_nameFormatted, String.format("%s", string_name)));
        age.setText(getString(R.string.label_ageFormatted, int_age));
        item.setText(getString(R.string.label_itemFormatted, int_position));
    }

    public void listenersButtons() {
        return_home.setOnClickListener(v -> finish());

        // TODO IMPLEMENTAR METODO DELETE
        // delete.setOnClickListener(v -> deletePeople(int_position));
    }

}