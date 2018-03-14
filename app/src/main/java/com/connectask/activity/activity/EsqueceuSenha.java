package com.connectask.activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.connectask.R;

public class EsqueceuSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        /*public void reset( View view ){
            firebaseAuth
                    .sendPasswordResetEmail( email.getText().toString() )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if( task.isSuccessful() ){
                                email.setText("");
                                Toast.makeText(
                                        ResetActivity.this,
                                        "Recuperação de acesso iniciada. Email enviado.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                            else{
                                Toast.makeText(
                                        ResetActivity.this,
                                        "Falhou! Tente novamente",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });
        }*/

    }
}
