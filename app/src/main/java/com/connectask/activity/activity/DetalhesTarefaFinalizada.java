package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.Fragments.PaginaUsuario;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Progress;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    private TextView textViewValor;
    private TextView textViewNome;
    private TextView textViewUsuario;
    private ImageButton imageButtonUsuario;

    private String usuarioPEmissor = "";
    private String usuarioPRealizador = "";
    private String usuarioTarefa = "";

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

        Progress progress = new Progress(DetalhesTarefaFinalizada.this, false);
        progress.threard(1000);

        textViewData = (TextView) findViewById(R.id.textViewData);
        textViewTipo = (TextView) findViewById(R.id.textViewTipo);
        textViewTarefa = (TextView) findViewById(R.id.textViewTarefa);
        textViewDescricao = (TextView) findViewById(R.id.textViewComentario);
        textViewValor = (TextView) findViewById(R.id.textViewValor);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        imageButtonUsuario = (ImageButton) findViewById(R.id.imageButtonUsuario);

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

                        if (((processoTarefa.getId_usuario_emissor().equals(identificadorUsuarioLogado) || processoTarefa.getId_usuario_realizador().equals(identificadorUsuarioLogado)))){
                            controle = true;
                            usuarioPEmissor = processoTarefa.getId_usuario_emissor();
                            usuarioPRealizador = processoTarefa.getId_usuario_realizador();
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
                            textViewData.setText(tarefa.getDataCadastro().toString().replace("-","/"));

                            String tipo = tarefa.getTipo();
                            textViewTipo.setText(tarefa.getTipo().toString());

                            String tituloTarefa = tarefa.getTitulo();
                            textViewTarefa.setText(tituloTarefa);

                            textViewDescricao.setText(tarefa.getDescricao().toString());
                            textViewValor.setText(tarefa.getValor().toString());

                            if(usuarioPEmissor.equals(tarefa.getId_usuario())){
                                textViewUsuario.setText("Cadastrado por:");
                                usuarioTarefa = usuarioPEmissor;

                            }
                            else{
                                textViewUsuario.setText("Realizado por:");
                                usuarioTarefa = usuarioPRealizador;
                            }
                            setNome(tarefa.getId_usuario());

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
                    paginaUsuario.setContext(DetalhesTarefaFinalizada.this);
                    paginaUsuario.setId(idTarefa);

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_detalhes_tarefa, paginaUsuario);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });

            imageButtonUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaginaUsuario paginaUsuario = new PaginaUsuario();
                    paginaUsuario.setContext(DetalhesTarefaFinalizada.this);
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

    private void setNome(final String user){
        firebase2 = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    if(user.toString().equals(usuarioTarefa)){
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
