package com.connectask.activity.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.connectask.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String end;
    private double lat;
    private double lon;
    private double zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        end = intent.getStringExtra("end");

        /*final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            geoLocate();
            LatLng ll = new LatLng(-lat, lon);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mMap.moveCamera(update);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void geoLocate() throws IOException {
        Geocoder gc = new Geocoder(this);
        try{
            List<Address> list = null;
            list = gc.getFromLocationName(end, 4);

            Address add = list.get(0);
            String locality = add.getLocality();

            lat = add.getLatitude();
            lon = add.getLongitude();
        }
        catch(IOException e) {
        }


    }
}
