package com.connectask.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectask.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuSenha extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private Button buttonRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonRecuperar = (Button) findViewById(R.id.buttonRecuperar);

        buttonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset(v);
                Toast.makeText(
                        EsqueceuSenha.this,
                        "Funcionalidade desabilitada no momento.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

    }

    public void reset(View view){
        firebaseAuth
                .sendPasswordResetEmail( editTextEmail.getText().toString() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            editTextEmail.setText("");
                            Toast.makeText(
                                    EsqueceuSenha.this,
                                    "Recuperação de acesso iniciada. Email enviado.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(EsqueceuSenha.this, Login.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(
                                    EsqueceuSenha.this,
                                    "Falhou! Tente novamente",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }
}
