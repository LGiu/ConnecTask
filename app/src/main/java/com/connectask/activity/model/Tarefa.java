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
import java.util.ArrayList;
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

    private int diferencaDia;

    private DatabaseReference firebase;
    private Context contexto;

    private ArrayList<String> tarefaId = new ArrayList<>();

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

    public void atualizarTempo()
    {
        tarefaId = new ArrayList<String>();

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(tarefaId.size() > 0){
                        for(String idTarefa : tarefaId){
                            if(!tarefa.getId().equals(idTarefa)){
                                if(tarefa.getStatus().equals("1") )
                                {
                                    tarefaId.add(tarefa.getId());

                                    Calendar calendario = Calendar.getInstance();
                                    calendario.setTime(new Date());

                                    int mesAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.MONTH) + 1)));
                                    int mes = Integer.parseInt(tarefa.getData().toString().substring(3, 5));

                                    int diaAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.DAY_OF_MONTH))));
                                    int dia = Integer.parseInt(tarefa.getData().toString().substring(0, 2));

                                    String novaData = ((String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) + "-" + ((String.valueOf(calendario.get(Calendar.MONTH)+1)).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MONTH)+1)) : String.valueOf(calendario.get(Calendar.MONTH)+1)) +"-"+String.valueOf(calendario.get(Calendar.YEAR));
                                    String novaHora = ((String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) + ":" + ((String.valueOf(calendario.get(Calendar.MINUTE))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MINUTE))) : String.valueOf(calendario.get(Calendar.MINUTE))) + ":" + ((String.valueOf(calendario.get(Calendar.SECOND))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.SECOND))) : String.valueOf(calendario.get(Calendar.SECOND)));

                                    if((mes - mesAtual) == 0) {
                                        diferencaDia = (diaAtual - dia) * 24;
                                    }
                                    else if ((mes - mesAtual) == 1){
                                        diferencaDia = (diaAtual - (dia+30)) * 24;
                                    }

                                    int hora = (Integer.parseInt(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)))+diferencaDia) - Integer.parseInt(tarefa.getHora().substring(0, 2));
                                    hora = Integer.parseInt(tarefa.getTempo()) - hora;

                                    if(hora <= 0){
                                        firebase.child(tarefa.getId()).child("tempo").setValue("0");
                                        firebase.child(tarefa.getId()).child("data").setValue(novaData);
                                        firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
                                        firebase.child(tarefa.getId()).child("status").setValue("3");
                                    }
                                    else{
                                        firebase.child(tarefa.getId()).child("tempo").setValue(String.valueOf(hora));
                                        firebase.child(tarefa.getId()).child("data").setValue(novaData);
                                        firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(tarefa.getStatus().equals("1") )
                        {
                            tarefaId.add(tarefa.getId());

                            Calendar calendario = Calendar.getInstance();
                            calendario.setTime(new Date());

                            int mesAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.MONTH) + 1)));
                            int mes = Integer.parseInt(tarefa.getData().toString().substring(3, 5));

                            int diaAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.DAY_OF_MONTH))));
                            int dia = Integer.parseInt(tarefa.getData().toString().substring(0, 2));

                            String novaData = ((String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) + "-" + ((String.valueOf(calendario.get(Calendar.MONTH)+1)).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MONTH)+1)) : String.valueOf(calendario.get(Calendar.MONTH)+1)) +"-"+String.valueOf(calendario.get(Calendar.YEAR));
                            String novaHora = ((String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) + ":" + ((String.valueOf(calendario.get(Calendar.MINUTE))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MINUTE))) : String.valueOf(calendario.get(Calendar.MINUTE))) + ":" + ((String.valueOf(calendario.get(Calendar.SECOND))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.SECOND))) : String.valueOf(calendario.get(Calendar.SECOND)));

                            if((mes - mesAtual) == 0) {
                                diferencaDia = (diaAtual - dia) * 24;
                            }
                            else if ((mes - mesAtual) == 1){
                                diferencaDia = (diaAtual - (dia+30)) * 24;
                            }

                            int hora = (Integer.parseInt(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)))+diferencaDia) - Integer.parseInt(tarefa.getHora().substring(0, 2));
                            hora = Integer.parseInt(tarefa.getTempo()) - hora;

                            if(hora <= 0){
                                firebase.child(tarefa.getId()).child("tempo").setValue("0");
                                firebase.child(tarefa.getId()).child("status").setValue("3");
                                firebase.child(tarefa.getId()).child("data").setValue(novaData);
                                firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
                            }
                            else{
                                firebase.child(tarefa.getId()).child("tempo").setValue(String.valueOf(hora));
                                firebase.child(tarefa.getId()).child("data").setValue(novaData);
                                firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
                            }
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
