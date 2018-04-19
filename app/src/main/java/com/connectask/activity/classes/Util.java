package com.connectask.activity.classes;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.connectask.activity.activity.CriarAvaliacao;
import com.connectask.activity.activity.Home;
import com.connectask.activity.activity.TarefaFinalizaRealizador;
import com.connectask.activity.activity.TarefaFinalizadaEmissor;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Avaliacao;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonardo Giuliani on 28/03/2018.
 */

public class Util {
    private DatabaseReference firebase;

    private String idTarefa = "";
    private String idProcesso = "";
    private boolean controle = false;

    public String idProcessoTarefa(Context context, String idT){
        idTarefa = idT;
        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if (idTarefa.equals(tarefa.getId())){

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if ((identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_emissor()) || identificadorUsuarioLogado.equals(processoTarefa.getId_usuario_realizador())) && processoTarefa.getAtivoEmissor().equals("1") && processoTarefa.getAtivoRealizador().equals("1")){
                        idProcesso = processoTarefa.getId();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return idProcesso;

    }

    public boolean isPasswordValid (String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
