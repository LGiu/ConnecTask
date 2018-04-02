package com.connectask.activity.classes;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscaEndereco extends AsyncTask<String, Void, String> {
    URL url = null;
    HttpURLConnection httpURLConnection = null;

    public JSONObject object;

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = null;
        int respCode = -1;

        try {
            url = new URL("http://api.postmon.com.br/v1/cep/" + params[0] + ".json");
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            object = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
