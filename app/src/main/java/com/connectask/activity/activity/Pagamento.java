package com.connectask.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.connectask.R;
import com.connectask.activity.Fragments.CadastroPagamento;

import java.util.ArrayList;
import java.util.List;

public class Pagamento extends AppCompatActivity {

    private Spinner spinnerPagamento;
    private Button buttonNovoMeio;
    private Button buttonFinalizar;

    private List<String> listaPagamento = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        spinnerPagamento = (Spinner) findViewById(R.id.spinnerPagamento);
        buttonNovoMeio = (Button) findViewById(R.id.buttonNovoMeio);
        buttonFinalizar = (Button) findViewById(R.id.buttonFinalizar);

        pagamento();

        buttonNovoMeio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastroPagamento cadastroPagamento = new CadastroPagamento();

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_pagamento, cadastroPagamento);
                fragmentTransaction.addToBackStack(null).commit();

                /*android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                CadastroPagamento cadastroPagamento = new CadastroPagamento();
                android.support.v4.app.FragmentTransaction add = fragmentTransaction.add(R.id.fragment_pagamento, cadastroPagamento);
                fragmentTransaction.addToBackStack(null).commit();*/

                /*toolbar.setTitle("Nova tarefa");
                toolbar.setNavigationIcon(null);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);*/
            }
        });

        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pagamento.this, Home.class);
                startActivity(intent);
            }
        });

    }

    public void pagamento(){
        listaPagamento.add("xxxx-xxxx-xxxx-2897");

        ArrayAdapter<String> arrayAdapterTipo = new ArrayAdapter<String>(Pagamento.this, android.R.layout.simple_list_item_1, listaPagamento);
        arrayAdapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPagamento.setAdapter(arrayAdapterTipo);
    }
}
