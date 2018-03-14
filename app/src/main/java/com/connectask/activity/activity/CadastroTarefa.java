package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.connectask.R;
import com.connectask.activity.Fragments.CadastroEndereco;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Endereco;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class CadastroTarefa extends AppCompatActivity {

    private EditText editTextTitulo;
    private Spinner spinnerTipo;
    private EditText editTextDescricao;
    private EditText editTextTempo;
    private Spinner spinnerTempo;
    private EditText editTextValor;
    private Spinner spinnerEndereco;
    private Button buttonCadastro;
    private Button buttonNovoEndereco;
    private List<String> listaTipo = new ArrayList<String>();
    private List<String> listaEndereco = new ArrayList<String>();
    private List<String> listaTempo = new ArrayList<String>();

    private Tarefa tarefa;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tarefa);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        editTextTitulo = (EditText) findViewById(R.id.editTextNome);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        spinnerTempo = (Spinner) findViewById(R.id.spinnerTempo);
        editTextTempo = (EditText) findViewById(R.id.editTextTempo);
        editTextValor = (EditText) findViewById(R.id.editTextValor);
        spinnerEndereco = (Spinner) findViewById(R.id.spinnerEndereco);
        buttonCadastro = (Button) findViewById(R.id.buttonFinalizar);
        buttonNovoEndereco = (Button) findViewById(R.id.buttonNovoEndereco);

        tipo();
        tempo();
        endereco();

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarefa = new Tarefa();
                tarefa.setTitulo(editTextTitulo.getText().toString());
                tarefa.setTipo(spinnerTipo.getSelectedItem().toString());
                tarefa.setDescricao(editTextDescricao.getText().toString());
                String temp = editTextTempo.getText().toString()+" "+spinnerTempo.getSelectedItem().toString();
                tarefa.setTempo(temp);
                tarefa.setValor((editTextValor.getText().toString()));
                tarefa.setEndereco(editTextValor.getText().toString());

                tarefa.salvar(CadastroTarefa.this);

                Intent intent = new Intent(CadastroTarefa.this, Pagamento.class);
                startActivity(intent);
              }
        });

        buttonNovoEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastroEndereco cadastroEndereco = new CadastroEndereco();

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_cadastro, cadastroEndereco);
                fragmentTransaction.addToBackStack(null).commit();


                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CadastroEndereco cadastroEndereco = new CadastroEndereco();
                FragmentTransaction add = fragmentTransaction.add(R.id.fragment_container, cadastroEndereco);
                fragmentTransaction.addToBackStack(null).commit();

                toolbar.setTitle("Nova tarefa");
                toolbar.setNavigationIcon(null);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);*/

            }
        });
    }

    public void tipo(){
        listaTipo.add("Doméstica");
        listaTipo.add("Externa");
        listaTipo.add("Serviço");
        listaTipo.add("Outro");

        ArrayAdapter<String> arrayAdapterTipo = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaTipo);
        arrayAdapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(arrayAdapterTipo);
    }

    public void tempo(){
        listaTempo.add("Hora(s)");
        listaTempo.add("Dia(s)");
        listaTempo.add("Minuto(s)");

        ArrayAdapter<String> arrayAdapterTempo = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaTempo);
        arrayAdapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTempo.setAdapter(arrayAdapterTempo);
    }

    public void endereco(){
        Preferencias preferencias = new Preferencias(CadastroTarefa.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase();
        firebase = firebase.child("endereco");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Endereco endereco = dados.getValue(Endereco.class);

                    if(identificadorUsuarioLogado.equals(endereco.getId_usuario())){
                        String end = endereco.getRua().toString()+", "+endereco.getNumero().toString();
                        listaEndereco.add(end);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> arrayAdapterEndereco = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaEndereco);
        arrayAdapterEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndereco.setAdapter(arrayAdapterEndereco);
    }
}
