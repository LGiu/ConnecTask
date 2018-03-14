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

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

/**
 * Created by Leonardo Giuliani on 17/01/2018.
 */

public class ProcessoTarefa {

    private DatabaseReference firebase;

    private Context context;

    private String id;
    private String id_tarefa;
    private String id_usuario_realizador;
    private String id_usuario_emissor;
    private String data;
    private String hora;

    public ProcessoTarefa(Context contextoParamentro){
        context = contextoParamentro;
    }

    public void salvar() {

        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        setId_usuario_realizador(identificadorUsuarioLogado);

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    final Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(tarefa.getId().toString().equals(getId_tarefa()))
                    {
                        setId_usuario_emissor(tarefa.getId_usuario());
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

        firebase.child("ProcessoTarefa").child(getId()).setValue(this);

        firebase = ConfiguracaoFirebase.getFirebase();

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
