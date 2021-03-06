package com.connectask.activity.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Leonardo Giuliani on 02/01/2018.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "ConnecTask.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    public Preferencias(Context contextoParamentro){
        contexto = contextoParamentro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.commit();
    }

    public void limpaDados(){
        editor.putString(CHAVE_IDENTIFICADOR, "");
        editor.commit();
    }

    public String getIdentificado(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

}
