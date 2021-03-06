package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.activity.Home;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Leonardo Giuliani on 21/12/2017.
 */

public class Tarefa implements Serializable {

    private String id;
    private String id_usuario;
    private String titulo;
    private String tipo;
    private String descricao;
    private String tempo;
    private String tempoCadastro;
    private String valor;
    private String id_endereco;
    private String dataCadastro;
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
        setDataCadastro(formataData.format(dataAtual));

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
        //5-Concluída

        //Pegar id único
        setId(firebase.child("tarefas").push().getKey());

        firebase.child("tarefas")
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
        return valor.replace(".", ",");
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

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getTempoCadastro() {
        return tempoCadastro;
    }

    public void setTempoCadastro(String tempoCadastro) {
        this.tempoCadastro = tempoCadastro;
    }
}
