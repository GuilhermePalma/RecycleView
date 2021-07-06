package com.example.recyclerview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ClickRecycleView {

    private RecyclerView recyclerView;
    RecycleAdapter adapter;
    private final List<People> peopleList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;

    Random randomPosition = new Random();
    private static final String[] names = {"Alan", "Arthur", "Nicolas", "Angela", "Brenda", "Liz"};
    private static final int[] ages = {32, 54, 65, 84, 41, 6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingButton);

        setUpRecyclerView();
        listenerFloatingButton();
    }

    public void setUpRecyclerView(){
        //Instancia o RecyclerView e Configura
        recyclerView =  findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intancia o Adapter do Recycler View
        adapter = new RecycleAdapter(this, peopleList, this);
        recyclerView.setAdapter(adapter);

        // Cria um divisor entre os Itens
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));
    }


    private void listenerFloatingButton() {
        floatingActionButton.setOnClickListener(v -> {
            // Instancia a Lista de Forma Aleatoria
            People peopleInstance = new People(names[randomPosition.nextInt(4)],
                    ages[randomPosition.nextInt(4)]);

            //Adiciona uma Pessoa ao Array
            peopleList.add(peopleInstance);
            //Atualiza o Conteudo no Adapter
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void addAge(int position) {

        int agePeople = peopleList.get(position).getAge() + 1;
        String namePeople = peopleList.get(position).getNome();

        People people = new People(namePeople,agePeople);

        peopleList.set(position, people);
        adapter.notifyDataSetChanged();
    }

     @Override
    public void onCustomClick(int position) {
        String name = peopleList.get(position).getNome();
        int age = peopleList.get(position).getAge();

        Toast.makeText(this, "Nome do Usuario: " + name + "\nIdade:" + age,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deletePeople(int position) {
        peopleList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMore(Object object)  {
       /* People people = (People) object;
        String name = people.getNome();
        int age = people.getAge();

        Bundle dataPeople = new Bundle();
        dataPeople.putString("name", name);
        dataPeople.putInt("age", age);

        //Aqui é possivel enviar dados para outra activity
        Intent activityResult = new Intent(this, WindowResults.class);
        activityResult.putExtras(dataPeople);
        startActivity(activityResult);*/
        System.out.println("Area ainda em Desenvolvimento...\n Layout não concluido");
    }

}