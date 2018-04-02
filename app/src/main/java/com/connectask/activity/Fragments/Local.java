package com.connectask.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.DetalhesTarefa;
import com.connectask.activity.activity.MapsActivity;
import com.connectask.activity.activity.ProcessoTarefaRealizador;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Endereco;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Local extends Fragment {

    private View view;

    private DatabaseReference firebase;
    private DatabaseReference firebase2;

    private String idTarefa;

    private TextView textViewEstado;
    private TextView textViewCidade;
    private TextView textViewCep;
    private TextView textViewRua;
    private TextView textViewNumero;
    private TextView textViewBairro;
    private TextView textViewComplemento;
    private ImageButton imageButtonMaps;
    private TextView textViewMaps;

    private String end = "";

    public Local() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_local, container, false);

        textViewEstado = (TextView) view.findViewById(R.id.textViewEstado);
        textViewCidade = (TextView) view.findViewById(R.id.textViewCidade);
        textViewCep = (TextView) view.findViewById(R.id.textViewCep);
        textViewRua = (TextView) view.findViewById(R.id.textViewRua);
        textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
        textViewBairro = (TextView) view.findViewById(R.id.textViewBairro);
        textViewComplemento = (TextView) view.findViewById(R.id.textViewComplemento);
        imageButtonMaps = (ImageButton) view.findViewById(R.id.imageButtonMaps);
        textViewMaps = (TextView) view.findViewById(R.id.textViewMaps);


        if(idTarefa != null) {

            firebase = ConfiguracaoFirebase.getFirebase()
                    .child("tarefas");

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Tarefa tarefa = dados.getValue(Tarefa.class);

                        String id = tarefa.getId();
                        if (id.equals(idTarefa)) {

                            String idEndereco = tarefa.getEndereco();
                            firebase2 = ConfiguracaoFirebase.getFirebase()
                                    .child("endereco")
                                    .child(idEndereco);

                            firebase2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                        Endereco endereco = dados.getValue(Endereco.class);

                                        textViewEstado.setText(endereco.getEstado());
                                        textViewCidade.setText(endereco.getCidade());
                                        textViewCep.setText(endereco.getCep());
                                        textViewRua.setText(endereco.getRua());
                                        textViewNumero.setText(endereco.getNumero());
                                        textViewBairro.setText(endereco.getBairro());
                                        textViewComplemento.setText(endereco.getComplemento());

                                        end = ""+textViewCep.getText()+", "+textViewCidade.getText()+", "+textViewRua.getText()+", "+textViewNumero.getText()+"";
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


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
                intent.putExtra("endereco", end);
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

}
