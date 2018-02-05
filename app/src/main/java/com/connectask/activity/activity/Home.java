package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.connectask.R;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth usuarioAutenticacao;


    //NOVO CÓDIGO COMEÇ AQUI

    private DatabaseReference firebase;

    private ListView listViewTarefas;
    private ArrayAdapter adapter;
    private ArrayList<Tarefa> listaTarefas;

    //TERMINA AQUI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();

        setContentView(R.layout.activity_home);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CadastroTarefaFragment cadastroTarefaFragment = new CadastroTarefaFragment();
                fragmentTransaction.add(R.id.fragment_container, cadastroTarefaFragment);
                fragmentTransaction.addToBackStack(null).commit();

                fab.hide();
                toolbar.setTitle("Nova tarefa");
                toolbar.setNavigationIcon(null);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);*/

                Intent intent = new Intent(Home.this, CadastroTarefa.class);
                startActivity(intent);

            }
        });

        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        //Tirar título Toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Home");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                    if(!(usuarioLogado.equals(identificadorUsuarioLogado) && (statusTarefa.equals("1")))){
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tarefas) {

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

}
