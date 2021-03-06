package com.connectask.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.DetalhesTarefa;
import com.connectask.activity.activity.DetalhesTarefaAtiva;
import com.connectask.activity.activity.DetalhesTarefaFinalizada;
import com.connectask.activity.model.Tarefa;

import java.util.ArrayList;

/**
 * Created by Leonardo Giuliani on 03/01/2018.
 */

public class TarefaAdapter extends ArrayAdapter<Tarefa>{
    private ArrayList<Tarefa> listaTarefa;
    private Context context;
    private int controle;

    private TextView tempo;
    private TextView te;

    public TarefaAdapter(Context c, ArrayList<Tarefa> objects, int controle) {
        super(c, 0, objects);
        this.context = c;
        this.listaTarefa = objects;
        this.controle = controle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está preenchida
        if( listaTarefa != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_tarefas, parent, false);

            // recupera elemento para exibição
            TextView titulo = (TextView) view.findViewById(R.id.textViewTitulo);
            TextView descricao = (TextView) view.findViewById(R.id.textViewComentario);
            tempo = (TextView) view.findViewById(R.id.textViewTempo);
            te = (TextView) view.findViewById(R.id.textViewTe);
            TextView valor = (TextView) view.findViewById(R.id.textViewNome);
            Button detalhes = (Button) view.findViewById(R.id.buttonDetalhes);

            final Tarefa tarefa = listaTarefa.get(position);
            titulo.setText( tarefa.getTitulo() );
            descricao.setText( tarefa.getDescricao() );
            valor.setText( tarefa.getValor() );

            if(controle == 1){
                tempo.setText( tarefa.getTempo() + " hora(s)");
            }
            else if(controle == 2){
                tempo.setVisibility( View.GONE);
                te.setVisibility( View.GONE);
            }

            detalhes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(controle == 1){
                        Intent intent = new Intent(context, DetalhesTarefa.class);
                        String id = tarefa.getId();
                        intent.putExtra("id", id);
                        intent.putExtra("status", tarefa.getStatus().toString());
                        context.startActivity(intent);
                    }
                    else if (controle == 2){
                        Intent intent = new Intent(context, DetalhesTarefaFinalizada.class);
                        String id = tarefa.getId();
                        intent.putExtra("id", id);
                        intent.putExtra("status", tarefa.getStatus().toString());
                        context.startActivity(intent);
                    }
                    else if (controle == 3){
                        Intent intent = new Intent(context, DetalhesTarefaAtiva.class);
                        String id = tarefa.getId();
                        intent.putExtra("id", id);
                        intent.putExtra("status", tarefa.getStatus().toString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        return view;
    }
}

