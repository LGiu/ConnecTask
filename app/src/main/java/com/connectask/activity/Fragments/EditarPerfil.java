package com.connectask.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.activity.Perfil;
import com.connectask.activity.classes.Cpf;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Telefone;
import com.connectask.activity.classes.Util;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarPerfil extends Fragment {

    private View view;

    private TextView textViewCampo;
    private EditText editTextCampo;
    private Button buttonSalvar;

    private String txCampo;
    private String etCampo;

    private DatabaseReference firebase;

    private String msg;

    public EditarPerfil() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        textViewCampo = (TextView) view.findViewById(R.id.textViewCampo);
        editTextCampo = (EditText) view.findViewById(R.id.editTextCampo);
        buttonSalvar = (Button) view.findViewById(R.id.buttonSalvar);

        textViewCampo.setText(txCampo);
        editTextCampo.setText(etCampo);

        if(textViewCampo.equals("CPF")){
            editTextCampo.addTextChangedListener(Cpf.insert(editTextCampo,1));
            editTextCampo.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        }
        else if(textViewCampo.equals("Telefone")){
            editTextCampo.addTextChangedListener(Telefone.insert("(##)#####-####", editTextCampo));
            editTextCampo.setInputType(InputType.TYPE_CLASS_PHONE);

        }
        else if(textViewCampo.equals("Senha")){
            editTextCampo.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else if(textViewCampo.equals("E-Mail")){
            editTextCampo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valida()) {
                    Preferencias preferencias = new Preferencias(getContext());
                    final String identificadorUsuarioLogado = preferencias.getIdentificado();

                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dados : dataSnapshot.getChildren()) {
                                Usuario usuario = dados.getValue(Usuario.class);

                                if (identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail())))
                                {
                                    String campo = txCampo.toLowerCase().replace(":","").replace("-","");
                                    String valor = editTextCampo.getText().toString();

                                    firebase.child(identificadorUsuarioLogado).child(campo).setValue(valor);

                                    Intent intent = new Intent(getContext(), Perfil.class);
                                    startActivity(intent);

                                    /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_perfil);
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.remove(fragment);
                                    fragmentTransaction.commit();*/
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    public void setCampos(String tx, String ed){
        txCampo = tx;
        etCampo = ed;
    }

    private boolean valida(){
        boolean teste = true;
        msg = "Campos incorretos:";

        Util util = new Util();

        if(txCampo.equals("Nome")){
            if(editTextCampo.getText().length() > 75 || editTextCampo.getText().length() == 0)
            {
                msg += "\nNome inválido.";
                teste = false;
            }
        }
        else if(txCampo.equals("E-Mail")){
            if(editTextCampo.getText().length() != 14)
            {
                msg += "\nCPF inválido.";
                teste = false;
            }
        }
        else if(txCampo.equals("CPF")){
            if(editTextCampo.getText().length() != 14)
            {
                msg += "\nCPF inválido.";
                teste = false;
            }
        }
        else if(txCampo.equals("Telefone")){
            if(editTextCampo.getText().length() != 13 && editTextCampo.getText().length() != 14)
            {
                msg += "\nTelefone inválido.";
                teste = false;
            }
        }
        else if(txCampo.equals("Senha")){
            if(editTextCampo.getText().toString().length() < 6 && editTextCampo.getText().toString().length() > 20)
            {
                msg += "\nSenha inválido.\nA senha deve conter no mínimo 6 e no máximo 20 dígitos.";
                teste = false;
            }
        }

        return teste;
    }

}
