package com.example.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ClickRecycleView {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recLayoutManager;
    RecycleAdapter adapter;
    private List<People> peopleList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRecyclerView();
        setButton();
        listenersButtons();
    }


    public void setRecyclerView(){
        //Instancia o RecyclerView
        recyclerView =  findViewById(R.id.recycler_main);
        recLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recLayoutManager);

        adapter = new RecycleAdapter(this,peopleList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setButton() {
        floatingActionButton = findViewById(R.id.floatButton);
    }

    private static String[] names = {"Alan", "Arthur", "Nicolas", "Angela", "Brenda", "Liz"};
    private static int[] ages = {32, 54, 65, 84, 41, 6};
    Random randomPosition = new Random();

    //Listeners dos Botões
    private void listenersButtons() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                People peopleInstance = new People(names[randomPosition.nextInt(4)],
                        ages[randomPosition.nextInt(4)]);

                //Adiciona uma Pessoa ao Array
                peopleList.add(peopleInstance);
                //Avisa o adapter que o conteudo foi Alterado
                adapter.notifyDataSetChanged();
            }
        });
    }


    //Metodo Herdado da Interface para Tratar o Clique em um Item da Lista
    @Override
    public void onCustomClick(Object object) {
        People people = (People) object;
        String name = people.getNome();
        int age = people.getAge();

        //Aqui é possivel enviar dados para outra activity
    }

    public void OpenWindow(View view){
        Intent intent = new Intent(this, WindowResults.class);
        startActivity(intent);
    }

}