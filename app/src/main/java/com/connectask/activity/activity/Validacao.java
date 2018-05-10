package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.Celular;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Token;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;

import java.util.Random;

public class Validacao extends AppCompatActivity {

    private EditText editTextCodigo;
    private Button buttonValidar;
    private Button buttonGerar;

    private DatabaseReference firebase;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);

        editTextCodigo = (EditText) findViewById(R.id.editTextCodigo);
        buttonValidar = (Button) findViewById(R.id.buttonValidar);
        buttonGerar = (Button) findViewById(R.id.buttonGerar);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(editTextCodigo, simpleMaskFormatter);

        editTextCodigo.addTextChangedListener(mascaraCodigoValidacao);

        buttonValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token tokenClass = new Token(Validacao.this);
                String token = tokenClass.getToken().toString();

                if(editTextCodigo.getText().toString().equals(token)){
                    Preferencias preferencias = new Preferencias(Validacao.this);
                    final String identificadorUsuarioLogado = preferencias.getIdentificado();

                    firebase = ConfiguracaoFirebase.getFirebase();
                    firebase.child("usuarios").child(identificadorUsuarioLogado).child("status").setValue("1");

                    abrirLoginUsuario();
                }
                else{
                    Toast.makeText(Validacao.this, "Token inválido!", Toast.LENGTH_SHORT).show();
                    count++;
                    if(count == 3){
                        Intent intent = new Intent(Validacao.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        buttonGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Token tokenClass = new Token(Validacao.this);
                String tokenGerado = gerarToken();
                tokenClass.salvarToken(tokenGerado);

                Celular celular = new Celular(Validacao.this);

                if(envioSms("+55" + celular.getTelefone().toString(), tokenGerado))
                {
                    Toast.makeText(Validacao.this, "Novo token enviado!", Toast.LENGTH_SHORT).show();
                    editTextCodigo.setText("");
                }
                else {
                    Toast.makeText(Validacao.this, "Problema ao enviar SMS de validação. Tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(Validacao.this, Login.class);
        startActivity(intent);
        finish();
    }

    private String gerarToken(){
        Random random = new Random();
        int numeroRandomico = random .nextInt(99999-10000)+10000;
        String token = String.valueOf(numeroRandomico);
        return token;
    }

    private boolean envioSms(String telefone, String token)
    {
        try {
            String mensagem = "ConnecTask Código de Confirmação:\n"+token+"";

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
