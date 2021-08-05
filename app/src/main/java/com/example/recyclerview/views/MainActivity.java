package com.example.recyclerview.views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.DataBaseHelper;
import com.example.recyclerview.model.RecyclerGridAdapter;
import com.example.recyclerview.model.RecyclerLinearAdapter;
import com.example.recyclerview.controller.ClickRecyclerView;
import com.example.recyclerview.model.People;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ClickRecyclerView {

    private RecyclerLinearAdapter adapterLinear;
    private RecyclerGridAdapter adapterGrid;
    public ArrayList<People> peopleList = new ArrayList<>();

    private ImageButton btn_fragment;
    private ImageButton help;
    private ImageButton btn_lists;
    private FloatingActionButton floatingActionButton;

    private FrameLayout fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RecyclerView recyclerView;

    private final DataBaseHelper database = new DataBaseHelper(this);
    private NewUserFragment userFragment;
    private boolean isOpenFragment;
    private boolean isGridList;

    private final String IS_GRID_LIST = "is_grid_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceItens();

        // Recupera o Estado do Tipo de Lista (Se Existir)
        Intent intentReturn = getIntent();
        if (intentReturn != null) {
            isGridList = intentReturn.getBooleanExtra(IS_GRID_LIST, false);
        }

        if (isGridList) {
            btn_lists.setBackgroundResource(R.drawable.ic_list);
            setUpRecyclerViewGrid();
        } else {
            btn_lists.setBackgroundResource(R.drawable.ic_view_comfy);
            setUpRecyclerViewLinear();
        }

        // Carrega as pessoas do Banco de Dados
        recoveryPeoples();

        // Listeners dos Botões
        listenerFloatingButton();
        listenerFragment();

        help.setOnClickListener(v -> dialogHowUse());

        btn_lists.setOnClickListener(v -> {
            if (!isGridList) {
                btn_lists.setBackgroundResource(R.drawable.ic_list);
                setUpRecyclerViewGrid();
                isGridList = true;
            } else {
                btn_lists.setBackgroundResource(R.drawable.ic_view_comfy);
                setUpRecyclerViewLinear();
                isGridList = false;
            }
        });
    }

    private void instanceItens() {
        floatingActionButton = findViewById(R.id.floatingButton);
        help = findViewById(R.id.btn_help);
        btn_fragment = findViewById(R.id.btn_fragment);
        btn_lists = findViewById(R.id.btn_list);
        fragment = findViewById(R.id.fragment_newUser);

        fragmentManager = getSupportFragmentManager();
        isOpenFragment = false;

        recyclerView = findViewById(R.id.recycler_main);
        isGridList = false;
    }

    // Configura o RecyclerView
    public void setUpRecyclerViewLinear() {
        // Define o tipo de Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Intancia o Adapter do Recycler View
        adapterLinear = new RecyclerLinearAdapter(this, peopleList, this);
        recyclerView.setAdapter(adapterLinear);
    }

    // Configura o RecyclerView
    public void setUpRecyclerViewGrid() {
        // Configura e define o Layout (Grid --> 2 Itens por Linha)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Intancia o Adapter do Recycler View
        adapterGrid = new RecyclerGridAdapter(getApplicationContext(), peopleList, this);
        recyclerView.setAdapter(adapterGrid);

        // Define a Quantidade de Itens por Linha
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

    }

    // Recupera as Pessoas já criadas e salvas no BD
    private void recoveryPeoples() {
        int id, age;
        String name;
        Cursor cursor = database.selectAllPeoples();

        // Reinicia o RecyclerView
        peopleList.clear();

        if (cursor.moveToFirst()) {
            // Recupera os Dados enquanto tiver uma Proxima Posição
            do {
                id = cursor.getInt(0);
                name = cursor.getString(1);
                age = cursor.getInt(2);

                // Instancia a Classe
                People peopleDataBase = new People(id, name, age);
                // Adiciona no Array do RecylcerView
                peopleList.add(peopleDataBase);

            } while (cursor.moveToNext());
        }

        //Atualiza o Conteudo no Adapter
        if (isGridList) {
            adapterGrid.notifyDataSetChanged();
        } else {
            adapterLinear.notifyDataSetChanged();
        }

        database.close();
    }

    // Listener do Button Help
    private void dialogHowUse() {
        AlertDialog alert_help = new AlertDialog.Builder(this).create();

        alert_help.setTitle(getString(R.string.title_dialog));
        alert_help.setMessage(getString(R.string.message_dialog));

        alert_help.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> alert_help.dismiss());
        alert_help.show();
    }

    // Listener de Clique no Floating Button
    private void listenerFloatingButton() {
        floatingActionButton.setOnClickListener(v -> {

            // Instancia a Lista de Forma Aleatoria
            People peopleInstance = new People(database.nextId());

            // Insere a Pessoa no Banco de Dados
            database.insertPeople(peopleInstance);
            database.close();

            //Adiciona uma Pessoa ao Array
            peopleList.add(peopleInstance);

            //Atualiza o Conteudo no Adapter
            if (isGridList) {
                adapterGrid.notifyDataSetChanged();
            } else {
                adapterLinear.notifyDataSetChanged();
            }
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

        recyclerView.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
    }

    // Fecha o Fragment se estiver um aberto
    private void closeFragment(View v) {

        // Deixa o FrameLayout do Fragment 'Escondido'
        fragment.setVisibility(View.GONE);

        // Recupera o Fragment Aberto pelo ID
        userFragment = (NewUserFragment) fragmentManager.findFragmentById(R.id.fragment_newUser);

        if (userFragment != null) {
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
        } else {
            Toast.makeText(this, R.string.no_find_fragment, Toast.LENGTH_SHORT).show();
        }

        recyclerView.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    // Fecha o Teclado se estiver Aberto
    public void closeKeyboard(View view) {
        InputMethodManager keyboardManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        // Ao clicar no botão, caso o teclado esteja ativo ele é fechado
        if (keyboardManager != null) {
            keyboardManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    // Interface do RecyclerView
    // Clique no CardView ou nos 3 pontinhos (Lista)
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

        People people_age = new People(idPeople, namePeople, agePeople);

        database.updatePeople(people_age);
        database.close();

        peopleList.set(position, people_age);

        //Atualiza o Conteudo no Adapter
        if (isGridList) {
            adapterGrid.notifyDataSetChanged();
        } else {
            adapterLinear.notifyDataSetChanged();
        }
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
    public void showMore(Object object, int position) {
        // Outra forma de recuperar a Classe do Item Clicado
        People people = (People) object;
        int id = people.getId();
        String name = people.getName();
        int age = people.getAge();

        // Pega a Posição do Array e soma +1 p/ representar o item na Lista
        position++;

        Bundle dataPeople = new Bundle();
        dataPeople.putInt("id", id);
        dataPeople.putString("name", name);
        dataPeople.putInt("age", age);
        dataPeople.putInt("position", position);
        dataPeople.putBoolean(IS_GRID_LIST, isGridList);

        //Aqui é possivel enviar dados para outra activity
        Intent activityResult = new Intent(this, WindowResults.class);
        activityResult.putExtras(dataPeople);
        startActivity(activityResult);
        finish();
    }

}