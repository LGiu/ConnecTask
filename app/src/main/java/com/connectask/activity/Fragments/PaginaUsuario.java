package com.connectask.activity.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.connectask.activity.activity.Denunciar;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Avaliacao;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginaUsuario extends Fragment {

    private View view;

    private DatabaseReference firebase;

    private TextView textViewNome;
    private TextView textViewTarefasRealizadas;
    private TextView textViewTarefasFinalizadas;
    private TextView textViewAvaliacoes;
    private TextView textViewNComentario;
    private ListView listViewComentarios;
    private TextView textViewDenunciar;
    private RatingBar ratingBar;

    private ArrayList<String> comentarios;
    private ArrayAdapter<String> adapter;
    private ArrayList<Tarefa> listaComentarios;

    private Float notaAvaliacao = 0.0f;

    private String idUsuario;
    private String idTarefa;

    private int numeroAvaliacoes = 0;
    private int numeroFinalizadas = 0;
    private int numeroRealizadas = 0;

    private Context context;

    private ProgressDialog pDialog;

    public PaginaUsuario() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pagina_usuario, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Por favor, aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

        textViewTarefasRealizadas = (TextView) view.findViewById(R.id.textViewTarefasRealizadas);
        textViewTarefasFinalizadas = (TextView) view.findViewById(R.id.textViewTarefasFinalizadas);
        textViewAvaliacoes = (TextView) view.findViewById(R.id.textViewAvaliacoes);
        textViewNComentario = (TextView) view.findViewById(R.id.textViewNComentario);
        textViewNome = (TextView) view.findViewById(R.id.textViewNome);
        textViewDenunciar = (TextView) view.findViewById(R.id.textViewDenunciar);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);


        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(idUsuario.equals(codificarBase64(usuario.getEmail().toString()))){
                        textViewNome.setText(usuario.getNome());

                        notaAvaliacao = Float.parseFloat(usuario.getAvaliacao());

                        textViewDenunciar.setVisibility(View.GONE);
                    }

                }
                ratingBar.setRating(notaAvaliacao);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewComentarios = (ListView) view.findViewById(R.id.listViewComentarios);

        listarComentarios();

        textViewDenunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Denunciar.class);
                intent.putExtra("nome", textViewNome.getText().toString());
                intent.putExtra("id", idUsuario);
                startActivity(intent);
            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("avaliacao");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Avaliacao avaliacao = dados.getValue(Avaliacao.class);

                    if(idUsuario.equals(avaliacao.getId_usuario_realizador())){
                        numeroAvaliacoes++;
                        textViewAvaliacoes.setText(String.valueOf(numeroAvaliacoes));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if(idUsuario.equals(processoTarefa.getId_usuario_emissor()) && processoTarefa.getAtivoEmissor().equals("2"))
                    {
                        numeroFinalizadas++;
                        textViewTarefasFinalizadas.setText(String.valueOf(numeroFinalizadas));
                    }
                    else if(idUsuario.equals(processoTarefa.getId_usuario_realizador()) && processoTarefa.getAtivoRealizador().equals("2")){
                        numeroRealizadas++;
                        textViewTarefasRealizadas.setText(String.valueOf(numeroRealizadas));
                    }

                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void setId(String id) {

        idTarefa = id;

        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(tarefa.getId().equals(idTarefa)){
                        idUsuario = tarefa.getId_usuario();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void listarComentarios(){
        comentarios = new ArrayList<String>();

        firebase = ConfiguracaoFirebase.getFirebase().child("avaliacao");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Avaliacao avaliacao = dados.getValue(Avaliacao.class);

                    if(avaliacao.getId_usuario_realizador().equals(idUsuario)) {
                        comentarios.add(avaliacao.getAvaliacao());
                        textViewNComentario.setVisibility(View.GONE);
                    }
                }
                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, comentarios);
                listViewComentarios.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
