package com.connectask.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.Fragments.CadastroEndereco;
import com.connectask.activity.adapter.EnderecoAdapter;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.ValoresFiltro;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Endereco extends AppCompatActivity {

    private DatabaseReference firebase;

    private ArrayAdapter adapter;
    private ArrayList<com.connectask.activity.model.Endereco> listaEndereco;

    private Button buttonNovoEndereco;
    private ListView listViewEndereco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

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

        buttonNovoEndereco = (Button) findViewById(R.id.buttonNovoEndereco);
        listViewEndereco = (ListView) findViewById(R.id.listViewEndereco);

        listarEndereco();

        buttonNovoEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastroEndereco cadastroEndereco = new CadastroEndereco();

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_endereco, cadastroEndereco);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
    }

    private void listarEndereco(){
        listaEndereco = new ArrayList<>();

        adapter = new EnderecoAdapter(
                Endereco.this,
                listaEndereco
        );

        listViewEndereco.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(Endereco.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("endereco").child(identificadorUsuarioLogado);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaEndereco.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    com.connectask.activity.model.Endereco endereco = dados.getValue(com.connectask.activity.model.Endereco.class);

                    listaEndereco.add(endereco);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
