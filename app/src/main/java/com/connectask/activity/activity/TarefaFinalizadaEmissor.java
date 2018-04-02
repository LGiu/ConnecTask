package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.connectask.R;
import com.connectask.activity.classes.Util;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class TarefaFinalizadaEmissor extends AppCompatActivity {

    private Button buttonAvaliar;

    private String idTarefa;
    public String id_ProcessoTarefa;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_finalizada_emissor);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");


        buttonAvaliar = (Button) findViewById(R.id.buttonAvaliar);

        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebase = ConfiguracaoFirebase.getFirebase();
                firebase.child("tarefas").child(idTarefa).child("status").setValue("5");

                firebase = ConfiguracaoFirebase.getFirebase();
                firebase.child("ProcessoTarefa").child(id_ProcessoTarefa).child("ativo").setValue("2");

                Intent intent = new Intent(TarefaFinalizadaEmissor.this, CriarAvaliacao.class);
                intent.putExtra("id", idTarefa);
                intent.putExtra("id_ProcessoTarefa", id_ProcessoTarefa);
                startActivity(intent);
            }
        });

    }
}
