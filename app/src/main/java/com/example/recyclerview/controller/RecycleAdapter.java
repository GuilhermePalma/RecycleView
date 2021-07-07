package com.example.recyclerview.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.model.ClickRecycleView;
import com.example.recyclerview.model.People;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    //Context e List usando a Classe "People" e Interface
    Context context;
    private final List<People> peopleList;
    public ClickRecycleView clickRecycleView;

    //Contrutor
    public RecycleAdapter(Context context, List<People> list, ClickRecycleView clickRecycleView) {
        this.context = context;
        this.peopleList = list;
        this.clickRecycleView = clickRecycleView;
    }

    //Cria o ViewHolder. Instancia e Infla com o Layout a ser usado
    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(context).inflate
                (R.layout.design_recycler_view, viewGroup, false);

        return new RecycleViewHolder(itemView);

    }

    //Pega os Valores da List
    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        if (context != null) {
            People people = peopleList.get(position);
            holder.txtName.setText(people.getName());
            holder.txtIdade.setText(String.valueOf(people.getAge()));
            // Clique do Usuario no Item
            holder.itemView.setOnClickListener(v ->
                    clickRecycleView.onCustomClick(position)
            );

            // Clique no Button de Excluir
            holder.delete.setOnClickListener(v ->
                    clickRecycleView.deletePeople(position)
            );

            // Clique no Button de Aumentar Idade
            holder.more_age.setOnClickListener(v ->
                    clickRecycleView.addAge(position)
            );

            // Clique no Button de Ver Mais
            holder.show_more.setOnClickListener( v ->
                    clickRecycleView.showMore(peopleList.get(position), position)
            );
        }

    }

    //Conta os Itens da Lista ---> Essencial p/ não criar uma Lista null
    @Override
    public int getItemCount() {
        if (peopleList != null){
            return peopleList.size();
        } else {
            return 0;
        }
    }

    // Classe Protegida que retorna os campos usados e a Interface
    protected class RecycleViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtName;
        protected TextView txtIdade;

        protected Button delete;
        protected Button more_age;
        protected Button show_more;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_nome);
            txtIdade = itemView.findViewById(R.id.txt_idade);
            delete = itemView.findViewById(R.id.btn_delete);
            more_age = itemView.findViewById(R.id.btn_addAge);
            show_more = itemView.findViewById(R.id.btn_more);
        }
    }

}