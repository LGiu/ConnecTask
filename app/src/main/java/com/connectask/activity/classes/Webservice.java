package com.connectask.activity.classes;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webservice {

    private String url;
    private Uri.Builder headers;
    private Uri.Builder params;
    private BufferedReader reader;

    public Webservice(String url) {
        this.url = url;
        this.headers = new Uri.Builder();
        this.params = new Uri.Builder();
    }

    public void AddHeader(String name, String value) {
        headers.appendQueryParameter(name, value);
    }

    public void AddParam(String name, String value) {
        params.appendQueryParameter(name, value);
    }

    public enum RequestMethod {
        GET, POST
    }

    /**
     * Conecta no Webservice e puxa os dados da tabela
     *
     * @param requestMethod Método GET ou POST
     * @return Retorna um BufferedReader para casos de importação
     */
    public BufferedReader sendData(RequestMethod requestMethod) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            if (requestMethod.toString().equals("GET")) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                if (!params.toString().equals("")) {
                    wr.write(params.build().getEncodedQuery());
                }
                wr.flush();
            }
//            urlConnection.setDoInput(true);
//            urlConnection.setChunkedStreamingMode(0);
//                wr.close();
            this.reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }
}
