package com.connectask.activity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.connectask.R;
import com.connectask.activity.Fragments.EditarPerfil;
import com.connectask.activity.classes.Preferencias;
import com.connectask.activity.config.ConfiguracaoFirebase;
import com.connectask.activity.model.Foto;
import com.connectask.activity.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static com.connectask.activity.classes.Base64Custom.codificarBase64;

public class Perfil extends AppCompatActivity {

    private DatabaseReference firebase;

    private ImageView imageViewFoto;
    private TextView textViewNome;
    private TextView textViewEmail;
    private TextView textViewCpf;
    private TextView textViewTelefone;

    protected Uri filePath;
    private final int PICK_IMAGE = 1234;
    public FirebaseStorage storage = FirebaseStorage.getInstance();
    public StorageReference storageReference = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

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

        imageViewFoto = (ImageView) findViewById(R.id.imageViewFoto);
        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewCpf = (TextView) findViewById(R.id.textViewCpf);
        textViewTelefone = (TextView) findViewById(R.id.textViewTelefone);

        Preferencias preferencias = new Preferencias(Perfil.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("usuarios");

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    if(identificadorUsuarioLogado.equals(codificarBase64(usuario.getEmail()))){
                        textViewNome.setText(usuario.getNome());
                        textViewEmail.setText(usuario.getEmail());
                        textViewCpf.setText(usuario.getCpf());
                        textViewTelefone.setText(usuario.getTelefone());
                        setarImagem();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE);

            }
        });*/

        textViewNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfil editarPerfil = new EditarPerfil();
                editarPerfil.setCampos("Nome", textViewNome.getText().toString());

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.fragment_perfil, editarPerfil).commit();

            }
        });

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfil editarPerfil = new EditarPerfil();
                editarPerfil.setCampos("Nome", textViewNome.getText().toString());

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.fragment_perfil, editarPerfil);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        textViewCpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfil editarPerfil = new EditarPerfil();
                editarPerfil.setCampos("CPF", textViewCpf.getText().toString());

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.fragment_perfil, editarPerfil);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        textViewTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfil editarPerfil = new EditarPerfil();
                editarPerfil.setCampos("Telefone", textViewTelefone.getText().toString());

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.fragment_perfil, editarPerfil);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewFoto.setImageBitmap(bitmap);
                uploadImagem();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImagem() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Carregando...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Perfil.this, "Salvo", Toast.LENGTH_SHORT).show();
                            Foto foto = new Foto(Perfil.this);
                            foto.setId_imagem(UUID.randomUUID().toString());
                            foto.salvar();
                            setarImagem();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Perfil.this, "Falha "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Carregado "+(int)progress+"%");
                        }
                    });
        }
    }

    private void setarImagem(){
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("fotos");

        Preferencias preferencias = new Preferencias(Perfil.this);
        final String identificadorUsuarioLogado = preferencias.getIdentificado();

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Foto foto = dados.getValue(Foto.class);

                    if(identificadorUsuarioLogado.equals(foto.getId_usuario())){
                        StorageReference storageRef = storage.getReference();
                        StorageReference pathReference = storageRef.child("images/"+foto.getId_imagem()+"");

//https://androidjson.com/upload-image-to-firebase-storage/
                        //mageViewFoto.setImageResource();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}


