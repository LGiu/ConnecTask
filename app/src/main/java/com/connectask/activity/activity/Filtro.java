package com.connectask.activity.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.ValoresFiltro;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Filtro extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private SeekBar seekBarLocalizacao;
    private SeekBar seekBarTempo;
    private SeekBar seekBarValor;
    private Button buttonFiltrar;
    private Button buttonLimpar;

    private TextView textViewProgressoLocalizacao;
    private TextView textViewProgressoValor;
    private TextView textViewProgressoTempo;


    private List<String> listaTipo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinnerCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        seekBarLocalizacao = (SeekBar) findViewById(R.id.seekBarLocalizacao);
        seekBarTempo = (SeekBar) findViewById(R.id.seekBarTempo);
        seekBarValor = (SeekBar) findViewById(R.id.seekBarValor);
        buttonFiltrar = (Button) findViewById(R.id.buttonFiltrar);
        buttonLimpar = (Button) findViewById(R.id.buttonLimpar);
        textViewProgressoLocalizacao = (TextView) findViewById(R.id.textViewProgressoLocalizacao);
        textViewProgressoValor = (TextView) findViewById(R.id.textViewProgressoValor);
        textViewProgressoTempo = (TextView) findViewById(R.id.textViewProgressoTempo);

        tipo();

        textViewProgressoLocalizacao.setText("15 Km(s)");
        textViewProgressoValor.setText("R$ 500,00");
        textViewProgressoTempo.setText("96 Horas");

        seekBarLocalizacao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 15;
            @SuppressLint("WrongConstant")
            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_LONG).show();
                textViewProgressoLocalizacao.setText(""+progress+" Km(s)");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }

        });

        seekBarValor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 15;
            @SuppressLint("WrongConstant")
            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_LONG).show();
                textViewProgressoValor.setText("R$ "+progress+",00");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }

        });

        seekBarTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 15;
            @SuppressLint("WrongConstant")
            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_LONG).show();
                textViewProgressoTempo.setText(""+progress+" Horas");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_SHORT).show();
            }

        });

        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent = new Intent(Filtro.this, Home.class);

                ValoresFiltro preferencias = new ValoresFiltro(Filtro.this);

                preferencias.salvarDados(spinnerCategoria.getSelectedItem().toString(), seekBarLocalizacao.getProgress(), seekBarValor.getProgress() , seekBarTempo.getProgress());
                startActivity(intent);
            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(Filtro.this, Home.class);

                ValoresFiltro preferencias = new ValoresFiltro(Filtro.this);
                preferencias.limparDados();

                startActivity(intent);
            }
        });
    }

    public void tipo(){
        listaTipo.add("");
        listaTipo.add("Doméstica");
        listaTipo.add("Externa");
        listaTipo.add("Serviço");
        listaTipo.add("Outro");

        ArrayAdapter<String> arrayAdapterTipo = new ArrayAdapter<String>(Filtro.this, android.R.layout.simple_list_item_1, listaTipo);
        arrayAdapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(arrayAdapterTipo);
    }

}
