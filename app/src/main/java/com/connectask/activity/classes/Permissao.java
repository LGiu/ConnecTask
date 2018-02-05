package com.connectask.activity.classes;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissao(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissao = new ArrayList<String>();

            //Percorre as permissões passadas, verificando uma a uma se já tem a permissão liberada
            for (String permissao: permissoes){
                //valida a permissão
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                //Se não tem a permissão ela é guardada
                if(!validaPermissao) listaPermissao.add(permissao);
            }

            //Caso a lista esteja vazia, nã é necessário solicitar permissões
            if( listaPermissao.isEmpty()) return true;

            String[] novaPermissao = new String[listaPermissao.size()];
            listaPermissao.toArray(novaPermissao);

            //Solicitar permissão
            ActivityCompat.requestPermissions(activity, novaPermissao, requestCode);

        }

        return true;
    }

}
