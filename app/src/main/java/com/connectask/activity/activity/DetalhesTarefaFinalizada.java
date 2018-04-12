package com.connectask.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.Fragments.Local;
import com.connectask.activity.Fragments.PaginaUsuario;
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

public class DetalhesTarefaFinalizada extends AppCompatActivity {

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

    private ProcessoTarefa processoTarefa = new ProcessoTarefa();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_tarefa_finalizada);

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
        textViewDescricao = (TextView) findViewById(R.id.textViewComentario);
        textViewTempo = (TextView) findViewById(R.id.textViewCep);
        textViewValor = (TextView) findViewById(R.id.textViewNome);
        textViewNome = (TextView) findViewById(R.id.textViewNome);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        status = intent.getStringExtra("status");

        Preferencias preferencias = new Preferencias(DetalhesTarefaFinalizada.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        if(idTarefa != null){

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
                            textViewData.setText(tarefa.getData().toString().replace("-","/"));

                            String tipo = tarefa.getTipo();
                            textViewTipo.setText(tarefa.getTipo().toString());

                            String tituloTarefa = tarefa.getTitulo();
                            textViewTarefa.setText(tituloTarefa);

                            textViewDescricao.setText(tarefa.getDescricao().toString());
                            textViewTempo.setText(tarefa.getTempo().toString() + "hora(s)");
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

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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

        }
        else{
            Intent in = new Intent(DetalhesTarefaFinalizada.this, Home.class);
            startActivity(in);

        }


    }
}
