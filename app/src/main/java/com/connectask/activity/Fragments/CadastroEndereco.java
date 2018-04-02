package com.connectask.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.connectask.R;
import com.connectask.activity.classes.BuscaEndereco;
import com.connectask.activity.classes.Util;
import com.connectask.activity.model.Endereco;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroEndereco extends Fragment {

    private View view;

    private EditText editTextCep;
    private EditText editTextRua;
    private EditText editTextNumero;
    private EditText editTextBairro;
    private EditText editTextComplemento;
    private EditText editTextCidade;
    private Spinner spinnerEstado;
    private Button buttonCadastro;

    private BuscaEndereco buscaEndereco = new BuscaEndereco();

    private String msg = "Campos incorretos:";

    private List<String> listaEstado = new ArrayList<String>();

    private Endereco endereco;

    public CadastroEndereco() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cadastro_endereco, container, false);

        editTextCep = (EditText) view.findViewById(R.id.editTextCep);
        editTextRua = (EditText) view.findViewById(R.id.editTextNome);
        editTextNumero = (EditText) view.findViewById(R.id.editTextComplemento);
        editTextBairro = (EditText) view.findViewById(R.id.editTextBairro);
        editTextComplemento = (EditText) view.findViewById(R.id.editTextComplemento);
        editTextCidade = (EditText) view.findViewById(R.id.editTextCidade);
        spinnerEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        buttonCadastro = (Button) view.findViewById(R.id.buttonFinalizar);

        estado();

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endereco = new Endereco();
                endereco.setEstado(spinnerEstado.getSelectedItem().toString());
                endereco.setCidade(editTextCidade.getText().toString());
                endereco.setCep(editTextCep.getText().toString());
                endereco.setRua(editTextRua.getText().toString());
                endereco.setNumero(editTextNumero.getText().toString());
                endereco.setBairro(editTextBairro.getText().toString());
                endereco.setComplemento((editTextComplemento.getText().toString()));

                cadastrarEnderenco();
            }
        });


        editTextCep.addTextChangedListener(new TextWatcher() {
            int x = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if(start == 4){
                //   editTextCep.setText(editTextCep.getText()+"-");
                //    s = editTextCep.getText()+"-";
                //}
                if(start == 7){
                    try {
                        buscaCep();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void cadastrarEnderenco() {

        //tarefa.setId(task.getResult().getUser.getUid(););
        endereco.salvar(getContext());
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_cadastro);
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();    }

    private void estado(){
        listaEstado.add("AC");
        listaEstado.add("AL");
        listaEstado.add("AM");
        listaEstado.add("AP");
        listaEstado.add("BA");
        listaEstado.add("CE");
        listaEstado.add("DF");
        listaEstado.add("ES");
        listaEstado.add("GO");
        listaEstado.add("MA");
        listaEstado.add("MG");
        listaEstado.add("MS");
        listaEstado.add("MT");
        listaEstado.add("PA");
        listaEstado.add("PB");
        listaEstado.add("PE");
        listaEstado.add("PI");
        listaEstado.add("PR");
        listaEstado.add("RJ");
        listaEstado.add("RN");
        listaEstado.add("RO");
        listaEstado.add("RR");
        listaEstado.add("RS");
        listaEstado.add("SC");
        listaEstado.add("SE");
        listaEstado.add("SP");
        listaEstado.add("TO");

        ArrayAdapter<String> arrayAdapterEstado = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, listaEstado);
        arrayAdapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(arrayAdapterEstado);
    }

    private void buscaCep() throws JSONException {
        buscaEndereco.execute(editTextCep.getText().toString());
        editTextRua.setText(buscaEndereco.object.getString("logradouro"));
        editTextBairro.setText(buscaEndereco.object.getString("bairro"));

        for (int i = 0; i < 26; i++) {
            if(listaEstado.get(i).equals(buscaEndereco.object.getString("estado"))){
                spinnerEstado.setSelection(i);
            }
        }

    }

    private boolean valida(){
        boolean teste = true;
        msg = "Campos incorretos:";
        Util util = new Util();

        if(editTextCep.getText().length() != 8)
        {
            msg += "\nCep inválida.";
            teste = false;
        }
        if(editTextCidade.getText().length() > 100 || editTextCidade.getText().length() == 0)
        {
            msg += "\nCidade inválido.";
            teste = false;
        }
        if(editTextRua.getText().length() > 255 || editTextCidade.getText().length() == 0)
        {
            msg += "\nRua Inválida.";
            teste = false;
        }
        if(editTextBairro.getText().length() > 70 || editTextBairro.getText().length() == 0)
        {
            msg += "\nBairro inválido.";
            teste = false;
        }
        if(editTextNumero.getText().length() > 7 || editTextNumero.getText().length() == 0)
        {
            msg += "\nNúmero inválido.";
            teste = false;
        }
        if(editTextNumero.getText().length() > 70)
        {
            msg += "\nNúmero inválido.";
            teste = false;
        }

        return teste;
    }

}
