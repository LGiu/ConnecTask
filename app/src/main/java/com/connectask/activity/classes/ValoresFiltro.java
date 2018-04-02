package com.connectask.activity.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Leonardo Giuliani on 02/01/2018.
 */

public class ValoresFiltro {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "ConnecTask.ValoresFiltro";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CATEGORIA = "categoria";
    private final String LOCALIZACAO = "localizacao";
    private final String VALOR = "valor";
    private final String TEMPO = "tempo";


    public ValoresFiltro(Context contextoParamentro){
        contexto = contextoParamentro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String categoria, int localizacao, int valor, int tempo){
        editor.putString(CATEGORIA, categoria);
        editor.putInt(LOCALIZACAO, localizacao);
        editor.putInt(VALOR, valor);
        editor.putInt(TEMPO, tempo);
        editor.commit();
    }

    public void limparDados(){
        editor.putString(CATEGORIA, "");
        editor.putInt(LOCALIZACAO, 15);
        editor.putInt(VALOR, 500);
        editor.putInt(TEMPO, 96);
        editor.commit();
    }

    public String getCategoria(){
        return preferences.getString(CATEGORIA, "");
    }

    public int getLocalizacao(){
        return preferences.getInt(LOCALIZACAO, 15);
    }

    public int getValor(){
        return preferences.getInt(VALOR, 500);
    }

    public int getTempo(){
        return preferences.getInt(TEMPO, 96);
    }
}
