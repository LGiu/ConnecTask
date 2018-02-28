package com.connectask.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {

    private DatabaseReference firebase;

    //private ImageButton imageViewFoto;
    private TextView textViewNome;
    private TextView textViewEmail;
    private TextView textViewSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewSenha = (TextView) findViewById(R.id.textViewSenha);

        Preferencias preferencias = new Preferencias(Perfil.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios")
                .child(identificadorUsuarioLogado);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    textViewNome.setText(usuario.getNome());
                    textViewEmail.setText(usuario.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textViewNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        textViewSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
