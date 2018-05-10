package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.connectask.R;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class TarefaFinalizaRealizador extends AppCompatActivity {

    private Button buttonAvaliar;

    private String idTarefa;
    public String id_ProcessoTarefa;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_finaliza_realizador);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");

        firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("ProcessoTarefa").child(id_ProcessoTarefa).child("ativoRealizador").setValue("2");

        buttonAvaliar = (Button) findViewById(R.id.buttonAvaliar);

        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TarefaFinalizaRealizador.this, CriarAvaliacao.class);
                intent.putExtra("id", idTarefa);
                intent.putExtra("id_ProcessoTarefa", id_ProcessoTarefa);
                startActivity(intent);
            }
        });
    }
}
