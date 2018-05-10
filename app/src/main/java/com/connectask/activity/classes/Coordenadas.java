package com.connectask.activity.classes;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Leonardo Giuliani on 03/04/2018.
 */

public class Coordenadas {

    private Geocoder gc;

    public double latitude;
    public double longitude;

    private List<Address> address = null;
    private String end = null;

    public Context contexto;


    public Coordenadas(Context context, String estado, String cidade,  String cep, String rua, String numero){
        contexto = context;
        end = estado +", "+ cidade +", "+ cep +", "+ rua +", "+ numero;

        try {
            geoLocate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void geoLocate() throws IOException {
        try {
            gc = new Geocoder(contexto, Locale.getDefault());

            address = gc.getFromLocationName(end, 1);

            Address add = address.get(0);
            String locality = add.getLocality();

            latitude = add.getLatitude();
            longitude = add.getLongitude();
        }
        catch (Exception e){

        }

    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}
