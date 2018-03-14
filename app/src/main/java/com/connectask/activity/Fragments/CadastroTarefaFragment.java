package com.connectask.activity.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.connectask.R;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroTarefaFragment extends Fragment {

    private View view;

    private EditText editTextTitulo;
    private Spinner spinnerTipo;
    private EditText editTextDescricao;
    private EditText editTextTempo;
    private EditText editTextValor;
    private Spinner spinnerEndereco;
    private Button buttonCadastro;
    private Button buttonNovoEndereco;
    private List<String> listaTipo = new ArrayList<String>();
    private List<String> listaEndereco = new ArrayList<String>();
    private Context context;

    private Tarefa tarefa;
    private FirebaseAuth autenticacao;


    public CadastroTarefaFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cadastro_tarefa, container, false);

        context = container.getContext();

        editTextTitulo = (EditText) view.findViewById(R.id.editTextNome);
        spinnerTipo = (Spinner) view.findViewById(R.id.spinnerTipo);
        editTextDescricao = (EditText) view.findViewById(R.id.editTextDescricao);
        editTextTempo = (EditText) view.findViewById(R.id.editTextTempo);
        editTextValor = (EditText) view.findViewById(R.id.editTextValor);
        spinnerEndereco = (Spinner) view.findViewById(R.id.spinnerEndereco);
        buttonCadastro = (Button) view.findViewById(R.id.buttonFinalizar);
        buttonNovoEndereco = (Button) view.findViewById(R.id.buttonNovoEndereco);

        tipo(view);
        endereco(view);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarefa = new Tarefa();
                tarefa.setTitulo(editTextTitulo.getText().toString());
                tarefa.setTipo(spinnerTipo.getSelectedItem().toString());
                tarefa.setDescricao(editTextDescricao.getText().toString());
                tarefa.setTempo(editTextTempo.getText().toString());
                tarefa.setValor((editTextValor.getText().toString()));
                tarefa.setEndereco(editTextValor.getText().toString());


                tarefa.salvar(context);

                //Fecha fragment
                getActivity().onBackPressed();

            }
        });

        buttonNovoEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CadastroEndereco cadastroEndereco = new CadastroEndereco();
                fragmentTransaction.add(R.id.fragment_container, cadastroEndereco);
                fragmentTransaction.addToBackStack(null).commit();

                CadastroEndereco cadastroEndereco = new CadastroEndereco();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, cadastroEndereco, "Novo Endereço");
                fragmentTransaction.addToBackStack(null).commit();*/


            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {

        }

        return super.onOptionsItemSelected(item);
    }
    */

    public void tipo(View view){
        listaTipo.add("Doméstica");
        listaTipo.add("Externa");
        listaTipo.add("Serviço");
        listaTipo.add("Outro");

        ArrayAdapter<String> arrayAdapterTipo = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, listaTipo);
        arrayAdapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(arrayAdapterTipo);
    }

    public void endereco(View view){
        listaEndereco.add("ab");

        ArrayAdapter<String> arrayAdapterEndereco = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, listaEndereco);
        arrayAdapterEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndereco.setAdapter(arrayAdapterEndereco);
    }

}
