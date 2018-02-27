package com.connectask.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.connectask.R;
import com.connectask.activity.activity.Home;
import com.connectask.activity.activity.ProcessoTarefaRealizador;

/**
 * A simple {@link Fragment} subclass.
 */
public class Filtro extends Fragment {

    private View view;

    private Spinner spinnerCategoria;
    private SeekBar seekBarLocalizacao;
    private SeekBar seekBarTempo;
    private SeekBar seekBarValor;
    private Button buttonFiltrar;

    public Filtro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_filtro, container, false);

        spinnerCategoria = (Spinner) view.findViewById(R.id.spinnerCategoria);
        seekBarLocalizacao = (SeekBar) view.findViewById(R.id.seekBarLocalizacao);
        seekBarTempo = (SeekBar) view.findViewById(R.id.seekBarTempo);
        seekBarValor = (SeekBar) view.findViewById(R.id.seekBarValor);
        buttonFiltrar = (Button) view.findViewById(R.id.buttonFiltrar);

        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = new Intent(getActivity(), Home.class);
                if(spinnerCategoria.getSelectedItem().toString().equals(null));
                {
                    intent.putExtra("categoria", spinnerCategoria.getSelectedItem().toString());
                }
                if(seekBarLocalizacao.getProgress() == 0)
                {
                    intent.putExtra("localizacao", seekBarLocalizacao.getProgress());
                }
                if(seekBarTempo.getProgress() == 0)
                {
                    intent.putExtra("tempo", seekBarTempo.getProgress());
                }
                if(seekBarValor.getProgress() == 0)
                {
                    intent.putExtra("valor", seekBarValor.getProgress());
                }
                startActivity(intent);
            }
        });

        return view;
    }

}
