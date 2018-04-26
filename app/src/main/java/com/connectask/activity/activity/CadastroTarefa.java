package com.connectask.activity.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.Fragments.CadastroEndereco;
import com.connectask.activity.classes.Moeda;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Util;
import com.connectask.activity.classes.pagamento.CreditCard;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Endereco;
import com.connectask.activity.model.Tarefa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.library.MDDialog;

public class CadastroTarefa extends AppCompatActivity {

    private EditText editTextTitulo;
    private Spinner spinnerTipo;
    private EditText editTextDescricao;
    private TextView textViewTempo;
    private SeekBar seekBarTempo;
    private EditText editTextValor;
    private Spinner spinnerEndereco;
    private Button buttonCadastro;
    private Button buttonNovoEndereco;
    private List<String> listaTipo = new ArrayList<String>();
    private List<String> listaEndereco = new ArrayList<String>();
    private List<String> idEndereco = new ArrayList<String>();
    private List<String> listaTempo = new ArrayList<String>();
    private ImageButton imageButtonTipo;
    private ImageButton imageButtonDescricao;
    private ImageButton imageButtonValor;
    private ImageButton imageButtonTempo;

    private Tarefa tarefa;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;

    private String msg = "Campos incorretos:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tarefa);

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

        editTextTitulo = (EditText) findViewById(R.id.editTextNome);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipo);
        editTextDescricao = (EditText) findViewById(R.id.editTextDescricao);
        seekBarTempo = (SeekBar) findViewById(R.id.seekBarTempo);
        textViewTempo = (TextView) findViewById(R.id.textViewCep);
        editTextValor = (EditText) findViewById(R.id.editTextValor);
        spinnerEndereco = (Spinner) findViewById(R.id.spinnerEndereco);
        buttonCadastro = (Button) findViewById(R.id.buttonFinalizar);
        buttonNovoEndereco = (Button) findViewById(R.id.buttonNovoEndereco);
        imageButtonTipo = (ImageButton) findViewById(R.id.imageButtonTipo);
        imageButtonDescricao = (ImageButton) findViewById(R.id.imageButtonDescricao);
        imageButtonValor = (ImageButton) findViewById(R.id.imageButtonValor);
        imageButtonTempo = (ImageButton) findViewById(R.id.imageButtonTempo);


        tipo();
        endereco();

        editTextValor.addTextChangedListener(new Moeda(editTextValor));

        seekBarTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 15;
            @SuppressLint("WrongConstant")
            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(Filtro.this, ""+progress+"", Toast.LENGTH_LONG).show();
                textViewTempo.setText(""+progress+" hora(s)");
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

        /*buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valida()) {
                    tarefa = new Tarefa();
                    tarefa.setTitulo(editTextTitulo.getText().toString());
                    tarefa.setTipo(spinnerTipo.getSelectedItem().toString());
                    tarefa.setDescricao(editTextDescricao.getText().toString());
                    tarefa.setTempo(String.valueOf(seekBarTempo.getProgress()));
                    tarefa.setValor((editTextValor.getText().toString()));
                    tarefa.setEndereco(idEndereco.get(spinnerEndereco.getSelectedItemPosition() - 1));

                    tarefa.salvar(CadastroTarefa.this);

                    Intent intent = new Intent(CadastroTarefa.this, Pagamento.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CadastroTarefa.this, msg, Toast.LENGTH_SHORT).show();
                }
              }
        });*/


        buttonNovoEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CadastroEndereco cadastroEndereco = new CadastroEndereco();
                cadastroEndereco.setContext(CadastroTarefa.this);

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //toolbar.setTitle("Cadastro de endereço");

                fragmentTransaction.replace(R.id.fragment_cadastro, cadastroEndereco);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });


        imageButtonTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help("Tipo", "\nEscolha qual tipo de tarefa melhor define a sua:\n\nDoméstica: Tarefas que devem ser realizas em casa. EX: Cortar a grama, lavar o carro ou pintar a casa.\nExterna: Tarefas que devem ser realizadas na rua. EX: Pagar uma conta, ir à padaria ou farmácia comprar algo.\nServiço: Tarefas que devem funcionar como um trabalho. EX: Servir de segurança ou garçom por um dia.\nOutro: Outros tipos de tarefas que não se encaixam nas categorias acima.\n");
            }
        });

        imageButtonDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help("Descrição", "\nDescreva neste campo todos os detalhes sobre a tarefa.\n");
            }
        });

        imageButtonTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help("Tempo", "\nEscolha o tempo que algum usuário terá para aceitar sua tarefa e realizá-la\n");
            }
        });

        imageButtonValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help("Valor", "\nDigite o valor que deseja paga pagar para que outra pessoa realize está tarefa para você.\n");
            }
        });
    }

    public void tipo(){
        listaTipo.add("Doméstica");
        listaTipo.add("Externa");
        listaTipo.add("Serviço");
        listaTipo.add("Outro");

        ArrayAdapter<String> arrayAdapterTipo = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaTipo);
        arrayAdapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(arrayAdapterTipo);
    }

    public void endereco(){
        Preferencias preferencias = new Preferencias(CadastroTarefa.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("endereco").child(identificadorUsuarioLogado);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Endereco endereco = dados.getValue(Endereco.class);
                    String id = endereco.getId();
                    if(identificadorUsuarioLogado.equals(endereco.getId_usuario())){
                        String end = ""+endereco.getRua()+", "+endereco.getNumero()+"";
                        if(end.length() > 20){
                            end = end.substring(0, 20);
                            listaEndereco.add(end);
                            idEndereco.add(id);
                        }
                        else{
                            listaEndereco.add(end);
                            idEndereco.add(id);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listaEndereco.add("");

        ArrayAdapter<String> arrayAdapterEndereco = new ArrayAdapter<String>(CadastroTarefa.this, android.R.layout.simple_list_item_1, listaEndereco);
        arrayAdapterEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndereco.setAdapter(arrayAdapterEndereco);

        //spinnerEndereco.setSelection(listaEndereco.size());
    }

    private boolean valida(){
        boolean teste = true;
        msg = "Campos incorretos:";
        Util util = new Util();

        if(editTextTitulo.getText().length() > 75 || editTextTitulo.getText().equals(""))
        {
            msg += "\nTítulo inválido.";
            teste = false;
        }
        if(spinnerTipo.getSelectedItem().toString().equals("") || spinnerTipo.getSelectedItem().toString().equals(null))
        {
            msg += "\nSelecione um tipo de tarefa.";
            teste = false;
        }
        if(editTextDescricao.getText().length() > 255  || editTextDescricao.getText().equals(""))
        {
            msg += "\nDescrição inválida.";
            teste = false;
        }
        if(editTextValor.getText().length() > 8)
        {
            msg += "\nValor inválido.";
            teste = false;
        }
        if(editTextValor.getText().equals("")) {
            msg += "\nValor inválido.";
            teste = false;
        }
        else if (Float.parseFloat(editTextValor.getText().toString().substring(2, editTextValor.getText().length()).replace(",", ".")) > 500.00) {
            msg += "\nValor inválido.";
            teste = false;
        }
        if(spinnerEndereco.getSelectedItem().toString().equals("") || spinnerEndereco.getSelectedItem().toString().equals(null))
        {
            msg += "\nSelecione um endereço de tarefa.";
            teste = false;
        }

        return teste;
    }

    public void buy( View view ){
        if(valida()) {
            new MDDialog.Builder(this)
                    .setTitle("Pagamento")
                    .setContentView(R.layout.payment)
                    .setNegativeButton("Cancelar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButton("Finalizar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        /*View root = v.getRootView();

                        CreditCard creditCard = new CreditCard( MainActivity.this );
                        creditCard.setCardNumber( getViewContent( root, R.id.card_number ) );
                        creditCard.setName( getViewContent( root, R.id.name ) );
                        creditCard.setMonth( getViewContent( root, R.id.month ) );
                        creditCard.setYear( getViewContent( root, R.id.year ) );
                        creditCard.setCvv( getViewContent( root, R.id.cvv ) );
                        creditCard.setParcels( Integer.parseInt( getViewContent( root, R.id.parcels ) ) );

                        getPaymentToken( creditCard );*/

                            tarefa = new Tarefa();
                            tarefa.setTitulo(editTextTitulo.getText().toString());
                            tarefa.setTipo(spinnerTipo.getSelectedItem().toString());
                            tarefa.setDescricao(editTextDescricao.getText().toString());
                            tarefa.setTempo(String.valueOf(seekBarTempo.getProgress()));
                            tarefa.setValor((editTextValor.getText().toString()));
                            tarefa.setEndereco(idEndereco.get(spinnerEndereco.getSelectedItemPosition() - 1));

                            tarefa.salvar(CadastroTarefa.this);

                            Intent intent = new Intent(CadastroTarefa.this, TarefaCadastrada.class);
                            startActivity(intent);

                        }
                    })
                    .create()
                    .show();
        }
        else{
            Toast.makeText(CadastroTarefa.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private String getViewContent( View root, int id ){
        EditText field = (EditText) root.findViewById(id);
        return field.getText().toString();
    }

    private void getPaymentToken( CreditCard creditCard ){
        /*WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.addJavascriptInterface( creditCard, "Android" );
        webView.loadUrl("file:///android_asset/index.html");*/
    }

    private void help(String titulo, String msg){
        new AlertDialog.Builder(CadastroTarefa.this)
                .setTitle(titulo)
                .setMessage(msg)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .show();
    }
}
