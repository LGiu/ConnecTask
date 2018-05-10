package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class CriarAvaliacao extends AppCompatActivity {

    private Button buttonEnviar;
    private RatingBar ratingBar;
    private EditText editTextDescricao;

    private String idTarefa;
    public String id_ProcessoTarefa;

    public String nota;
    public String descricao;

    private DatabaseReference firebase;

    private boolean sairLoop = true;

    private Avaliacao avaliacao = new Avaliacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        Intent intent = getIntent();
        idTarefa = intent.getStringExtra("id");
        id_ProcessoTarefa = intent.getStringExtra("id_ProcessoTarefa");


        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);
                    if(id_ProcessoTarefa.equals(processoTarefa.getId()) && (processoTarefa.getAtivoEmissor().equals("2") && processoTarefa.getAtivoRealizador().equals("2"))){
                        firebase = ConfiguracaoFirebase.getFirebase();
                        firebase.child("tarefas").child(idTarefa).child("status").setValue("5");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        sairLoop = true;

        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);
                    if(id_ProcessoTarefa.equals(processoTarefa.getId()) && sairLoop){
                        avaliacao.setId_usuario_emissor(identificadorUsuarioLogado);
                        if(identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_emissor())){
                            avaliacao.setId_usuario_realizador(processoTarefa.getId_usuario_emissor());
                        }
                        else if(identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_realizador())){
                            avaliacao.setId_usuario_realizador(processoTarefa.getId_usuario_realizador());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail().toString()))){
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
                        nota = String.valueOf(ratingBar.getRating());
                        descricao = editTextDescricao.getText().toString();
                        salvar();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void salvar(){
        Toast.makeText(CriarAvaliacao.this, "Avaliado com sucesso!", Toast.LENGTH_SHORT).show();
        avaliacao.salvar(idTarefa, nota, descricao, id_ProcessoTarefa);
    }

}
