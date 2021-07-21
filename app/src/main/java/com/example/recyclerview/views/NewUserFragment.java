package com.example.recyclerview.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.DataBaseHelper;
import com.example.recyclerview.model.People;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewUserFragment extends Fragment {

    private DataBaseHelper database;

    // Contrutor da Classe
    public NewUserFragment() {}

    // Cria uma nova Instancia do Fragment
    public static NewUserFragment newInstance() {
        return new NewUserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Incia o Layout do Fragment
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);

        database = new DataBaseHelper(view.getContext());

        TextInputEditText input_name = view.findViewById(R.id.edit_name);
        TextInputEditText input_age = view.findViewById(R.id.edit_age);

        Button register = view.findViewById(R.id.btn_register);

        // Clique no Botão "Cadastrar"
        register.setOnClickListener(v -> {
            int nextId, age;
            String name;

            // Valida se o Banco de Dados é null
            if (database != null){
                nextId = database.nextId();

                // Valida se os Campos estão Preenchidos
                if (input_name.getText().toString().equals("")){
                    input_name.setError(getString(R.string.error_input, "Nome"));

                } else if(input_age.getText().toString().equals("")) {
                    input_age.setError(getString(R.string.error_input, "Idade"));

                } else {

                    // Obtem os valores Informados e Valida se a Idade está no Intervalo Int
                    name = input_name.getText().toString();
                    try {
                        age = Integer.parseInt(input_age.getText().toString());

                    } catch (Exception ex){
                        Log.e("CONVERT INT", "Erro na conversão da String para " +
                                "Integer. Error:\n" + ex);

                        Toast.makeText(view.getContext(), R.string.error_convertInt,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Valida a Quantidade de Caraceteres do Nome e Idade Minima e Maxima
                    if (name.length() > 50){
                        input_name.setError(getString(R.string.error_lenght, "Nome", 50,
                                "Caracteres"));
                    } else if (age < 0 || age > 120) {
                        input_age.setError(getString(R.string.error_lenght, "Idade", 120,
                                "Anos"));
                    } else {
                        // Intanciando a Classe e Insere no Banco de Dados
                        People people = new People(nextId, name, age);
                        database.insertPeople(people);
                        database.close();

                        Toast.makeText(view.getContext(), R.string.register_complete,
                                Toast.LENGTH_SHORT).show();

                        input_name.setText("");
                        input_age.setText("");
                    }
                }

            } else{
                // Caso o Banco de Dados seja Nulo
                Toast.makeText(view.getContext(), R.string.error_database,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}