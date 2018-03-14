package com.connectask.activity.model;

import com.connectask.activity.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

/**
 * Created by Leonardo Giuliani on 08/09/2017.
 */

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private String status;
    private String avaliacao;
    private String numeroAvaliacoes;


    public Usuario(){

    }

    public void salvar(String identificadorUsuario){
        setStatus("1");
        //1 - Ativo
        //2 - Inativo
        //3 - Suspenso

        setAvaliacao("0");
        setNumeroAvaliacoes("0");

        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getNumeroAvaliacoes() {
        return numeroAvaliacoes;
    }

    public void setNumeroAvaliacoes(String numeroAvaliacoes) {
        this.numeroAvaliacoes = numeroAvaliacoes;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
