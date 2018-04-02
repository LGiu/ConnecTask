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

    private DatabaseReference firebase;

    private Button ok;
    private TextView textViewTitulo;
    private TextView textViewDescricao;
    private TextView textViewTempo;
    private TextView textViewNome;
    private TextView textViewER;
    private TextView textViewStatus;
    private LinearLayout layoutTarefa;

    private ProcessoTarefa processoTarefa;
    private Tarefa tarefa;

    private String tarefaId = "";
    private String id_usuario = "";
    private String id_ProcessoTarefa = "";
    private String status = "";

    private boolean c1 = true;
    private boolean c2 = true;


    public Dialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dialog, container, false);
        getDialog().setTitle("");

        textViewTitulo = (TextView) view.findViewById(R.id.textViewTitle);
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
        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    tarefa = dados.getValue(Tarefa.class);
                    if(tarefa.getStatus().toString().equals("2") && tarefa.getId_usuario().equals(identificadorUsuarioLogado)){
                        tarefaId = tarefa.getId();
                        status = tarefa.getStatus();

                        textViewTitulo.setText(tarefa.getTitulo().toString());
                        textViewTempo.setText(tarefa.getTempo().toString());
                        textViewER.setText("Realizador:");
                        c1 = false;
                        montaEmissor();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tarefaId = "";
        id_usuario = "";
        id_ProcessoTarefa = "";

        //Se o usuário for o Realizador da tarefa
        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if(processoTarefa.getId_usuario_realizador().toString().equals(identificadorUsuarioLogado) && processoTarefa.getAtivo().equals("1")){
                        tarefaId = processoTarefa.getId_tarefa().toString();
                        id_ProcessoTarefa = processoTarefa.getId().toString();
                        c2 = false;

                        montaRealizador();
                    }

                }

                if (c1 && c2) {
                    vazio();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return view;
    }

    public void vazio(){
        layoutTarefa.setVisibility(View.GONE);
        textViewStatus.setText("Nenhuma tarefa ativa no momento.");
    }

    public void montaEmissor(){
        firebase = ConfiguracaoFirebase.getFirebase().child("ProcessoTarefa");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    processoTarefa = dados.getValue(ProcessoTarefa.class);

                    if (processoTarefa.getId_tarefa().equals(tarefaId)) {
                        id_usuario = processoTarefa.getId_usuario_realizador();
                        id_ProcessoTarefa = processoTarefa.getId().toString();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    if (id_usuario.equals(codificarBase64(usuario.getEmail()))) {
                        textViewNome.setText(usuario.getNome().toString());
                    }

                    layoutTarefa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), ProcessoTarefaEmissor.class);
                            intent.putExtra("id", tarefaId);
                            intent.putExtra("id_ProcessoTarefa", id_ProcessoTarefa);
                            intent.putExtra("status", status);
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

    public void montaRealizador(){
        firebase = ConfiguracaoFirebase.getFirebase().child("tarefas");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    tarefa = dados.getValue(Tarefa.class);
                    tarefaId = tarefaId;
                    id_ProcessoTarefa = id_ProcessoTarefa;
                    if (tarefa.getId().toString().equals(tarefaId) && tarefa.getStatus().toString().equals("2")) {
                        id_usuario = tarefa.getId_usuario();

                        textViewER.setText("Emissor:");

                        textViewTitulo.setText(tarefa.getTitulo().toString());
                        textViewTempo.setText(tarefa.getTempo().toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    if (id_usuario.equals(codificarBase64(usuario.getEmail().toString()))) {
                        textViewNome.setText(usuario.getNome().toString());

                        layoutTarefa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), ProcessoTarefaRealizador.class);
                                intent.putExtra("id", tarefaId);
                                intent.putExtra("id_ProcessoTarefa", id_ProcessoTarefa);
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
