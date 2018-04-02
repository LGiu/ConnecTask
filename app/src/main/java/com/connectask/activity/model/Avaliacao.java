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
    private String id_usuario_realizador;
    private String notaAvaliancao;
    private String avaliacao;
    private String id_processoTarefa;

    private String idProcessoTarefa;
    private String idTarefa;

    private DatabaseReference firebase;

    public void salvar(String idT, String nota, String descricao, String idProcessoT) {
        this.idProcessoTarefa = idProcessoT;
        this.idTarefa = idT;

        firebase = ConfiguracaoFirebase.getFirebase();

        setId(firebase.child("avaliacao").push().getKey());
        setId_tarefa(idTarefa);


        setNotaAvaliancao(nota);
        setAvaliacao(descricao);
        setId_processoTarefa(idProcessoTarefa);

        firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("avaliacao").child(getId()).setValue(this);

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

    public String getId_usuario_realizador() {
        return id_usuario_realizador;
    }

    public void setId_usuario_realizador(String id_usuario_realizador) {
        this.id_usuario_realizador = id_usuario_realizador;
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

    public String getId_processoTarefa() {
        return id_processoTarefa;
    }

    public void setId_processoTarefa(String id_processoTarefa) {
        this.id_processoTarefa = id_processoTarefa;
    }
}
