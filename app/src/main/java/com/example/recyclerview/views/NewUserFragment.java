package com.example.recyclerview.views;

import android.os.Bundle;
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
            int nextId = 0;
            String name = "", age = "";

            // Valida se o Banco de Dados é null
            if (database != null){
                nextId = database.nextId();

                // Recupera os Dados se não forem Nulos
                name = Objects.requireNonNull(input_name.getText()).toString();
                age = Objects.requireNonNull(input_age.getText()).toString();

            } else{
                // Caso o Banco de Dados seja Nulo
                Toast.makeText(view.getContext(), R.string.error_database,
                        Toast.LENGTH_SHORT).show();
            }

            // TODO IMPLEMENTAR REGEX P/ VALIDAR OS CAMPOS
            // Tratamento dos Campos
            if (name.equals("")){
                input_name.setError(getString(R.string.error_input, "Nome"));
            } else if(age.equals("")){
                input_age.setError(getString(R.string.error_input, "Idade"));
            } else{
                People people = new People(nextId, name, Integer.parseInt(age));
                database.insertPeople(people);
                database.close();
            }
        });

        return view;
    }
}