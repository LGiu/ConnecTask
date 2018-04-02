package com.connectask.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.ValoresFiltro;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth usuarioAutenticacao;

    private DatabaseReference firebase;

    private ListView listViewTarefas;
    private ArrayAdapter adapter;
    private ArrayAdapter adapterBusca;
    private ArrayList<Tarefa> listaTarefas;
    private ArrayList<Tarefa> listaTarefasBusca;
    private Button buttonFiltros;
    private SearchView searchViewBusca;
    private SwipeRefreshLayout mSwipeToRefresh;

    private String id_processoTarefa = "";
    private String id_tarefa = "";
    private int er = 0;

    private boolean controle = false;

    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();

        setContentView(R.layout.activity_home);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        //Tirar título Toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controle = false;

                Preferencias preferencias = new Preferencias(Home.this);
                final String identificadorUsuarioLogado = preferencias.getIdentificado();

                firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dados : dataSnapshot.getChildren()) {
                            ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                            if (((processoTarefa.getId_usuario_emissor().equals(identificadorUsuarioLogado) || processoTarefa.getId_usuario_realizador().equals(identificadorUsuarioLogado))) && processoTarefa.getAtivo().equals("1") ){
                                controle = true;
                            }

                        }
                        if(controle){
                            Toast.makeText(Home.this, "Você já está envolvido em um processo de uma tarefa", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(Home.this, CadastroTarefa.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        listarTarefas();
        loading.cancel();

        nomeUsuario();

        buttonFiltros  = (Button) findViewById(R.id.buttonFiltros);
        buttonFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Filtro.class);
                startActivity(intent);
            }
        });


        mSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listarTarefas();
                mSwipeToRefresh.setRefreshing(false);
            }
        });

        tarefaFinalizada();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchFiltro());

        //
        final MenuItem item = menu.findItem(R.id.app_bar_notification);

        controle = false;

        Preferencias preferencias = new Preferencias(Home.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if (((processoTarefa.getId_usuario_emissor().equals(identificadorUsuarioLogado) || processoTarefa.getId_usuario_realizador().equals(identificadorUsuarioLogado))) && processoTarefa.getAtivo().equals("1") ){
                        item.setIcon(R.drawable.not);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.app_bar_notification:
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                com.connectask.activity.Fragments.Dialog dialogFragment = new com.connectask.activity.Fragments.Dialog();
                dialogFragment.show(fragmentManager, "Tarefas ativas no momento");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Home.this,
                        listaTarefasBusca
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, Perfil.class);
            startActivity(intent);
        } else if (id == R.id.nav_tarefas_cadastradas) {
            Intent intent = new Intent(this, MinhasTarefasCadastradas.class);
            startActivity(intent);
        } else if (id == R.id.nav_tarefas_realizadas) {
            Intent intent = new Intent(this, MinhasTarefasRealizadas.class);
            startActivity(intent);
        } else if (id == R.id.nav_pagamento) {

        } else if (id == R.id.nav_configuracoes) {

        } else if (id == R.id.nav_logout) {
            deslogarUsuario();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void deslogarUsuario(){
        usuarioAutenticacao.signOut();
        startActivity(new Intent(Home.this, MainActivity.class)); //Go back to home page
        finish();
    }

    private void listarTarefas(){
        loading = new ProgressDialog(Home.this);
        loading.show();

        ValoresFiltro valoresFiltro = new ValoresFiltro(Home.this);
        final String categoria = valoresFiltro.getCategoria();
        final int localizacao = valoresFiltro.getLocalizacao();
        final int valor = valoresFiltro.getValor();
        final int tempo = valoresFiltro.getTempo();

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

                    Preferencias preferencias = new Preferencias(Home.this);
                    final String identificadorUsuarioLogado = preferencias.getIdentificado();

                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    String usuarioLogado = tarefa.getId_usuario();
                    String statusTarefa = tarefa.getStatus();

                    if((!(usuarioLogado.equals(identificadorUsuarioLogado)) && (statusTarefa.equals("1")))) {
                        if (categoria != "") {
                            if ((tarefa.getTipo().toString() == categoria) &&
                                 /*(Integer.parseInt(tarefa.getLocalizacao().toString()) < valor) &&*/
                                    (Integer.parseInt(tarefa.getValor().toString().substring(0, tarefa.getValor().length() - 3)) < valor) &&
                                    (Integer.parseInt(tarefa.getTempo().toString()) < tempo)) {
                                listaTarefas.add(tarefa);
                            }
                        } else {
                            if (/*(Integer.parseInt(tarefa.getLocalizacao().toString()) < valor) &&*/
                                    (Integer.parseInt(tarefa.getValor().toString().substring(0, tarefa.getValor().length() - 3)) < valor) &&
                                            (Integer.parseInt(tarefa.getTempo().toString()) < tempo)) {
                                listaTarefas.add(tarefa);
                            }
                        }
                    }
                }

                //Avisar adapter que mudou
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //https://stackoverflow.com/questions/44777989/firebase-infinite-scroll-list-view-load-10-items-on-scrolling
    }

    protected void nomeUsuario(){
        Preferencias preferencias = new Preferencias(Home.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail().toString()))) {
                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);
                        TextView navNome = (TextView) headerView.findViewById(R.id.textViewNome);
                        navNome.setText(usuario.getNome().toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tarefaFinalizada(){
        Preferencias preferencias = new Preferencias(Home.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    //Emissor
                    if (identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_emissor()) && processoTarefa.getAtivo().equals("1")){
                        id_processoTarefa = processoTarefa.getId();
                        id_tarefa = processoTarefa.getId_tarefa();
                        er = 1;
                    }
                    //Realizador
                    else if (identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_realizador()) && processoTarefa.getAtivo().equals("1")){
                        id_processoTarefa = processoTarefa.getId();
                        id_tarefa = processoTarefa.getId_tarefa();
                        er = 2;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if (tarefa.getId().equals(id_tarefa) && tarefa.getStatus().equals("4")){
                        if(er == 1){
                            Intent intent = new Intent(Home.this, TarefaFinalizadaEmissor.class);
                            intent.putExtra("id", tarefa.getId());
                            intent.putExtra("id_ProcessoTarefa", id_processoTarefa);
                            startActivity(intent);
                        }
                        else if (er == 2){
                            Intent intent = new Intent(Home.this, TarefaFinalizaRealizador.class);
                            intent.putExtra("id", tarefa.getId());
                            intent.putExtra("id_ProcessoTarefa", id_processoTarefa);
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