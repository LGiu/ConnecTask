package com.connectask.activity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Progress;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MinhasTarefasAtivas extends AppCompatActivity {

    private DatabaseReference firebase;

    private TextView textViewNenhuma;
    private ListView listViewTarefas;
    private ArrayAdapter adapter;
    private ArrayList<Tarefa> listaTarefas;
    private ArrayList<Tarefa> listaTarefasBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_tarefas_ativas);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Progress progress = new Progress(MinhasTarefasAtivas.this, false);
        progress.threard(1000);

        textViewNenhuma = (TextView) findViewById(R.id.textViewNenhuma);

        listarTarefas();

        SearchView searchView = (SearchView) findViewById(R.id.searchViewBusca);
        searchView.setOnQueryTextListener(new SearchFiltro());
    }

    //Busca
    public class SearchFiltro implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            // TODO Auto-generated method stub
            //Log.i("Script", "onQueryTextSubmit-> " + query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String text) {
            if (text.toString() != null && !text.toString().equals("")) {
                listaTarefasBusca = new ArrayList<>();

                listViewTarefas = (ListView) findViewById(R.id.listViewTarefas);
                adapter = new TarefaAdapter(
                        MinhasTarefasAtivas.this,
                        listaTarefasBusca,
                        2
                );
                listViewTarefas.setAdapter(adapter);

                for (Tarefa  tarefa : listaTarefas)
                {
                    if (tarefa.getTitulo().toLowerCase().contains(text.toLowerCase())) {
                        listaTarefasBusca.add(tarefa);
                    }
                }
            }
            else {
                listarTarefas();
            }
            return false;
        }
    }


    private void listarTarefas(){
        listaTarefas = new ArrayList<>();

        listViewTarefas = (ListView) findViewById(R.id.listViewTarefas);
        adapter = new TarefaAdapter(
                MinhasTarefasAtivas.this,
                listaTarefas,
                3
        );
        listViewTarefas.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(MinhasTarefasAtivas.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar lista
                listaTarefas.clear();
                //percorre o nó

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if((tarefa.getId_usuario().equals(identificadorUsuarioLogado)) && tarefa.getStatus().equals("1")){
                        textViewNenhuma.setVisibility(View.GONE);
                        listaTarefas.add(tarefa);
                    }

                }

                //Avisar adapter que mudou
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
