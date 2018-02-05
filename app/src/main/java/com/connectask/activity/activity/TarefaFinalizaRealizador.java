package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.connectask.R;

public class TarefaFinalizaRealizador extends AppCompatActivity {

    private Button buttonAvaliar;

    private String idTarefa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_finaliza_realizador);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");

        buttonAvaliar = (Button) findViewById(R.id.buttonAvaliar);

        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarefaFinalizaRealizador.this, Avaliacao.class);
                intent.putExtra("id", idTarefa);
                startActivity(intent);
            }
        });
    }
}
