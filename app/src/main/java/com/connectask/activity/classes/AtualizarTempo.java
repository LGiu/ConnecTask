package com.connectask.activity.classes;

import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class AtualizarTempo {

    private DatabaseReference firebase;
    private Tarefa tarefa;

    private int diferencaDia;

    public void atualiza(){

        firebase =ConfiguracaoFirebase.getFirebase().child("tarefas");
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot dataSnapshot){
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        tarefa = dados.getValue(Tarefa.class);
                        calculo();
                    }
                }

                @Override
                public void onCancelled (DatabaseError databaseError){

                }
        });
    }

    private void calculo(){
        if(tarefa.getStatus().equals("1") )
        {
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
