package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.activity.Home;
import com.connectask.activity.classes.Coordenadas;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Leonardo Giuliani on 29/12/2017.
 */

public class Endereco {

    private String id;
    private String estado;
    private String cidade;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String latitude;
    private String longitude;
    private String id_usuario;

    private DatabaseReference firebase;
    private Context contexto;


    public void salvar(Context contextoParamentro){
        contexto = contextoParamentro;

        Preferencias preferencias = new Preferencias(contexto);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        setId_usuario(identificadorUsuarioLogado);

        Coordenadas coordenadas = new Coordenadas(contextoParamentro, estado, cidade, cep, rua, numero);
        setLatitude(String.valueOf(coordenadas.getLatitude()));
        setLongitude(String.valueOf(coordenadas.getLongitude()));

        firebase = ConfiguracaoFirebase.getFirebase();

        //Pegar id único
        setId(firebase.child("endereco").push().getKey());

        firebase.child("endereco")
                .child(getId_usuario())
                .child(getId()).setValue(this);

    }

    public void update(Context contextoParamentro){
        contexto = contextoParamentro;

        Coordenadas coordenadas = new Coordenadas(contextoParamentro, estado, cidade, cep, rua, numero);
        setLatitude(String.valueOf(coordenadas.getLatitude()));
        setLongitude(String.valueOf(coordenadas.getLongitude()));

        firebase.child("endereco").child(getId_usuario()).child(getId()).setValue(this);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
