package com.connectask.activity.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Token {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "ConnecTask.token";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_TOKEN = "token";

    public Token(Context contextoParamentro){
        contexto = contextoParamentro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarToken(String token){
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();
    }


    public String getToken(){
        return preferences.getString(CHAVE_TOKEN, null);
    }
}
