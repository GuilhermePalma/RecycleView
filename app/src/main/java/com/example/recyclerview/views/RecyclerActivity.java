package com.example.recyclerview.views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.DataBaseHelper;
import com.example.recyclerview.controller.RecyclerAdapter;
import com.example.recyclerview.model.ClickRecycleView;
import com.example.recyclerview.model.People;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity implements ClickRecycleView {

    private RecyclerAdapter adapter;
    public ArrayList<People> peopleList = new ArrayList<>();

    private ImageButton help;
    private ImageButton btn_fragment;
    private FloatingActionButton floatingActionButton;
    private FrameLayout fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private final DataBaseHelper database = new DataBaseHelper(this);
    private NewUserFragment userFragment;
    private int nextId_db;
    private boolean isOpenFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        floatingActionButton = findViewById(R.id.floatingButton);
        help = findViewById(R.id.btn_help);
        btn_fragment = findViewById(R.id.btn_fragment);
        fragment = findViewById(R.id.fragment_newUser);

        fragmentManager = getSupportFragmentManager();

        // Configura o RecyclerView
        setUpRecyclerView();

        // Carrega as pessoas do Banco de Dados
        recoveryPeoples();

        // Listeners dos Botões
        listenerFloatingButton();
        listenerHelp();
        listenerFragment();

    }

    // Configura o RecyclerView
    public void setUpRecyclerView(){
        //Instancia o RecyclerView e Configura
        RecyclerView recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intancia o Adapter do Recycler View
        adapter = new RecyclerAdapter(this, peopleList, this);
        recyclerView.setAdapter(adapter);
    }

    // Recupera as Pessoas já criadas e salvas no BD
    private void recoveryPeoples() {
        int id, age;
        String name;
        Cursor cursor = database.selectAllPeoples();

        // Reinicia o RecyclerView
        peopleList.clear();

        if (cursor.moveToFirst()){
            // Recupera os Dados enquanto tiver uma Proxima Posição
            do {
                id = cursor.getInt(0);
                name = cursor.getString(1);
                age = cursor.getInt(2);

                // Instancia a Classe
                People peopleDataBase = new People(id,name,age);
                // Adiciona no Array do RecylcerView
                peopleList.add(peopleDataBase);

            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();
        database.close();
    }

    // Listener do Button Help
    private void listenerHelp() {
        help.setOnClickListener( v -> {

            AlertDialog alert_help = new AlertDialog.Builder(this).create();

            alert_help.setTitle(getString(R.string.title_dialog));
            alert_help.setMessage(getString(R.string.message_dialog));

            alert_help.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (dialog, which) -> alert_help.dismiss());
            alert_help.show();

        });
    }

    // Listener de Clique no Floating Button
    private void listenerFloatingButton() {
        floatingActionButton.setOnClickListener(v -> {

            nextId_db = database.nextId();

            // Instancia a Lista de Forma Aleatoria
            People peopleInstance = new People(nextId_db);

            // Insere a Pessoa no Banco de Dados
            database.insertPeople(peopleInstance);
            database.close();

            //Adiciona uma Pessoa ao Array
            peopleList.add(peopleInstance);

            //Atualiza o Conteudo no Adapter
            adapter.notifyDataSetChanged();

        });
    }

    // Listener do ImageButton que abre/fecha o Fragment
    private void listenerFragment() {
        btn_fragment.setOnClickListener(v -> {
            if (isOpenFragment) closeFragment(v);
            else openFragment();
        });
    }

    // Inicia o Fragment
    private void openFragment() {

        // Instancia a Classe do Fragment
        userFragment = NewUserFragment.newInstance();

        // Cria uma ação a ser executada
        fragmentTransaction = fragmentManager.beginTransaction();

        // Adiciona o Fragment e Altera a Variavel
        fragmentTransaction.add(R.id.fragment_newUser, userFragment).addToBackStack(null).commit();

        btn_fragment.setImageResource(R.drawable.ic_keyboard_arrow_up);
        isOpenFragment = true;

        // Deixa o FrameLayout do Fragment Visivel
        fragment.setVisibility(View.VISIBLE);
    }

    // Fecha o Fragment se estiver um aberto
    private void closeFragment(View v) {

        // Deixa o FrameLayout do Fragment 'Escondido'
        fragment.setVisibility(View.GONE);

        // Recupera o Fragment Aberto pelo ID
        userFragment = (NewUserFragment) fragmentManager.findFragmentById(R.id.fragment_newUser);

        if(userFragment != null){
            // Fragment Encontrado
            fragmentTransaction = fragmentManager.beginTransaction();
            //Remove o Fragment pelo ID
            fragmentTransaction.remove(userFragment).commit();

            // Altera o Icone e a Variavel do Fragment
            btn_fragment.setImageResource(R.drawable.ic_keyboard_arrow_down);
            isOpenFragment = false;

            closeKeyboard(v);

            // Atualiza a Lista de Pessoas
            recoveryPeoples();
        } else{
            Toast.makeText(this, R.string.no_find_fragment, Toast.LENGTH_SHORT).show();
        }
    }

    // Fecha o Teclado se estiver Aberto
    public void closeKeyboard(View view){
        InputMethodManager keyboardManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        // Ao clicar no botão, caso o teclado esteja ativo ele é fechado
        if (keyboardManager != null) {
            keyboardManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

        recoveryPeoples();
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