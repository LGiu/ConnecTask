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
import com.connectask.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CadastroTarefa extends AppCompatActivity {

    private EditText editTextTitulo;
    private Spinner spinnerTipo;
    private EditText editTextDescricao;
    private EditText editTextTempo;
    private EditText editTextValor;
    private Spinner spinnerEndereco;
    private Button buttonCadastro;
    private Button buttonNovoEndereco;
    private List<String> listaTipo = new ArrayList<String>();
    private List<String> listaEndereco = new ArrayList<String>();

    private Tarefa tarefa;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tarefa);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        editTextTitulo = (EditText) findViewById(R.id.editTextRua);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        editTextTempo = (EditText) findViewById(R.id.editTextTempo);
        editTextValor = (EditText) findViewById(R.id.editTextValor);
        spinnerEndereco = (Spinner) findViewById(R.id.spinnerEndereco);
        buttonCadastro = (Button) findViewById(R.id.buttonFinalizar);
        buttonNovoEndereco = (Button) findViewById(R.id.buttonNovoEndereco);

        tipo();
        endereco();

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarefa = new Tarefa();
                tarefa.setTitulo(editTextTitulo.getText().toString());
                tarefa.setTipo(spinnerTipo.getSelectedItem().toString());
                tarefa.setDescricao(editTextDescricao.getText().toString());
                tarefa.setTempo(editTextTempo.getText().toString());
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

    public void endereco(){
        listaEndereco.add("Rua Leopoldo Souza, 1373");

        ArrayAdapter<String> arrayAdapterEndereco = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaEndereco);
        arrayAdapterEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndereco.setAdapter(arrayAdapterEndereco);
    }
}
