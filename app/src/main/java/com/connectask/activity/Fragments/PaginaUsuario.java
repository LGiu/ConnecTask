package com.connectask.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginaUsuario extends Fragment {

    private View view;

    private DatabaseReference firebase;

    private TextView textViewNome;
    private RatingBar ratingBar;

    private Float notaAvaliacao = 0.0f;

    public PaginaUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pagina_usuario, container, false);


        textViewNome = (TextView) view.findViewById(R.id.textViewNome);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);


        Preferencias preferencias = new Preferencias(getContext());
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuario");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    textViewNome.setText(usuario.getNome());

                    notaAvaliacao = Float.parseFloat(usuario.getAvaliacao());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratingBar.setRating(notaAvaliacao);

        return view;
    }

}
