package com.connectask.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.Home;
import com.connectask.activity.adapter.TarefaAdapter;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Avaliacao;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginaUsuario extends Fragment {

    private View view;

    private DatabaseReference firebase;

    private TextView textViewNome;
    private ListView listViewComentarios;
    private RatingBar ratingBar;

    private ArrayList<String> comentarios;
    private ArrayAdapter<String> adapter;
    private ArrayList<Tarefa> listaComentarios;

    private Float notaAvaliacao = 0.0f;

    public PaginaUsuario() {
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

        listViewComentarios = (ListView) view.findViewById(R.id.listViewComentarios);

        listarComentarios();

        return view;
    }

    private void listarComentarios(){
        comentarios = new ArrayList<String>();

        Preferencias preferencias = new Preferencias(getContext());
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("avalicao");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Avaliacao avaliacao = dados.getValue(Avaliacao.class);

                    if(avaliacao.getId_usuario_emissor().equals(identificadorUsuarioLogado) || avaliacao.getId_usuario_realizador().equals(identificadorUsuarioLogado)) {
                        comentarios.add(avaliacao.getAvaliacao());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, comentarios);
        listViewComentarios.setAdapter(adapter);
    }

}
