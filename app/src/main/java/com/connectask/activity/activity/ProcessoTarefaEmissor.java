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
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ProcessoTarefaEmissor extends AppCompatActivity {

    private String idTarefa;
    public String id_ProcessoTarefa;
    private String idVariavel;
    private String idUsario;
    private String statusTarefa;

    private DatabaseReference firebase;

    private Button buttonFinalizar;
    private Button buttonCancelar;
    private TextView textViewTipo;
    private TextView textViewTitulo;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewNome;
    private TextView textViewLocal;
    private ImageButton imageButtonLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processo_tarefa_emissor);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");


        //Status tarefa
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("tarefas")
                .child(idTarefa);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);
                    statusTarefa = tarefa.getStatus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(statusTarefa.equals("2")) {

            buttonFinalizar = (Button) findViewById(R.id.buttonFinalizar);
            buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
            textViewTipo = (TextView) findViewById(R.id.textViewTipo);
            textViewTitulo = (TextView) findViewById(R.id.textViewTitulo);
            textViewDescricao = (TextView) findViewById(R.id.textViewDescricao);
            textViewTempo = (TextView) findViewById(R.id.textViewTempo);
            textViewNome = (TextView) findViewById(R.id.textViewNome);
            textViewLocal = (TextView) findViewById(R.id.textViewLocal);
            imageButtonLocal = (ImageButton) findViewById(R.id.imageButtonLocal);

            if (idTarefa != null) {
                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("tarefas");

                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Tarefa tarefa = dados.getValue(Tarefa.class);

                            idVariavel = tarefa.getId();
                            if (idVariavel.equals(idTarefa)) {
                                textViewTipo.setText(tarefa.getTipo());
                                textViewTitulo.setText(tarefa.getTitulo());
                                textViewDescricao.setText(tarefa.getDescricao());
                                textViewTempo.setText(tarefa.getTempo());

                                idUsario = tarefa.getId_usuario();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                firebase = ConfiguracaoFirebase.getFirebase()
                        .child("usuario");

                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            Usuario usuario = dados.getValue(Usuario.class);

                            idVariavel = usuario.getId();
                            if (idVariavel.equals(idUsario)) {
                                textViewNome.setText(usuario.getNome());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

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

            buttonFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(ProcessoTarefaEmissor.this)
                            .setTitle("Finalizar tarefa")
                            .setMessage("Tem certeza que deseja finalizar esta tarefa?")
                            .setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        firebase = ConfiguracaoFirebase.getFirebase();

                                        firebase.child("tarefas")
                                                .child(idTarefa)
                                                .child("status").setValue("4");

                                        Intent intent = new Intent(ProcessoTarefaEmissor.this, TarefaFinalizadaEmissor.class);
                                        intent.putExtra("id", idTarefa);
                                        intent.putExtra("id_ProcessoTarefa", id_ProcessoTarefa);
                                        startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("Não", null)
                            .show();
                }
            });

            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(ProcessoTarefaEmissor.this)
                            .setTitle("Finalizar tarefa")
                            .setMessage("Tem certeza que deseja cancelar esta tarefa?")
                            .setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            firebase = ConfiguracaoFirebase.getFirebase();

                                            firebase.child("tarefas")
                                                    .child(idTarefa)
                                                    .child("status").setValue("3");

                                            Intent intent = new Intent(ProcessoTarefaEmissor.this, Home.class);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("Não", null)
                            .show();
                }
            });

        }

        else if (statusTarefa.equals("4")){
            intent = new Intent(ProcessoTarefaEmissor.this, TarefaFinalizadaEmissor.class);
            intent.putExtra("id", idTarefa);
            startActivity(intent);
        }
    }
}
