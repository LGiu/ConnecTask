package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.Fragments.Local;
import com.connectask.activity.Fragments.PaginaUsuario;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class ProcessoTarefaRealizador extends AppCompatActivity {

    private String idTarefa;
    public String id_ProcessoTarefa;
    private String idVariavel;
    private String idUsario;
    private String statusTarefa;

    private DatabaseReference firebase1;
    private DatabaseReference firebase2;

    private Button buttonCancelar;
    private TextView textViewTipo;
    private TextView textViewTitulo;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewNome;
    private TextView textViewLocal;
    private TextView textViewData;
    private ImageButton imageButtonLocal;

    private Tarefa tarefa = new Tarefa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processo_tarefa_realizador);

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

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");

        //Status tarefa
        firebase1 = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(idTarefa.equals(tarefa.getId().toString())){
                        statusTarefa = tarefa.getStatus();

                        if(statusTarefa.equals("2")) {
                            buttonCancelar = (Button) findViewById(R.id.buttonFinalizar);
                            textViewTipo = (TextView) findViewById(R.id.textViewTipo);
                            textViewTitulo = (TextView) findViewById(R.id.textViewTitulo);
                            textViewDescricao = (TextView) findViewById(R.id.textViewDescricao);
                            textViewTempo = (TextView) findViewById(R.id.textViewCep);
                            textViewNome = (TextView) findViewById(R.id.textViewNome);
                            textViewLocal = (TextView) findViewById(R.id.textViewLocal);
                            textViewData = (TextView) findViewById(R.id.textViewData);
                            imageButtonLocal = (ImageButton) findViewById(R.id.imageButtonLocal);

                            if (idTarefa != null) {
                                firebase1 = ConfiguracaoFirebase.getFirebase()
                                        .child("tarefas");

                                firebase1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                            final Tarefa tarefa = dados.getValue(Tarefa.class);

                                            idVariavel = tarefa.getId();

                                            if (idVariavel.equals(idTarefa)) {
                                                textViewTipo.setText(tarefa.getTipo());
                                                textViewTitulo.setText(tarefa.getTitulo());
                                                textViewDescricao.setText(tarefa.getDescricao());
                                                textViewTempo.setText(tarefa.getTempo());
                                                textViewData.setText(tarefa.getData().replace("-","/"));

                                                firebase2 = ConfiguracaoFirebase.getFirebase().child("usuarios");

                                                firebase2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
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

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    firebase1 = ConfiguracaoFirebase.getFirebase();

                                    firebase1.child("tarefas")
                                            .child(idTarefa)
                                            .child("status").setValue("3");

                                    Intent intent = new Intent(ProcessoTarefaRealizador.this, Home.class);
                                    startActivity(intent);
                                }
                            });

                            textViewNome.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PaginaUsuario paginaUsuario = new PaginaUsuario();
                                    paginaUsuario.setId(idTarefa);

                                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    fragmentTransaction.replace(R.id.fragment_detalhes_tarefa, paginaUsuario);
                                    fragmentTransaction.addToBackStack(null).commit();
                                }
                            });

                            textViewLocal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Local local = new Local();
                                    local.setId(idTarefa);

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
                                    local.setId(idTarefa);

                                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                    fragmentTransaction.replace(R.id.fragment_detalhes_tarefa, local);
                                    fragmentTransaction.addToBackStack(null).commit();
                                }
                            });
                        }

                        else if (statusTarefa.equals("4")){
                            Intent intent = new Intent(ProcessoTarefaRealizador.this, TarefaFinalizaRealizador.class);
                            intent.putExtra("id", idTarefa);
                            startActivity(intent);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
