package com.connectask.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.connectask.R;
import com.connectask.activity.model.Endereco;

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

}
