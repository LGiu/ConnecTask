package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leonardo Giuliani on 16/03/2018.
 */

public class Sessao {
    private String id;
    private String id_usuario;
    private String data;
    private String hora;

    private DatabaseReference firebase;
    private Context contexto;


    public void salvar(Context contextoParamentro){
        contexto = contextoParamentro;

        Preferencias preferencias = new Preferencias(contexto);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();
        setId_usuario(identificadorUsuarioLogado);

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
        setId(firebase.child("sessao").push().getKey());

        firebase.child("sessao")
                .child(getId_usuario())
                .child(getId()).setValue(this);
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
