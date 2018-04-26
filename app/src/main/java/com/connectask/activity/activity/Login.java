package com.connectask.activity.activity;

import android.app.ProgressDialog;
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
import com.connectask.activity.classes.ValoresFiltro;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.classes.Base64Custom;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.model.ProcessoTarefa;
import com.connectask.activity.model.Sessao;
import com.connectask.activity.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class Login extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonEntrar;
    private Usuario usuario;

    private ProgressDialog pDialog;

    private FirebaseAuth autenticacao;

    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        excluir();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextSenha = (EditText) findViewById(R.id.editTextCpf);
        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);

        editTextSenha.setTypeface(Typeface.DEFAULT);
        editTextSenha.setTransformationMethod(new PasswordTransformationMethod());

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!editTextEmail.getText().toString().equals("")) && (!editTextSenha.getText().toString().equals("")))
                {
                    usuario = new Usuario();

                    usuario.setEmail(editTextEmail.getText().toString());
                    usuario.setSenha(editTextSenha.getText().toString());

                    validarLogin();
                }
                else{
                    Toast.makeText(Login.this, "Digite o Login e a Senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();
        if (autenticacao.getCurrentUser() != null){
            Sessao sessao = new Sessao();
            sessao.salvar(this);

            ValoresFiltro valoresFiltro = new ValoresFiltro(Login.this);
            valoresFiltro.limparDados();

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

                    Toast.makeText(Login.this, erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirHome(){
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }


    public void esqueceuSenha(View view){
        Intent intent = new Intent(Login.this, EsqueceuSenha.class);
        startActivity(intent);
    }

    public void cadastrar(View view){
        Intent intent = new Intent(Login.this, CadastroUsuario.class);
        startActivity(intent);
    }

    private void excluir()
    {
        final Preferencias preferencias = new Preferencias(Login.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        if(!identificadorUsuarioLogado.equals(null) && !identificadorUsuarioLogado.equals("")) {
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Por favor, aguarde...");
            pDialog.setCancelable(false);
            pDialog.show();

            firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");

            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Usuario usuario = dados.getValue(Usuario.class);

                        if (identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail().toString()))) {
                            if (usuario.getStatus().equals("2")) {
                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase.child("usuarios").child(identificadorUsuarioLogado).setValue(null);

                                autenticacao = ConfiguracaoFirebase.getFirebaseAuteticacao();
                                autenticacao.getCurrentUser().delete();

                                preferencias.limpaDados();

                            }
                        }
                    }
                    verificarUsuarioLogado();
                    pDialog.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
