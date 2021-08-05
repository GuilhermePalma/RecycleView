package com.example.recyclerview.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.ClickRecyclerView;

import java.util.List;

public class RecyclerLinearAdapter extends RecyclerView.Adapter<RecyclerLinearAdapter.RecycleViewHolder> {

    //Context e List usando a Classe "People" e Interface
    Context context;
    private final List<People> peopleList;
    public ClickRecyclerView clickLinearRecycler;

    //Contrutor
    public RecyclerLinearAdapter(Context context, List<People> list, ClickRecyclerView clickLinearRecycler) {
        this.context = context;
        this.peopleList = list;
        this.clickLinearRecycler = clickLinearRecycler;
    }

    //Cria o ViewHolder. Instancia e Infla com o Layout a ser usado
    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(context).inflate
                (R.layout.layout_linear_recycler, viewGroup, false);

        return new RecycleViewHolder(itemView);

    }

    //Pega os Valores da List
    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        if (context != null) {
            People people = peopleList.get(position);

            // Pega o Context, usando a View do Holder ---> P/ acessar as Strings
            String dataFormatted = holder.itemView.getContext().getString(R.string.label_data,
                    String.format("%s", people.getName()), people.getAge());

            holder.txtData.setText(dataFormatted);
            // Clique do Usuario no Item
            holder.itemView.setOnClickListener(v ->
                    clickLinearRecycler.onCustomClick(position)
            );

            // Clique no Button de Excluir
            holder.delete.setOnClickListener(v ->
                    clickLinearRecycler.deletePeople(position)
            );

            // Clique no Button de Aumentar Idade
            holder.more_age.setOnClickListener(v ->
                    clickLinearRecycler.addAge(position)
            );

            // Clique no Button de Ver Mais
            holder.show_more.setOnClickListener(v ->
                    clickLinearRecycler.showMore(peopleList.get(position), position)
            );
        }

    }

    //Conta os Itens da Lista ---> Essencial p/ não criar uma Lista null
    @Override
    public int getItemCount() {
        if (peopleList != null) {
            return peopleList.size();
        } else {
            return 0;
        }
    }

    // Classe Protegida que retorna os campos usados e a Interface
    protected class RecycleViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtData;

        protected Button delete;
        protected Button more_age;
        protected Button show_more;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txt_data);
            delete = itemView.findViewById(R.id.btn_delete);
            more_age = itemView.findViewById(R.id.btn_addAge);
            show_more = itemView.findViewById(R.id.btn_more);
        }
    }

}