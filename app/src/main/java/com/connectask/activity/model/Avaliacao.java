package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.activity.CriarAvaliacao;
import com.connectask.activity.activity.Home;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Leonardo Giuliani on 14/02/2018.
 */

public class Avaliacao {

    private String id;
    private String id_tarefa;
    private String id_usuario_emissor;
    private String id_usuario_realizaor;
    private String notaAvaliancao;
    private String avaliacao;

    private DatabaseReference firebase;

    public void salvar(String idTarefa, String nota, String descricao, String idProcessoTarefa) {
        firebase = ConfiguracaoFirebase.getFirebase();

        setId(firebase.child("avaliacao").push().getKey());
        setId_tarefa(idTarefa);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("tarefa")
                .child(idTarefa);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);
                    setId_usuario_emissor(tarefa.getId_usuario());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("ProcessoTarefa")
                .child(idProcessoTarefa);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);
                    setId_usuario_realizaor(processoTarefa.getId_usuario_realizador());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setNotaAvaliancao(nota);
        setAvaliacao(descricao);

        firebase.child("avaliacao")
                .child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_tarefa() {
        return id_tarefa;
    }

    public void setId_tarefa(String id_tarefa) {
        this.id_tarefa = id_tarefa;
    }

    public String getId_usuario_emissor() {
        return id_usuario_emissor;
    }

    public void setId_usuario_emissor(String id_usuario_emissor) {
        this.id_usuario_emissor = id_usuario_emissor;
    }

    public String getId_usuario_realizaor() {
        return id_usuario_realizaor;
    }

    public void setId_usuario_realizaor(String id_usuario_realizaor) {
        this.id_usuario_realizaor = id_usuario_realizaor;
    }

    public String getNotaAvaliancao() {
        return notaAvaliancao;
    }

    public void setNotaAvaliancao(String notaAvaliancao) {
        this.notaAvaliancao = notaAvaliancao;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }
}
