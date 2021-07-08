package com.example.recyclerview.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.controller.DataBaseHelper;
import com.example.recyclerview.model.ClickRecycleView;
import com.example.recyclerview.model.People;
import com.example.recyclerview.R;
import com.example.recyclerview.controller.RecycleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ClickRecycleView {

    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    public ArrayList<People> peopleList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;

    Random randomPosition = new Random();
    private static final String[] names = {"Alan", "Arthur", "Nicolas", "Angela", "Brenda", "Liz"};
    private static final int[] ages = {32, 54, 65, 84, 41, 6, 15, 22};


    private final DataBaseHelper database = new DataBaseHelper(this);
    private int nextId_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingButton);

        setUpRecyclerView();

        hasDeleteItem();
        recoveryPeoples();

        listenerFloatingButton();
    }

    // Configura o RecyclerView
    public void setUpRecyclerView(){
        //Instancia o RecyclerView e Configura
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intancia o Adapter do Recycler View
        adapter = new RecycleAdapter(this, peopleList, this);
        recyclerView.setAdapter(adapter);

        // Cria um divisor entre os Itens
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));
    }

    // Verifica se a Lista e o Banco de Dados tem os mesmos Tamanhos/Dados
    private void hasDeleteItem(){
        if (peopleList.size() > database.amountPeoples()){
            // Limpa a List e Recupera os Dados do Banco de Dados
            peopleList.clear();
            database.close();
            recoveryPeoples();
        }
    }

    // Recupera as Pessoas já criadas e salvas no BD
    private void recoveryPeoples() {
        int id, age;
        String name;
        Cursor cursor = database.selectAllPeoples();

        if (cursor.moveToFirst()){
            // Recupera os Dados enquanto tiver uma Proxima Posição
            do {
                id = cursor.getInt(0);
                name = cursor.getString(1);
                age = cursor.getInt(2);

                // Instancia a Classe
                People peopleDataBase = new People(id,name,age);

                // Insere no Array caso não esteja no Array
                if (!peopleList.contains(peopleDataBase)){
                    peopleList.add(peopleDataBase);
                    // Notifica o Recycler Adpater que houve alterações
                    adapter.notifyDataSetChanged();
                }

            } while (cursor.moveToNext());
        }

        database.close();
    }

    // Listener de Clique no Floating Button
    private void listenerFloatingButton() {
        floatingActionButton.setOnClickListener(v -> {

            nextId_db = database.nextId();

            // Instancia a Lista de Forma Aleatoria
            People peopleInstance = new People(nextId_db,
                    names[randomPosition.nextInt(5)],
                    ages[randomPosition.nextInt(7)]);

            // Insere a Pessoa no Banco de Dados
            database.insertPeople(peopleInstance);
            database.close();

            //Adiciona uma Pessoa ao Array
            peopleList.add(peopleInstance);

            //Atualiza o Conteudo no Adapter
            adapter.notifyDataSetChanged();

        });
    }

    // Clique no CardView
    @Override
    public void onCustomClick(int position) {
        String name = peopleList.get(position).getName();
        int age = peopleList.get(position).getAge();

        Toast.makeText(this, "Nome do Usuario: " + name + "\nIdade: " + age,
                Toast.LENGTH_SHORT).show();
    }

    // Clique no Botão "+"
    @Override
    public void addAge(int position) {

        int idPeople = peopleList.get(position).getId();
        int agePeople = peopleList.get(position).getAge() + 1;
        String namePeople = peopleList.get(position).getName();

        People people_age = new People(idPeople,namePeople,agePeople);

        database.updatePeople(people_age);
        database.close();

        peopleList.set(position, people_age);
        adapter.notifyDataSetChanged();
    }

    // Clique no Botão Lixeira
    @Override
    public void deletePeople(int position) {
        int id = peopleList.get(position).getId();
        database.deletePeople(id);
        database.close();

        hasDeleteItem();

        adapter.notifyDataSetChanged();
    }

    // Clique nos 3 pontinhos
    @Override
    public void showMore(Object object, int position)  {
        // Outra forma de recuperar a Classe do Item Clicado
        People people = (People) object;
        int id = people.getId();
        String name = people.getName();
        int age = people.getAge();

        // Pega a Posição do Array e soma +1 p/ representar o item na Lista
        position ++;

        Bundle dataPeople = new Bundle();
        dataPeople.putInt("id", id);
        dataPeople.putString("name", name);
        dataPeople.putInt("age", age);
        dataPeople.putInt("position", position);

        //Aqui é possivel enviar dados para outra activity
        Intent activityResult = new Intent(this, WindowResults.class);
        activityResult.putExtras(dataPeople);
        startActivity(activityResult);
        finish();
    }

}