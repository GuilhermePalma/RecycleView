package com.example.recyclerview.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerview.R;
import com.example.recyclerview.controller.ClickRecyclerView;

import java.util.List;


public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.RecycleViewHolder> {

    // Parametros utilzizados pelo RecyclerView
    private final Context context;
    private final List<People> peopleList;
    public ClickRecyclerView clickRecyclerView;

    // Contrutor da Classe RecyclerGridAdapter
    public RecyclerGridAdapter(Context context, List<People> list, ClickRecyclerView clickRecyclerView) {
        this.context = context;
        this.clickRecyclerView = clickRecyclerView;
        this.peopleList = list;
    }

    // Classe do ViewHolder ---> Recupera o ID do Text que aparecerá na Tela
    public static class RecycleViewHolder extends RecyclerView.ViewHolder {
        private final TextView text_recycler;
        private final CardView cardView;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            text_recycler = itemView.findViewById(R.id.txt_people);
            cardView = itemView.findViewById(R.id.cardView_number);
        }
    }

    // Define e Infla o Layout do Recycler
    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_recycler,
                parent, false);

        return new RecycleViewHolder(itemView);
    }

    // Cria e Gerencia um Item da RecyclerView
    @Override
    public void onBindViewHolder(@NonNull RecyclerGridAdapter.RecycleViewHolder holder, int position) {

        // Caso seja um Item da Primeira Coluna ---> Margem no lado Direito
        if (isFirstColumn(position)) {
            // Obtem os Parametros de Layout do CardView
            ViewGroup.LayoutParams layoutCardView = holder.cardView.getLayoutParams();

            // Verifica se os Parametros obtidos podem receber parametros de margem
            if (layoutCardView instanceof ViewGroup.MarginLayoutParams) {
                // Insere a margem e atualiza o item
                ((ViewGroup.MarginLayoutParams) layoutCardView).rightMargin = 30;
                holder.cardView.requestLayout();
            }
        }

        People people = peopleList.get(position);

        // Acessa as Strings e Formata com os Dados e Exibe na Tela
        String dataFormatted = holder.itemView.getContext().getString(R.string.label_data,
                String.format("%s", people.getName()), people.getAge());
        holder.text_recycler.setText(dataFormatted);

        // Clique no CardView ---> Redireciona para a Interface
        holder.cardView.setOnClickListener(v ->
                clickRecyclerView.showMore(peopleList.get(position), position)
        );
    }

    // Obtem a quantidade de Itens da Lista
    @Override
    public int getItemCount() {
        if (peopleList.size() == 0 || peopleList == null) {
            return 0;
        } else {
            return peopleList.size();
        }
    }

    public boolean isFirstColumn(int position) {
        return position % 2 == 0 || position == 0;
    }
}