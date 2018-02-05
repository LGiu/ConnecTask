package com.connectask.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.classes.Base64Custom;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonEntrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Permissao.validaPermissao(1, this, permissoesNecessarias);

        verificarUsuarioLogado();


        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
        buttonEntrar = (Button) findViewById(R.id.buttonFinalizar);

        editTextSenha.setTypeface(Typeface.DEFAULT);
        editTextSenha.setTransformationMethod(new PasswordTransformationMethod());

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario();

                usuario.setEmail(editTextEmail.getText().toString());
                usuario.setSenha(editTextSenha.getText().toString());
                validarLogin();
            }
        });

    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();
        if (autenticacao.getCurrentUser() != null){
            abrirHome();
        }
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Preferencias preferencias = new Preferencias(Login.this);
                    String identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    preferencias.salvarDados(identificadorUsuarioLogado);

                    abrirHome();

                }
                else{
                    String erroExcecao = "";

                    try {
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "Este e-mail não está cadastrado ou está incorreto";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha incorreta";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "O e-mail já esta em uso";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o login";
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, erroExcecao, Toast.LENGTH_SHORT).show();                }
            }
        });

    }

    public void abrirHome(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }


    public void esqueceuSenha(View view){

    }

    public void cadastrar(View view){
        Intent intent = new Intent(Login.this, CadastroUsuario.class);
        startActivity(intent);
    }

}
