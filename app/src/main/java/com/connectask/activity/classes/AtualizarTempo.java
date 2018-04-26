package com.connectask.activity.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.connectask.activity.activity.Home;
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

    private int count = 0;

    private Context context;
    private String id_tarefa;
    private String id_usuario;
    private String status;
    private String data;
    private String hora;
    private String tempo;

    public void atualiza(Context context, String id_tarefa, String id_usuario, String status, String data, String hora, String tempo)
    {
        this.context = context;
        this.id_tarefa = id_tarefa = id_tarefa;
        this.id_usuario = id_usuario;
        this.status = status;
        this.data = data;
        this.hora = hora;
        this.tempo = tempo;

        if(status.equals("1") )
        {
            firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

            calculo();
        }
    }

    private void calculo(){
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(new Date());

        int mesAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.MONTH) + 1)));
        int mes = Integer.parseInt(data.substring(3, 5));

        int diaAtual = Integer.parseInt(String.valueOf((calendario.get(Calendar.DAY_OF_MONTH))));
        int dia = Integer.parseInt(data.substring(0, 2));

        String novaData = ((String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) : String.valueOf(calendario.get(Calendar.DAY_OF_MONTH))) + "-" + ((String.valueOf(calendario.get(Calendar.MONTH)+1)).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MONTH)+1)) : String.valueOf(calendario.get(Calendar.MONTH)+1)) +"-"+String.valueOf(calendario.get(Calendar.YEAR));
        String novaHora = ((String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) : String.valueOf(calendario.get(Calendar.HOUR_OF_DAY))) + ":" + ((String.valueOf(calendario.get(Calendar.MINUTE))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.MINUTE))) : String.valueOf(calendario.get(Calendar.MINUTE))) + ":" + ((String.valueOf(calendario.get(Calendar.SECOND))).length() < 2 ? "0"+(String.valueOf(calendario.get(Calendar.SECOND))) : String.valueOf(calendario.get(Calendar.SECOND)));

        if((mes - mesAtual) == 0) {
            diferencaDia = (diaAtual - dia) * 24;
        }
        else if ((mes - mesAtual) == 1){
            diferencaDia = (diaAtual - (dia+30)) * 24;
        }

        int horario = (Integer.parseInt(String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)))+diferencaDia) - Integer.parseInt(hora.substring(0, 2));
        horario = Integer.parseInt(tempo) - horario;

        if(horario <= 0){
            Preferencias preferencias = new Preferencias(context);
            final String identificadorUsuarioLogado = preferencias.getIdentificado();

            if(identificadorUsuarioLogado.equals(id_usuario)){
                tarefaCancelada();
            }
            firebase.child(id_tarefa).child("tempo").setValue("0");
            firebase.child(id_tarefa).child("data").setValue(novaData);
            firebase.child(id_tarefa).child("hora").setValue(novaHora);
            firebase.child(id_tarefa).child("status").setValue("3");
        }
        else{
            firebase.child(id_tarefa).child("tempo").setValue(String.valueOf(horario));
            firebase.child(id_tarefa).child("data").setValue(novaData);
            firebase.child(id_tarefa).child("hora").setValue(novaHora);
        }

    }

    private void tarefaCancelada(){
        new AlertDialog.Builder(context)
                .setTitle("Tarefa Cancelada")
                .setMessage("\nInfelizmente ninguém realizou sua tarefa no tempo determinado, portanto ela foi cancelada.\n")
                .setPositiveButton("OK", null)
                .show();
    }
}
