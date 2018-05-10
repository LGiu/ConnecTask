package com.connectask.activity.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class Introducao {

    private Context context;
    private DatabaseReference firebase;
    private DatabaseReference firebase2;

    private int novo = 0;

    public Introducao(final Context context){
        this.context = context;

        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuario usuario = dados.getValue(Usuario.class);
                    if(identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail().toString())) && usuario.getIntroducao().equals("0")){

                        introducao1();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setarIndroducao()
    {
        Preferencias preferencias = new Preferencias(context);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase2 = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorUsuarioLogado);
        firebase2.child("introducao").setValue("1");
    }

    private void introducao1(){

        new AlertDialog.Builder(context)
                .setTitle("Introdução")
                .setMessage("\nÉ novo por aqui? Que tal uma breve introdução para saber como funciona o ConnecTask.\n")
                .setNegativeButton("NÃO, OBRIGADO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setarIndroducao();
                    }
                })
                .setPositiveButton("VAMOS LÁ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                introducao2();
                                dialogInterface.dismiss();
                            }
                        })
                .show();
    }

    private void introducao2(){
        new AlertDialog.Builder(context)
                .setTitle("O que é o ConnecTask?")
                .setMessage("\nO ConnecTask é um aplicativo onde você pode cadastrar suas tarefas para que outra pessoas possam realiza-lás. Ou você pode realizar uma tarefa já cadastrada ganhando a devida remuneração por isso.\n")
                .setPositiveButton("PROXIMO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                introducao3();
                                dialogInterface.dismiss();
                            }
                        })
                .show();
    }

    private void introducao3(){
        new AlertDialog.Builder(context)
                .setTitle("Como cadastrar uma tarefa?")
                .setMessage("\nPara cadastrar uma tarefa basta você clicar no botão contido no canto inferior direito da tela pincipal e preencher os dados. Após cadastrada, a tarefa ficará disponível para que outro usuário possa realiza-lá.\n")
                .setPositiveButton("PROXIMO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                introducao4();
                                dialogInterface.dismiss();
                            }
                        })
                .show();
    }

    private void introducao4(){
        new AlertDialog.Builder(context)
                .setTitle("Como realizar uma tarefa?")
                .setMessage("\nPara realizar uma basta acessar o página inicial, selecionar uma tarefa, clicar no botão Mais Detalhes e em seguida no botão Realizar.\n")
                .setPositiveButton("PROXIMO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                introducao5();
                                dialogInterface.dismiss();

                            }
                        })
                .show();
    }

    private void introducao5(){
        new AlertDialog.Builder(context)
                .setTitle("Tarefas em progresso")
                .setMessage("\nSempre que você possuir uma tarefa em andamento aparecerá uma notificação no canto superior direito de sua tela.\n")
                .setPositiveButton("PROXIMO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                introducao6();
                                dialogInterface.dismiss();

                            }
                        })
                .show();
    }

    private void introducao6(){
        new AlertDialog.Builder(context)
                .setTitle("Pronto")
                .setMessage("\nAgora que você já sabe mais sobre o aplicativo está pronto para iniciar.\n")
                .setPositiveButton("VAMOS LÁ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setarIndroducao();
                                dialogInterface.dismiss();
                            }
                        })
                .show();
    }
}
