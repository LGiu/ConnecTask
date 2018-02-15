package com.connectask.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.Fragments.Local;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DetalhesTarefa extends AppCompatActivity {

    private String idTarefa;

    private DatabaseReference firebase;

    private TextView textViewTipo;
    private TextView textViewTarefa;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewValor;
    private TextView textViewNome;
    private Button buttonRealizar;
    private TextView textViewLocal;
    private ImageButton imageButtonLocal;

    private ProcessoTarefa processoTarefa = new ProcessoTarefa(DetalhesTarefa.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        textViewTipo = (TextView) findViewById(R.id.textViewTipo);
        textViewTarefa = (TextView) findViewById(R.id.textViewLocal);
        textViewDescricao = (TextView) findViewById(R.id.textViewDescricao);
        textViewTempo = (TextView) findViewById(R.id.textViewTempo);
        textViewValor = (TextView) findViewById(R.id.textViewTempo);
        buttonRealizar = (Button) findViewById(R.id.buttonRealizar);
        textViewLocal = (TextView) findViewById(R.id.textViewLocal);
        imageButtonLocal = (ImageButton) findViewById(R.id.imageButtonLocal);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");

        if(idTarefa != null){

            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("tarefas");

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados: dataSnapshot.getChildren()){
                        Tarefa tarefa = dados.getValue(Tarefa.class);

                        String id = tarefa.getId();
                        if(id.equals(idTarefa)){
                            textViewTipo.setText(tarefa.getTipo());
                            textViewTarefa.setText(tarefa.getTitulo());
                            textViewDescricao.setText(tarefa.getDescricao());
                            textViewTempo.setText(tarefa.getTempo());
                            textViewValor.setText(tarefa.getValor());

                            buttonRealizar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    new AlertDialog.Builder(DetalhesTarefa.this)
                                            .setTitle("Realizar tarefa")
                                            .setMessage("Tem certeza que deseja realizar esta tarefa?")
                                            .setPositiveButton("Sim",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            firebase.child(idTarefa)
                                                                    .child("status").setValue("2");

                                                            processoTarefa.salvar(idTarefa);

                                                            Intent intent = new Intent(DetalhesTarefa.this, ProcessoTarefaRealizador.class);
                                                            intent.putExtra("id", idTarefa);
                                                            intent.putExtra("id_ProcessoTarefa", processoTarefa.getId());
                                                            startActivity(intent);
                                                        }
                                                    })
                                            .setNegativeButton("Não", null)
                                            .show();
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            textViewLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Local local = new Local();

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_detalhes_tarefa, local);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });

            imageButtonLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Local local = new Local();

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_detalhes_tarefa, local);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });


        }


    }
}
