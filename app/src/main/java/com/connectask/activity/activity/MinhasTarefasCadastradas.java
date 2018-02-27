package com.connectask.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.connectask.R;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MinhasTarefasCadastradas extends AppCompatActivity {

    private DatabaseReference firebase;

    private ListView listViewTarefas;
    private ArrayAdapter adapter;
    private ArrayList<Tarefa> listaTarefas;

    private SearchView searchViewBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_tarefas_cadastradas);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        listaTarefas = new ArrayList<>();

        listViewTarefas = (ListView) findViewById(R.id.listViewTarefas);
        adapter = new TarefaAdapter(
                this,
                listaTarefas
        );
        listViewTarefas.setAdapter(adapter);


        firebase = ConfiguracaoFirebase.getFirebase()
                .child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar lista
                listaTarefas.clear();
                //percorre o nó

                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Preferencias preferencias = new Preferencias(MinhasTarefasCadastradas.this);
                    final String identificadorUsuarioLogado = preferencias.getIdentificado();

                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    String usuarioLogado = tarefa.getId_usuario();
                    String statusTarefa = tarefa.getStatus();
                    if((usuarioLogado.equals(identificadorUsuarioLogado))){
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
