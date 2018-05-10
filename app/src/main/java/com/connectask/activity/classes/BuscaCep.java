package com.connectask.activity.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.connectask.activity.Fragments.CadastroEndereco;

import org.json.JSONObject;

import java.io.BufferedReader;

public class BuscaCep extends AsyncTask<Void, Void, Void> {

    private ProgressDialog pDialog;
    private Context context;
    private String cep;
    private String estado;
    private String cidade;
    private String rua;
    private String bairro;

    public JSONObject objectCep = null;

    public CadastroEndereco delegate = null;

    public BuscaCep(Context context, String cep) {
        this.context = context;
        this.cep = cep;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String line;
        BufferedReader reader = new Webservice("https://viacep.com.br/ws/"+cep+"/json/" ).sendData(Webservice.RequestMethod.GET);
        try {
            StringBuffer buffer = new StringBuffer();
            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            objectCep = new JSONObject(buffer.toString());

            estado = objectCep.getString("uf");
            cidade = objectCep.getString("localidade");
            rua = objectCep.getString("logradouro");
            bairro = objectCep.getString("bairro");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            delegate.processFinish(1, estado);
            delegate.processFinish(2, cidade);
            delegate.processFinish(3, rua);
            delegate.processFinish(4, bairro);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

