package gabrielcunha.cursoandroid.instagram.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import gabrielcunha.cursoandroid.instagram.helper.UsuarioFirebase;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    private Button editButtonPerfil;
    private CircleImageView editImagePerfil;
    private TextView textAlterarFoto;
    private EditText editTextNome,editTextEmail;
    private Usuario usuarioLogado;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageRef;
    private String urlImagemSelecionado="";
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializarComponentes();

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        idUsuario = UsuarioFirebase.getIdUsuario();

        //Configura toolbar

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        recuperaDadosPerfil();

        editButtonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNome.getText().toString();
                UsuarioFirebase.atualizarNomeUsuario(nome);

                //Atualizar nome no banco de dados
                usuarioLogado.setNome(nome);
                usuarioLogado.atualizar();
                exibirMensagem("Dados alterados com sucesso!");
            }
        });

        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {

                //Seleção apenas da galeria

                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                        break;
                }

                if(imagem!=null){

                    editImagePerfil.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte[] dadosImagem = baos.toByteArray();
                    //Salvar imagem no firebase

                    StorageReference imagemRef = storageRef
                            .child("imagens")
                            .child("perfil")
                            .child(idUsuario + "jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            ExibirMensagem("Erro ao fazer upload da imagem");

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            urlImagemSelecionado = taskSnapshot.getDownloadUrl().toString();
                            ExibirMensagem("Sucesso ao fazer upload da imagem");

                            Uri url = taskSnapshot.getDownloadUrl();
                            atualizarFotoPerfil(url);
                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void ExibirMensagem(String texto) {
        Toast.makeText(EditarPerfilActivity.this,texto,Toast.LENGTH_SHORT).show();
    }

    private void recuperaDadosPerfil() {
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        editTextNome.setText(usuario.getDisplayName());
        editTextEmail.setText(usuario.getEmail());
        Uri url = usuario.getPhotoUrl();

        if(url!=null){
            Glide.with(EditarPerfilActivity.this)
                  .load(url)
                  .into(editImagePerfil);
        }else{
            editImagePerfil.setImageResource(R.drawable.avatar);
        }
    }

    private void atualizarFotoPerfil(Uri url){
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if(retorno){
            usuarioLogado.setCaminhoFoto(url.toString());
            usuarioLogado.atualizar();
            ExibirMensagem("Sua foto foi atualizada!");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(EditarPerfilActivity.this,texto,Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes() {
        editButtonPerfil = findViewById(R.id.buttonEditPerfil);
        editTextNome = findViewById(R.id.textEditNomePerfil);
        editTextEmail = findViewById(R.id.textEditEmailPerfil);
        editImagePerfil = findViewById(R.id.imageEditarPerfil);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editTextEmail.setFocusable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
