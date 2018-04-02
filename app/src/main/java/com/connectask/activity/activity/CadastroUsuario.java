package com.connectask.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.classes.Cpf;
import com.connectask.activity.classes.Util;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.classes.Base64Custom;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroUsuario extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextCpf;
    private EditText editTextSenha;
    private Button buttoCadastrar;

    private DatabaseReference firebase;

    private Usuario usuario;

    private String msg;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNome = (EditText) findViewById(R.id.editTextNome);
        editTextCpf = (EditText) findViewById(R.id.editTextCpf);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        buttoCadastrar = (Button) findViewById(R.id.buttonFinalizar);


        editTextCpf.addTextChangedListener(Cpf.insert(editTextCpf,1));

        buttoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(valida()){
                    usuario = new Usuario();
                    usuario.setEmail(editTextEmail.getText().toString());
                    usuario.setNome(editTextNome.getText().toString());
                    if(editTextCpf.length() == 11){
                        new AlertDialog.Builder(CadastroUsuario.this)
                                .setTitle("Erro")
                                .setMessage("CPF inválido")
                                .show();
                    }
                    else{
                        usuario.setCpf(editTextCpf.getText().toString());
                    }

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
                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail().toString());
                    usuario.setId(identificadorUsuario);
                    usuario.salvar(identificadorUsuario);

                    firebase = ConfiguracaoFirebase.getFirebase();
                    //firebase.child("usuarios").child(identificadorUsuario).child("id").setValue(identificadorUsuario);

                    Preferencias preferencias = new Preferencias(CadastroUsuario.this);
                    preferencias.salvarDados(identificadorUsuario);

                    abrirLoginUsuario();

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
        if(editTextCpf.getText().length() != 14)
        {
            msg += "\nCPF inválido.";
            teste = false;
        }
        if(util.isPasswordValid(editTextSenha.getText().toString().trim()))
        {
            msg += "\nSenha inválido.";
            teste = false;
        }

        return teste;
    }


}
