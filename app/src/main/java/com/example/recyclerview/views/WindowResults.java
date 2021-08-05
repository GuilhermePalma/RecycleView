package com.example.recyclerview.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.DataBaseHelper;


public class WindowResults extends AppCompatActivity {

    private TextView id;
    private TextView name;
    private TextView age;
    private TextView item;
    private Button delete;
    private Button return_home;

    private String string_name;
    private int int_age, int_position, int_id;
    private boolean isActiveGrid;

    DataBaseHelper database = new DataBaseHelper(this);

    private final String IS_GRID_LIST = "is_grid_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_results);

        // Refenrencia com os campos do XML
        return_home = findViewById(R.id.btn_home);
        delete =  findViewById(R.id.btn_deleteItem);
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        age = findViewById(R.id.txt_age);
        item = findViewById(R.id.txt_item);

        // Obtem os valores passados pela Intent
        Intent intent = getIntent();

        if (intent != null){
            int_id = intent.getIntExtra("id",0);
            string_name = intent.getStringExtra("name");
            int_age = intent.getIntExtra("age",0);
            int_position = intent.getIntExtra("position",0);
            isActiveGrid = intent.getBooleanExtra(IS_GRID_LIST, false);
        } else {
            // Não obteve nenhum dado da Intent ---> Volta à Home
            isActiveGrid = false;
            intentHome();
        }

        listenersButtons();
        formattedTexts();
    }

    @Override
    public void onBackPressed()
    {
        intentHome();
    }

    // Formata os Textos
    private void formattedTexts() {
        // Substitui os Caracteres pelas Variaveis
        id.setText(getString(R.string.label_idFormatted, int_id));
        name.setText(getString(R.string.label_nameFormatted, String.format("%s", string_name)));
        age.setText(getString(R.string.label_ageFormatted, int_age));
        item.setText(getString(R.string.label_itemFormatted, int_position));
    }

    // Listeners dos Botões
    public void listenersButtons() {
        return_home.setOnClickListener(v ->
            // Desativa a Transição e Inicia a Atividade
            intentHome()
        );

         delete.setOnClickListener(v -> {
             database.deletePeople(int_id);
             database.close();
             intentHome();
         });
    }

    // Reinicia a Activity Recycler e Desativa a Animação de Abertura
    public void intentHome(){
        Intent home = new Intent(this, MainActivity.class);
        home.putExtra(IS_GRID_LIST, isActiveGrid);
        overridePendingTransition(0, 0);
        startActivity(home);
        finish();
    }

}