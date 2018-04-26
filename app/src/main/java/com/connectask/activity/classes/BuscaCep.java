package com.connectask.activity.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.connectask.activity.Fragments.CadastroEndereco;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/*
public class BuscaEndereco{
    URL url = null;
    HttpURLConnection httpURLConnection = null;

    public JSONObject object;

    public String buscaCep(String cep) {
        StringBuilder result = null;
        int respCode = -1;

        try {
            object = new JSONObject();

            url = new URL("http://api.postmon.com.br/v1/cep/" + cep + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();

            do {
                if (httpURLConnection != null) {
                    respCode = httpURLConnection.getResponseCode();
                }
            } while (respCode == -1);

            if (respCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }

        return (result != null) ? result.toString() : null;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            object = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}*/


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
        //Showing progress dialog
        //pDialog = new ProgressDialog(context);
        //pDialog.setMessage("Por favor, aguarde...");
        //pDialog.setCancelable(false);
        //pDialog.show();
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
        delegate.processFinish(1, estado);
        delegate.processFinish(2, cidade);
        delegate.processFinish(3, rua);
        delegate.processFinish(4, bairro);
        //pDialog.dismiss();
    }
}

