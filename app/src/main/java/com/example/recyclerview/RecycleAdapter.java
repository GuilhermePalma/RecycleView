package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    //Interface, context e list
    public static ClickRecycleView clickRecycleView;
    Context context;
    private List<People> peopleList;

    //Contrutor
    public RecycleAdapter(Context context, List<People> list, ClickRecycleView clickRecycleView) {
        this.context = context;
        this.peopleList = list;
        this.clickRecycleView = clickRecycleView;
    }

    //Classe Protegida que retorna os campos usados e a Interface
    protected class RecycleViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtName;
        protected TextView txtIdade;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_nome);
            txtIdade = itemView.findViewById(R.id.txt_idade);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecycleView.onCustomClick(peopleList.get(getLayoutPosition()));
                }
            });
        }
    }

    //Cria o ViewHolder; Instancia com o valor do Layout usado
    @NonNull
    @Override
    public RecycleAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.texts, viewGroup, false);
        return new RecycleAdapter.RecycleViewHolder(itemView);
    }

    //Pega os Valores da List
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.RecycleViewHolder holder, int position) {
        People people = peopleList.get(position);
        holder.txtName.setText(people.getNome());
        holder.txtIdade.setText(Integer.toString(people.getAge()));
    }

    //Conta os Itens da Lista
    @Override
    public int getItemCount() {
        return peopleList.size();
    }

}




