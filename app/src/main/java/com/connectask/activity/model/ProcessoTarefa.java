package com.connectask.activity.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.connectask.activity.activity.DetalhesTarefa;
import com.connectask.activity.activity.ProcessoTarefaEmissor;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leonardo Giuliani on 17/01/2018.
 */

public class ProcessoTarefa {

    private DatabaseReference firebase;
    private Context context;
    private String idTarefa;

    private String id;
    private String id_tarefa;
    private String id_usuario_realizador;
    private String id_usuario_emissor;
    private String nome_realizador;
    private String nome_emissor;
    private String data;
    private String hora;

    private String idEmissor;
    private String nome;
    private String id_variavel;

    public ProcessoTarefa(Context contextoParamentro){
        context = contextoParamentro;
    }

    public void salvar(String idParamentro) {

        idTarefa = idParamentro;

        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        setId_usuario_realizador(identificadorUsuarioLogado);
        setId_tarefa(idTarefa);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    id_variavel = tarefa.getId();
                    if(id_variavel.equals(idTarefa)){
                        idEmissor = tarefa.getId_usuario();
                        setId_usuario_emissor(idEmissor);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuario");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    id_variavel = usuario.getId();

                    if(id_variavel.equals(idEmissor)){
                        nome = usuario.getNome();
                        setNome_emissor(nome);
                    }

                    if(id_variavel.equals(identificadorUsuarioLogado)){
                        nome = usuario.getNome();
                        setNome_realizador(nome);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //setar data
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date dataAtual = new Date();
        setData(formataData.format(dataAtual));

        //setar hora
        SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date horaAtual = new Date();
        calendar.setTime(horaAtual);
        horaAtual = calendar.getTime();
        setHora(formataHora.format(horaAtual));


        firebase = ConfiguracaoFirebase.getFirebase();

        //Pegar id único
        setId(firebase.child("ProcessoTarefa").push().getKey());

        firebase.child("ProcessoTarefa")
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

    public String getId_usuario_realizador() {
        return id_usuario_realizador;
    }

    public void setId_usuario_realizador(String id_usuario_realizador) {
        this.id_usuario_realizador = id_usuario_realizador;
    }

    public String getId_usuario_emissor() {
        return id_usuario_emissor;
    }

    public void setId_usuario_emissor(String id_usuario_emissor) {
        this.id_usuario_emissor = id_usuario_emissor;
    }

    public String getNome_realizador() {
        return nome_realizador;
    }

    public void setNome_realizador(String nome_realizador) {
        this.nome_realizador = nome_realizador;
    }

    public String getNome_emissor() {
        return nome_emissor;
    }

    public void setNome_emissor(String nome_emissor) {
        this.nome_emissor = nome_emissor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
