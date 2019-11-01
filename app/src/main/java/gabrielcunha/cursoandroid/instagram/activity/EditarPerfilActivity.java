package gabrielcunha.cursoandroid.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.helper.UsuarioFirebase;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    private Button editButtonPerfil;
    private CircleImageView editImagePerfil;
    private TextView textAlterarFoto;
    private EditText editTextNome,editTextEmail;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializarComponentes();

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

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


    }

    private void recuperaDadosPerfil() {
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        editTextNome.setText(usuario.getDisplayName());
        editTextEmail.setText(usuario.getEmail());
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(EditarPerfilActivity.this,texto,Toast.LENGTH_SHORT);
    }

    private void inicializarComponentes() {
        editButtonPerfil = findViewById(R.id.buttonEditPerfil);
        editTextNome = findViewById(R.id.textEditNomePerfil);
        editTextEmail = findViewById(R.id.textEditEmailPerfil);
        editImagePerfil = findViewById(R.id.imageEditarPerfil);
        textAlterarFoto = findViewById(R.id.textCadastrar);
        editTextEmail.setFocusable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
