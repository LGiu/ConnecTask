package com.connectask.activity.model;

import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leonardo Giuliani on 04/04/2018.
 */

public class Denuncia {
    private String id;
    private String id_usuario_denunciador;
    private String id_usuario_denunciado;
    private String data;
    private String hora;
    private String denuncia;

    private DatabaseReference firebase;

    public void salvar(){
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
        setId(firebase.child("denuncias").push().getKey());

        firebase.child("denuncias").child(getId()).setValue(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_usuario_denunciador() {
        return id_usuario_denunciador;
    }

    public void setId_usuario_denunciador(String id_usuario_denunciador) {
        this.id_usuario_denunciador = id_usuario_denunciador;
    }

    public String getId_usuario_denunciado() {
        return id_usuario_denunciado;
    }

    public void setId_usuario_denunciado(String id_usuario_denunciado) {
        this.id_usuario_denunciado = id_usuario_denunciado;
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

    public String getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(String denuncia) {
        this.denuncia = denuncia;
    }
}
