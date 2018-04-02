package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Avaliacao;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CriarAvaliacao extends AppCompatActivity {

    private Button buttonEnviar;
    private RatingBar ratingBar;
    private EditText editTextDescricao;

    private String idTarefa;
    public String id_ProcessoTarefa;

    private DatabaseReference firebase;

    private Avaliacao avaliacao = new Avaliacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");

        buttonEnviar = (Button) findViewById(R.id.buttonEnviar);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
            }
        });

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarAvaliacao();

                Intent intent = new Intent(CriarAvaliacao.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void enviarAvaliacao()
    {
        Preferencias preferencias = new Preferencias(CriarAvaliacao.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();


        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);
                    if(id_ProcessoTarefa.equals(processoTarefa.getId())){
                        avaliacao.setId_usuario_emissor(processoTarefa.getId_usuario_emissor());
                        avaliacao.setId_usuario_realizador(processoTarefa.getId_usuario_realizador());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    float avaliacaoAtual = Float.parseFloat(usuario.getAvaliacao());
                    int numeroAvaliacoes = Integer.parseInt(usuario.getNumeroAvaliacoes());

                    if(numeroAvaliacoes == 0){
                        numeroAvaliacoes++;
                        firebase.child(identificadorUsuarioLogado).child("avaliacao").setValue(String.valueOf(ratingBar.getRating()));
                        firebase.child(identificadorUsuarioLogado).child("numeroAvaliacoes").setValue(String.valueOf(numeroAvaliacoes));
                    }
                    else{
                        numeroAvaliacoes++;
                        avaliacaoAtual = (avaliacaoAtual + ratingBar.getRating())/numeroAvaliacoes;
                        firebase.child(identificadorUsuarioLogado).child("avaliacaoAtual").setValue(String.valueOf(avaliacaoAtual));
                    }
                }
                String nota = String.valueOf(ratingBar.getRating());
                String descricao = editTextDescricao.getText().toString();
                Toast.makeText(CriarAvaliacao.this, "Avaliado com sucesso!", Toast.LENGTH_SHORT).show();
                avaliacao.salvar(idTarefa, nota, descricao, id_ProcessoTarefa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
