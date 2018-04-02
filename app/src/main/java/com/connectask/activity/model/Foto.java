package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

/**
 * Created by Leonardo Giuliani on 14/03/2018.
 */

public class Foto {

    private DatabaseReference firebase;

    private Context context;

    private String id;
    private String id_usuario;
    private String id_imagem;

    private boolean novo = true;

    public Foto(Context contextoParamentro){
        context = contextoParamentro;
    }

    public void salvar() {

        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();
        setId(identificadorUsuarioLogado);

        firebase = ConfiguracaoFirebase.getFirebase().child("fotos");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuairo = dados.getValue(Usuario.class);

                    if (identificadorUsuarioLogado.equals(codificarBase64(usuairo.getEmail().toString())))
                    {
                        novo = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(novo){
            setId(firebase.child("fotos").push().getKey());

            firebase.child("fotos")
                    .child(getId()).setValue(this);
        }
        else {

        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_imagem() {
        return id_imagem;
    }

    public void setId_imagem(String id_imagem) {
        this.id_imagem = id_imagem;
    }
}
