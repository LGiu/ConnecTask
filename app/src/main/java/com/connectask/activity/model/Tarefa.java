package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.activity.Home;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.classes.Preferencias;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leonardo Giuliani on 21/12/2017.
 */

public class Tarefa {

    private String id;
    private String id_usuario;
    private String titulo;
    private String tipo;
    private String descricao;
    private String tempo;
    private String valor;
    private String id_endereco;
    private String data;
    private String hora;
    private String status;

    private DatabaseReference firebase;
    private Context contexto;


    public Tarefa(){

    }

    public void salvar(Context contextoParamentro){
        contexto = contextoParamentro;

        Home home = new Home();
        Preferencias preferencias = new Preferencias(contexto);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        setId_usuario(identificadorUsuarioLogado);

        firebase = ConfiguracaoFirebase.getFirebase();

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

        //setar status
        setStatus("1");
        //1-Cadastrado
        //2-Em andamento
        //3-Cancelado
        //4-Finalizada


        //Pegar id único
        setId(firebase.child("tarefas").push().getKey());

        firebase.child("tarefas")
                .child(getId()).setValue(this);

        /*firebase = ConfiguracaoFirebase.getFirebase().child("tarefas").child(identificadorUsuarioLogado);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){

                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase.child("tarefas")
                            .child(identificadorUsuarioLogado)
                            .child(getId()).setValue(this);
                }
                else{
                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase.child(getId()).setValue(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getEndereco() {
        return id_endereco;
    }

    public void setEndereco(String endereco) {
        this.id_endereco = endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
