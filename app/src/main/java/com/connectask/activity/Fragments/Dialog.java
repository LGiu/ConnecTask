package com.connectask.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.Perfil;
import com.connectask.activity.activity.ProcessoTarefaEmissor;
import com.connectask.activity.activity.ProcessoTarefaRealizador;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Tarefa;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dialog extends DialogFragment {

    private View view;

    private DatabaseReference firebase1;
    private DatabaseReference firebase2;
    private DatabaseReference firebase3;

    private Button ok;
    private TextView textViewTitulo;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewNome;
    private TextView textViewER;
    private TextView textViewStatus;
    private LinearLayout layoutTarefa;


    private boolean controle = false;

    public Dialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("Simple Dialog");

        textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
        textViewDescricao = (TextView) view.findViewById(R.id.textViewDescricao);
        textViewTempo = (TextView) view.findViewById(R.id.textViewTempo);
        textViewNome = (TextView) view.findViewById(R.id.textViewNome);
        textViewER = (TextView) view.findViewById(R.id.textViewER);
        textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
        layoutTarefa = (LinearLayout) view.findViewById(R.id.layoutTarefa);

        Button ok = (Button) view.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Preferencias preferencias = new Preferencias(getContext());
        final String identificadorUsuarioLogado = preferencias.getIdentificado();



        //Se o usuário for o Emissor da tarefa
        firebase1 = ConfiguracaoFirebase.getFirebase()
                .child("usuarios");

        firebase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    final Tarefa tarefa = dados.getValue(Tarefa.class);

                    if(tarefa.getStatus().toString().equals("2") && tarefa.getId_usuario().toString() == identificadorUsuarioLogado){
                        controle = true;

                        textViewTitulo.setText(tarefa.getTitulo().toString());
                        textViewDescricao.setText(tarefa.getDescricao().toString());
                        textViewTempo.setText(tarefa.getTempo().toString());
                        textViewER.setText("Realizador:");

                        firebase2 = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

                        firebase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                    final ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                                    if(processoTarefa.getId_tarefa().toString().equals(tarefa.getId().toString())){
                                        textViewNome.setText(tarefa.getTitulo().toString());

                                        firebase3 = ConfiguracaoFirebase.getFirebase()
                                                .child("usuarios");

                                        firebase3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                    Usuario usuario = dados.getValue(Usuario.class);

                                                    if(processoTarefa.getId_usuario_realizador().toString().equals(codificarBase64(usuario.getEmail().toString()))){
                                                        textViewNome.setText(usuario.getNome().toString());
                                                    }

                                                    layoutTarefa.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent intent = new Intent(getContext(), ProcessoTarefaEmissor.class);
                                                            startActivity(intent);
                                                        }
                                                    });
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Se o usuário for o Realizador da tarefa
        firebase1 = ConfiguracaoFirebase.getFirebase()
                .child("ProcessoTarefa");

        firebase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    final ProcessoTarefa processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if(processoTarefa.getId_usuario_realizador().toString().equals(identificadorUsuarioLogado)){

                        firebase2 = ConfiguracaoFirebase.getFirebase()
                                .child("tarefas");

                        firebase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                    final Tarefa tarefa = dados.getValue(Tarefa.class);

                                    if(tarefa.getId().toString() == processoTarefa.getId_tarefa().toString() && tarefa.getStatus().toString().equals("2")){

                                        textViewER.setText("Emissor:");

                                        textViewTitulo.setText(tarefa.getTitulo().toString());
                                        textViewDescricao.setText(tarefa.getDescricao().toString());
                                        textViewTempo.setText(tarefa.getTempo().toString());


                                        firebase3 = ConfiguracaoFirebase.getFirebase()
                                                .child("usuarios");

                                        firebase3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                                    Usuario usuario = dados.getValue(Usuario.class);

                                                    if(tarefa.getId_usuario().toString().equals(codificarBase64(usuario.getEmail().toString()))){
                                                        textViewNome.setText(usuario.getNome().toString());
                                                        controle = true;

                                                        layoutTarefa.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Intent intent = new Intent(getContext(), ProcessoTarefaRealizador.class);
                                                                startActivity(intent);
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


        if(controle == false){
            layoutTarefa.setVisibility(View.GONE);
            textViewStatus.setText("Nenhuma tarefa ativa no momento.");
        }

        return view;
    }

}
