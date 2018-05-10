package com.connectask.activity.classes;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class AtualizarTempo {

    private DatabaseReference firebase;
    private DatabaseReference firebaseProcesso;
    private Tarefa tarefa;

    private int diferencaDia;

    private Context context;
    private String id_tarefa;
    private String id_usuario;
    private String status;
    private String data;
    private String hora;
    private String tempo;

    public void setContext(Context context){
        this.context = context;
    }

    public void atualiza()
    {
        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    tarefa = dados.getValue(Tarefa.class);

                    if(tarefa.getStatus().equals("1") )
                    {
                        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

                        calculo();
                    }
                    else if(tarefa.getStatus().equals("2") )
                    {
                        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

                        calculo2();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void calculo(){

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        int mesAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.MONTH) + 1)));
        int mes = Integer.parseInt(tarefa.getData().substring(3, 5));

        int diaAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.DAY_OF_MONTH))));
        int dia = Integer.parseInt(tarefa.getData().substring(0, 2));

        String novaData = ((String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) + "-" + ((String.valueOf(calendario.get(Calendar.MONTH)+1)).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MONTH)+1)) : String.valueOf(calendario.get(Calendar.MONTH)+1)) +"-"+String.valueOf(calendario.get(Calendar.YEAR));
        String novaHora = ((String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) + ":" + ((String.valueOf(calendario.get(Calendar.MINUTE))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MINUTE))) : String.valueOf(calendario.get(Calendar.MINUTE))) + ":" + ((String.valueOf(calendario.get(Calendar.SECOND))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.SECOND))) : String.valueOf(calendario.get(Calendar.SECOND)));

        novaHora = novaHora.substring(0, 5);

        if((mes - mesAtual) == 0) {
            diferencaDia = (diaAtual - dia) * 24;
        }
        else if ((mes - mesAtual) == 1){
            diferencaDia = (diaAtual - (dia+30)) * 24;
        }

        int horario = (Integer.parseInt(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)))+diferencaDia) - Integer.parseInt(tarefa.getHora().substring(0, 2));
        horario = Integer.parseInt(tarefa.getTempo()) - horario;

        if(horario <= 0){
            Preferencias preferencias = new Preferencias(context);
            final String identificadorUsuarioLogado = preferencias.getIdentificado();

            if(identificadorUsuarioLogado.equals(tarefa.getId_usuario())){
                tarefaCancelada();
            }


            firebase.child(tarefa.getId()).child("tempo").setValue("0");
            firebase.child(tarefa.getId()).child("data").setValue(novaData);
            firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
            firebase.child(tarefa.getId()).child("status").setValue("3");
        }
        else{
            firebase.child(tarefa.getId()).child("tempo").setValue(String.valueOf(horario));
            firebase.child(tarefa.getId()).child("data").setValue(novaData);
            firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
        }

    }

    private void calculo2(){

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        int mesAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.MONTH) + 1)));
        int mes = Integer.parseInt(tarefa.getData().substring(3, 5));

        int diaAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.DAY_OF_MONTH))));
        int dia = Integer.parseInt(tarefa.getData().substring(0, 2));

        String novaData = ((String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) + "-" + ((String.valueOf(calendario.get(Calendar.MONTH)+1)).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MONTH)+1)) : String.valueOf(calendario.get(Calendar.MONTH)+1)) +"-"+String.valueOf(calendario.get(Calendar.YEAR));
        String novaHora = ((String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) + ":" + ((String.valueOf(calendario.get(Calendar.MINUTE))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MINUTE))) : String.valueOf(calendario.get(Calendar.MINUTE))) + ":" + ((String.valueOf(calendario.get(Calendar.SECOND))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.SECOND))) : String.valueOf(calendario.get(Calendar.SECOND)));

        novaHora = novaHora.substring(0, 5);

        if((mes - mesAtual) == 0) {
            diferencaDia = (diaAtual - dia) * 24;
        }
        else if ((mes - mesAtual) == 1){
            diferencaDia = (diaAtual - (dia+30)) * 24;
        }

        int horario = (Integer.parseInt(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)))+diferencaDia) - Integer.parseInt(tarefa.getHora().substring(0, 2));
        horario = Integer.parseInt(tarefa.getTempoCadastro()) - horario;

        if(horario <= 0){
            Preferencias preferencias = new Preferencias(context);
            final String identificadorUsuarioLogado = preferencias.getIdentificado();

            if(identificadorUsuarioLogado.equals(tarefa.getId_usuario())){
                tarefaCancelada2();
            }

            if(identificadorUsuarioLogado.equals(tarefa.getId_usuario()) && tarefa.getStatus().equals("2")){
                processoTar();
            }

            firebase.child(tarefa.getId()).child("tempoCadastro").setValue("0");
            firebase.child(tarefa.getId()).child("data").setValue(novaData);
            firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
            firebase.child(tarefa.getId()).child("status").setValue("3");
        }
        else{
            firebase.child(tarefa.getId()).child("tempoCadastro").setValue(String.valueOf(horario));
            firebase.child(tarefa.getId()).child("data").setValue(novaData);
            firebase.child(tarefa.getId()).child("hora").setValue(novaHora);
        }

    }

    private void processoTar()
    {
        firebaseProcesso = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebaseProcesso.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if(tarefa.getId().equals(processoTarefa.getId()) )
                    {
                        firebaseProcesso = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");
                        firebaseProcesso.child(processoTarefa.getId()).child("ativoEmissor").setValue("2");                        firebaseProcesso.child(processoTarefa.getId()).child("ativoEmissor").setValue("2");
                        firebaseProcesso.child(processoTarefa.getId()).child("ativoRealizador").setValue("2");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void tarefaCancelada(){
        new AlertDialog.Builder(context)
                .setTitle("Tarefa Cancelada")
                .setMessage("\nInfelizmente nenhum usuário realizou sua tarefa no tempo determinado, portanto ela foi cancelada.\n")
                .setPositiveButton("OK", null)
                .show();
    }

    private void tarefaCancelada2(){
        new AlertDialog.Builder(context)
                .setTitle("Tarefa Cancelada")
                .setMessage("\nInfelizmente o usuário não realizou sua tarefa no tempo determinado, portanto ela foi cancelada.\n")
                .setPositiveButton("OK", null)
                .show();
    }
}
