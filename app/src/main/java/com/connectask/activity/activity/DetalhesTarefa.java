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
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.Fragments.Local;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class DetalhesTarefa extends AppCompatActivity {

    private String idTarefa;
    private String status;
    private boolean controle = false;
    private boolean controle2 = false;

    private DatabaseReference firebase1;
    private DatabaseReference firebase2;

    private TextView textViewData;
    private TextView textViewTipo;
    private TextView textViewTarefa;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewValor;
    private TextView textViewNome;
    private Button buttonRealizar;
    private TextView textViewLocal;
    private ImageButton imageButtonLocal;

    private ProcessoTarefa processoTarefa = new ProcessoTarefa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa);

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


        textViewData = (TextView) findViewById(R.id.textViewData);
        textViewTipo = (TextView) findViewById(R.id.textViewTipo);
        textViewTarefa = (TextView) findViewById(R.id.textViewTarefa);
        textViewDescricao = (TextView) findViewById(R.id.textViewDescricao);
        textViewTempo = (TextView) findViewById(R.id.textViewTempo);
        textViewValor = (TextView) findViewById(R.id.textViewNome);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        buttonRealizar = (Button) findViewById(R.id.buttonRealizar);
        textViewLocal = (TextView) findViewById(R.id.textViewLocal);
        imageButtonLocal = (ImageButton) findViewById(R.id.imageButtonLocal);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        status = intent.getStringExtra("status");

        Preferencias preferencias = new Preferencias(DetalhesTarefa.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        if(idTarefa != null && (status.equals("1"))){

            controle = false;

            firebase1 = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

            firebase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                        if (((processoTarefa.getId_usuario_emissor().equals(identificadorUsuarioLogado) || processoTarefa.getId_usuario_realizador().equals(identificadorUsuarioLogado))) && processoTarefa.getAtivo().equals("1") ){
                            controle = true;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            firebase1 = ConfiguracaoFirebase.getFirebase().child("tarefas");

            firebase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados: dataSnapshot.getChildren()){
                        final Tarefa tarefa = dados.getValue(Tarefa.class);

                        String id = tarefa.getId();
                        if(id.equals(idTarefa)){
                            textViewData.setText(tarefa.getData().toString());

                            String tipo = tarefa.getTipo();
                            textViewTipo.setText(tarefa.getTipo().toString());

                            String tituloTarefa = tarefa.getTitulo();
                            textViewTarefa.setText(tituloTarefa);

                            textViewDescricao.setText(tarefa.getDescricao().toString());
                            textViewTempo.setText(tarefa.getTempo().toString());
                            textViewValor.setText(tarefa.getValor().toString());

                            firebase2 = ConfiguracaoFirebase.getFirebase().child("usuarios");

                            firebase2.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot dados: dataSnapshot.getChildren()) {
                                                                        Usuario usuario = dados.getValue(Usuario.class);
                                                                        if(tarefa.getId_usuario().toString().equals(codificarBase64(usuario.getEmail().toString()))){
                                                                            textViewNome.setText(usuario.getNome().toString());
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });


                                    buttonRealizar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(controle){
                                                Toast.makeText(DetalhesTarefa.this, "Você já está envolvido em um processo de uma tarefa", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                new AlertDialog.Builder(DetalhesTarefa.this)
                                                        .setTitle("Realizar tarefa")
                                                        .setMessage("Tem certeza que deseja realizar esta tarefa?")
                                                        .setPositiveButton("Sim",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        firebase1.child(idTarefa)
                                                                                .child("status").setValue("2");
                                                                        status = "2";

                                                                        processoTarefa.setId_tarefa(idTarefa);
                                                                        processoTarefa.setId_usuario_emissor(tarefa.getId_usuario());
                                                                        processoTarefa.setId_usuario_realizador(identificadorUsuarioLogado);
                                                                        processoTarefa.salvar();

                                                                        Intent intent = new Intent(DetalhesTarefa.this, ProcessoTarefaRealizador.class);
                                                                        intent.putExtra("id", idTarefa);
                                                                        intent.putExtra("id_ProcessoTarefa", processoTarefa.getId());

                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                        .setNegativeButton("Não", null)
                                                        .show();
                                            }
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
        else{
            Intent in = new Intent(DetalhesTarefa.this, Home.class);
            startActivity(in);

        }


    }
}
