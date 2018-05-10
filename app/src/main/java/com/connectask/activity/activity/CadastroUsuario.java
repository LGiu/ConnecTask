package com.connectask.activity.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.Base64Custom;
import com.connectask.activity.classes.Celular;
import com.connectask.activity.classes.Cpf;
import com.connectask.activity.classes.Permissao;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.classes.Telefone;
import com.connectask.activity.classes.Token;
import com.connectask.activity.classes.Util;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CadastroUsuario extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextCpf;
    private EditText editTextSenha;
    private EditText editTextTelefone;
    private Button buttoCadastrar;

    private DatabaseReference firebase;

    private Usuario usuario;

    private String msg;
    private String telefoneFormatado;

    private boolean testeTelefone = false;

    private FirebaseAuth autenticacao;

    private FirebaseUser usuarioFirebase;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Permissao.validaPermissao(1, this, permissoesNecessarias);


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        editTextTelefone = (EditText) findViewById(R.id.editTextTelefone);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        buttoCadastrar = (Button) findViewById(R.id.buttonFinalizar);


        editTextCpf.addTextChangedListener(Cpf.insert(editTextCpf,1));
        editTextTelefone.addTextChangedListener(Telefone.insert("(##)#####-####", editTextTelefone));

        buttoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telefoneFormatado = editTextTelefone.getText().toString().replace("(", "").replace(")", "").replace("-", "").replace(" ", "");

                if(valida())
                {
                    usuario = new Usuario();
                    usuario.setEmail(editTextEmail.getText().toString());
                    usuario.setNome(editTextNome.getText().toString());
                    usuario.setCpf(editTextCpf.getText().toString());
                    usuario.setTelefone(telefoneFormatado);
                    usuario.setSenha(editTextSenha.getText().toString());

                    cadastrarUsuario();
                }
                else{
                    Toast.makeText(CadastroUsuario.this, msg, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    usuarioFirebase = task.getResult().getUser();

                    Token tokenClass = new Token(CadastroUsuario.this);
                    String tokenGerado = gerarToken();
                    tokenClass.salvarToken(tokenGerado);

                    Celular celular = new Celular(CadastroUsuario.this);
                    celular.salvarTelefone(telefoneFormatado);

                    if(envioSms("+55" + telefoneFormatado, tokenGerado))
                    {
                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail().toString());
                        usuario.setId(identificadorUsuario);
                        usuario.salvar(identificadorUsuario);

                        Preferencias preferencias = new Preferencias(CadastroUsuario.this);
                        preferencias.salvarDados(identificadorUsuario);

                        Intent intent = new Intent(CadastroUsuario.this, Validacao.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(CadastroUsuario.this, "Problema ao enviar SMS de validação. Tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo mais de 5 caracteres e com letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digita é inválido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "O e-mail já esta em uso";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuario.this, Login.class);
        startActivity(intent);
        finish();
    }

    private boolean valida(){
        boolean teste = true;
        msg = "Campos incorretos:";

        Util util = new Util();

        if(editTextNome.getText().length() > 75 || editTextNome.getText().length() == 0)
        {
            msg += "\nNome inválido.";
            teste = false;
        }
        //Validar se existe telefone igual
        if(editTextCpf.getText().length() != 14)
        {
            msg += "\nCPF inválido.";
            teste = false;
        }
        if(telefoneFormatado.length() != 10 && telefoneFormatado.length() != 11)
        {
            msg += "\nTelefone inválido.";
            teste = false;
        }

        validaTelefone();
        if(testeTelefone)
        {
            msg += "\nTelefone já cadastrado";
            teste = false;
        }

        if(editTextSenha.getText().toString().length() < 6 && editTextSenha.getText().toString().length() > 20)
        {
            msg += "\nSenha inválido.\nA senha deve conter no mínimo 6 e no máximo 20 dígitos.";
            teste = false;
        }

        return teste;
    }

    private void validaTelefone(){
        Preferencias preferencias = new Preferencias(CadastroUsuario.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(telefoneFormatado.equals(usuario.getTelefone()))
                    {
                        testeTelefone = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
