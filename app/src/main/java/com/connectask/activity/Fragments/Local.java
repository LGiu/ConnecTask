package com.connectask.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.MapsActivity;
import com.connectask.activity.classes.Progress;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Endereco;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Local extends Fragment{

    private View view;

    private DatabaseReference firebase;

    private TextView textViewEstado;
    private TextView textViewCidade;
    private TextView textViewCep;
    private TextView textViewRua;
    private TextView textViewNumero;
    private TextView textViewBairro;
    private TextView textViewComplemento;
    private ImageButton imageButtonMaps;
    private TextView textViewMaps;

    private String latitude;
    private String longitude;

    private String idTarefa;
    private String idEndereco;
    private String idUsuario;


    public Local() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_local, container, false);

        Progress progress = new Progress(getContext(), false);
        progress.threard(1000);


        textViewEstado = (TextView) view.findViewById(R.id.textViewEstado);
        textViewCidade = (TextView) view.findViewById(R.id.textViewCidade);
        textViewCep = (TextView) view.findViewById(R.id.textViewTempo);
        textViewRua = (TextView) view.findViewById(R.id.textViewRua);
        textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
        textViewBairro = (TextView) view.findViewById(R.id.textViewBairro);
        textViewComplemento = (TextView) view.findViewById(R.id.textViewComplemento);
        imageButtonMaps = (ImageButton) view.findViewById(R.id.imageButtonMaps);
        textViewMaps = (TextView) view.findViewById(R.id.textViewMaps);

        if (idTarefa != null) {

            firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Tarefa tarefa = dados.getValue(Tarefa.class);

                        if (tarefa.getId().equals(idTarefa)) {
                            idUsuario = tarefa.getId_usuario();
                            idEndereco = tarefa.getEndereco();
                            preencherEndereco();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        imageButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("longitude", String.valueOf(longitude));
                startActivity(intent);
            }
        });

        textViewMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void setId(String idTarefa) {
        this.idTarefa = idTarefa;
    }

    private void preencherEndereco(){
        if(idUsuario != null) {
            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("endereco")
                    .child(idUsuario);

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Endereco endereco = dados.getValue(Endereco.class);

                        if (endereco.getId().equals(idEndereco)) {
                            textViewEstado.setText(endereco.getEstado());
                            textViewCidade.setText(endereco.getCidade());
                            textViewCep.setText(endereco.getCep());
                            textViewRua.setText(endereco.getRua());
                            textViewNumero.setText(endereco.getNumero());
                            textViewBairro.setText(endereco.getBairro());
                            textViewComplemento.setText(endereco.getComplemento());
                            latitude = endereco.getLatitude().toString();
                            longitude = endereco.getLongitude().toString();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
