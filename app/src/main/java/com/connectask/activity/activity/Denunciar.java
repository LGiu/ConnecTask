package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.Denuncia;

public class Denunciar extends AppCompatActivity {

    private TextView textViewNome;
    private EditText editTextDenuncia;
    private Button buttonEnviar;

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denunciar);

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

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        editTextDenuncia = (EditText) findViewById(R.id.editTextDenuncia);
        buttonEnviar = (Button) findViewById(R.id.buttonEnviar);

        Intent intent = getIntent();
        idUsuario = intent.getStringExtra("id");
        textViewNome.setText(intent.getStringExtra("nome"));

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Denuncia denuncia = new Denuncia();
                Preferencias preferencias = new Preferencias(Denunciar.this);
                final String identificadorUsuarioLogado = preferencias.getIdentificado();

                denuncia.setDenuncia(editTextDenuncia.getText().toString());
                denuncia.setId_usuario_denunciador(identificadorUsuarioLogado);
                denuncia.setId_usuario_denunciado(idUsuario);

                denuncia.salvar();
            }
        });


    }
}
