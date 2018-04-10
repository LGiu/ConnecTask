package com.connectask.activity.model;

import android.content.Context;

import com.connectask.activity.activity.Home;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.classes.Preferencias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Period;
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
        //5-Concluída

        //Pegar id único
        setId(firebase.child("tarefas").push().getKey());

        firebase.child("tarefas")
                .child(getId()).setValue(this);

    }

    public void atualizarTempo(){

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(tarefa.getStatus().equals("1"))
                    {
                        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                        Date dataAtual = new Date();

                        SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        Date horaAtual = new Date();
                        calendar.setTime(horaAtual);
                        horaAtual = calendar.getTime();

                        String dataFormatada = formataData.format(dataAtual);
                        dataFormatada = dataFormatada.replace("-","").substring(2, 2);
                        int mesAtual = Integer.parseInt(dataFormatada);
                        int mes = Integer.parseInt(tarefa.getData().toString().substring(3, 2));

                        if((mesAtual - mes) == 0){
                            int hora = (Integer.parseInt(tarefa.getTempo().substring(0, 2)) - Integer.parseInt(String.valueOf(horaAtual).substring(0, 2)));
                            hora = Integer.parseInt(tarefa.getTempo()) - hora;

                            if(hora <= 0){
                                firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                                firebase.child("tarefas").child(tarefa.getId()).child("status").setValue("3");
                            }
                            else{
                                firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                            }
                        }
                        else if((mesAtual - mes) == 1){
                            int hora = ((Integer.parseInt(String.valueOf(horaAtual).substring(0, 2))+30) - Integer.parseInt(tarefa.getTempo().substring(0, 2)));
                            hora = Integer.parseInt(tarefa.getTempo()) - hora;

                            if(hora <= 0){
                                firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                                firebase.child("tarefas").child(tarefa.getId()).child("status").setValue("3");
                            }
                            else{
                                firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                            }
                        }


                        int hora = (Integer.parseInt(tarefa.getTempo().substring(0, 2)) - Integer.parseInt(String.valueOf(horaAtual).substring(0, 2)));
                        hora = Integer.parseInt(tarefa.getTempo()) - hora;

                        if(hora <= 0){
                            firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                            firebase.child("tarefas").child(tarefa.getId()).child("status").setValue("3");
                        }
                        else{
                            firebase.child("tarefas").child(tarefa.getId()).child("tempo").setValue(hora);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
