package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.Erro;

public class Report extends AppCompatActivity {

    private EditText editTextTitulo;
    private EditText editTextDescricao;
    private Button buttonEnviar;

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

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

        editTextTitulo = (EditText) findViewById(R.id.editTextTitulo);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        buttonEnviar = (Button) findViewById(R.id.buttonEnviar);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Erro erro = new Erro();
                Preferencias preferencias = new Preferencias(Report.this);
                final String identificadorUsuarioLogado = preferencias.getIdentificado();

                erro.setId_usuario(identificadorUsuarioLogado);
                erro.setTitulo(editTextTitulo.getText().toString());
                erro.setDescricao(editTextDescricao.getText().toString());

                erro.salvar();

                Toast.makeText(Report.this, "Erro reportado com sucesso. Muito Obrigado por contribuir!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Report.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
