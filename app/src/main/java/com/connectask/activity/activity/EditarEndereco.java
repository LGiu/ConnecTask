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
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Util;
import com.connectask.activity.model.*;
import com.connectask.activity.model.Endereco;

import java.util.ArrayList;
import java.util.List;

public class EditarEndereco extends AppCompatActivity {

    private EditText editTextCep;
    private EditText editTextRua;
    private EditText editTextNumero;
    private EditText editTextBairro;
    private EditText editTextComplemento;
    private EditText editTextCidade;
    private Spinner spinnerEstado;
    private Button buttonCadastro;

    private String msg = "Campos incorretos:";

    private List<String> listaEstado = new ArrayList<String>();

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_endereco);

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

        editTextCep = (EditText) findViewById(R.id.editTextCep);
        editTextRua = (EditText) findViewById(R.id.editTextNome);
        editTextNumero = (EditText) findViewById(R.id.editTextNumero);
        editTextBairro = (EditText) findViewById(R.id.editTextBairro);
        editTextComplemento = (EditText) findViewById(R.id.editTextComplemento);
        editTextCidade = (EditText) findViewById(R.id.editTextCidade);
        spinnerEstado = (Spinner) findViewById(R.id.spinnerEstado);
        buttonCadastro = (Button) findViewById(R.id.buttonFinalizar);

        Intent intent = getIntent();
        editTextCep.setText(intent.getStringExtra("cep"));
        editTextRua.setText(intent.getStringExtra("rua"));
        editTextNumero.setText(intent.getStringExtra("numero"));
        editTextBairro.setText(intent.getStringExtra("bairro"));
        editTextComplemento.setText(intent.getStringExtra("complemento"));
        editTextCidade.setText(intent.getStringExtra("cidade"));
        id = intent.getStringExtra("id");

        estado();

        for(int i= 0; i < spinnerEstado.getAdapter().getCount(); i++)
        {
            if(spinnerEstado.getAdapter().getItem(i).toString().contains(intent.getStringExtra("estado")))
            {
                spinnerEstado.setSelection(i);
            }
        }

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias preferencias = new Preferencias(EditarEndereco.this);
                final String identificadorUsuarioLogado = preferencias.getIdentificado();

                Endereco endereco = new com.connectask.activity.model.Endereco();
                endereco.setId(id);
                endereco.setId_usuario(id);
                endereco.setEstado(spinnerEstado.getSelectedItem().toString());
                endereco.setCidade(editTextCidade.getText().toString());
                endereco.setCep(editTextCep.getText().toString());
                endereco.setRua(editTextRua.getText().toString());
                endereco.setNumero(editTextNumero.getText().toString());
                endereco.setBairro(editTextBairro.getText().toString());
                endereco.setComplemento((editTextComplemento.getText().toString()));

                endereco.update(EditarEndereco.this);

                Intent intent = new Intent(EditarEndereco.this, com.connectask.activity.activity.Endereco.class);
                startActivity(intent);
            }
        });
    }

    private void estado(){
        listaEstado.add("AC");
        listaEstado.add("AL");
        listaEstado.add("AM");
        listaEstado.add("AP");
        listaEstado.add("BA");
        listaEstado.add("CE");
        listaEstado.add("DF");
        listaEstado.add("ES");
        listaEstado.add("GO");
        listaEstado.add("MA");
        listaEstado.add("MG");
        listaEstado.add("MS");
        listaEstado.add("MT");
        listaEstado.add("PA");
        listaEstado.add("PB");
        listaEstado.add("PE");
        listaEstado.add("PI");
        listaEstado.add("PR");
        listaEstado.add("RJ");
        listaEstado.add("RN");
        listaEstado.add("RO");
        listaEstado.add("RR");
        listaEstado.add("RS");
        listaEstado.add("SC");
        listaEstado.add("SE");
        listaEstado.add("SP");
        listaEstado.add("TO");

        ArrayAdapter<String> arrayAdapterEstado = new ArrayAdapter<String>(EditarEndereco.this, android.R.layout.simple_list_item_1, listaEstado);
        arrayAdapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(arrayAdapterEstado);
    }

    private boolean valida(){
        boolean teste = true;
        msg = "Campos incorretos:";
        Util util = new Util();

        if(editTextCep.getText().length() != 8)
        {
            msg += "\nCep inválida.";
            teste = false;
        }
        if(editTextCidade.getText().length() > 100 || editTextCidade.getText().length() == 0)
        {
            msg += "\nCidade inválido.";
            teste = false;
        }
        if(editTextRua.getText().length() > 255 || editTextCidade.getText().length() == 0)
        {
            msg += "\nRua Inválida.";
            teste = false;
        }
        if(editTextBairro.getText().length() > 70 || editTextBairro.getText().length() == 0)
        {
            msg += "\nBairro inválido.";
            teste = false;
        }
        if(editTextNumero.getText().length() > 7 || editTextNumero.getText().length() == 0)
        {
            msg += "\nNúmero inválido.";
            teste = false;
        }
        if(editTextNumero.getText().length() > 70)
        {
            msg += "\nNúmero inválido.";
            teste = false;
        }

        return teste;
    }
}
