package com.connectask.activity.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Celular {
    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "ConnecTask.telefone";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_TOKEN = "token";
    private final String CHAVE_TELEFONE = "telefone";

    public Celular(Context contextoParamentro){
        contexto = contextoParamentro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarTelefone(String telefone){
        editor.putString(CHAVE_TELEFONE, telefone);
        editor.commit();
    }

    public String getTelefone(){
        return preferences.getString(CHAVE_TELEFONE, null);
    }
}
