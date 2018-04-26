package com.connectask.activity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.connectask.R;
import com.connectask.activity.activity.DetalhesTarefa;
import com.connectask.activity.activity.EditarEndereco;
import com.connectask.activity.activity.Home;
import com.connectask.activity.activity.ProcessoTarefaRealizador;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.Endereco;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Leonardo Giuliani on 03/01/2018.
 */

public class EnderecoAdapter extends ArrayAdapter<Endereco>{
    private ArrayList<Endereco> listaEndereco;
    private Context context;
    private DatabaseReference firebase;

    private TextView textViewEstado;
    private TextView textViewCidade;
    private TextView textViewCep;
    private TextView textViewRua;
    private TextView textViewNumero;
    private TextView textViewBairro;
    private TextView textViewComplemento;
    private ImageButton imageButtonEditar;
    private ImageButton imageButtonExcluir;

    private int posicao;

    public EnderecoAdapter(Context c, ArrayList<Endereco> objects) {
        super(c, 0, objects);
        this.context = c;
        this.listaEndereco = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está preenchida
        if( listaEndereco != null ){
            posicao = position;

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_endereco, parent, false);

            // recupera elemento para exibição
            textViewEstado = (TextView) view.findViewById(R.id.textViewEstado);
            textViewCidade = (TextView) view.findViewById(R.id.textViewCidade);
            textViewCep = (TextView) view.findViewById(R.id.textViewCep);
            textViewRua = (TextView) view.findViewById(R.id.textViewRua);
            textViewNumero = (TextView) view.findViewById(R.id.textViewNumero);
            textViewBairro = (TextView) view.findViewById(R.id.textViewBairro);
            textViewComplemento = (TextView) view.findViewById(R.id.textViewComplemento);
            imageButtonEditar = (ImageButton) view.findViewById(R.id.imageButtonEditar);
            imageButtonExcluir = (ImageButton) view.findViewById(R.id.imageButtonExcluir);

            final Endereco endereco = listaEndereco.get(posicao);
            textViewEstado.setText( endereco.getEstado() );
            textViewCidade.setText( endereco.getCidade() );
            textViewCep.setText( endereco.getCep() );
            textViewRua.setText( endereco.getRua() );
            textViewNumero.setText( endereco.getNumero().toString() );
            textViewBairro.setText( endereco.getBairro() );
            textViewComplemento.setText( endereco.getComplemento() );


            imageButtonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditarEndereco.class);
                    intent.putExtra("id", endereco.getId());
                    intent.putExtra("estado", textViewEstado.getText().toString());
                    intent.putExtra("cidade", textViewCidade.getText().toString());
                    intent.putExtra("cep", textViewCep.getText().toString());
                    intent.putExtra("rua", textViewRua.getText().toString());
                    intent.putExtra("numero", textViewNumero.getText().toString());
                    intent.putExtra("bairro", textViewBairro.getText().toString());
                    intent.putExtra("complemento", textViewComplemento.getText().toString());
                    context.startActivity(intent);
                }
            });

            imageButtonExcluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preferencias preferencias = new Preferencias(context);
                    final String identificadorUsuarioLogado = preferencias.getIdentificado();

                    new AlertDialog.Builder(context)
                            .setTitle("Excluir Endereço")
                            .setMessage("Tem certeza que deseja excluir este endereço?")
                            .setPositiveButton("Sim",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            firebase.child(identificadorUsuarioLogado).child(endereco.getId()).setValue(null);
                                            Intent intent = new Intent(context, com.connectask.activity.activity.Endereco.class);
                                            context.startActivity(intent);

                                        }
                                    })
                            .setNegativeButton("Não", null)
                            .show();
                }
            });
        }

        return view;
    }
}

