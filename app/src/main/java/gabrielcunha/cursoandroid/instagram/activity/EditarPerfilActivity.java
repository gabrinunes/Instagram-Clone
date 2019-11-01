package gabrielcunha.cursoandroid.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class EditarPerfilActivity extends AppCompatActivity {

    private Button editButtonPerfil;
    private EditText editTextNome,editTextEmail;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializarComponentes();

        //Configura toolbar

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        editButtonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textNome = editTextNome.getText().toString();
                String textEmail = editTextEmail.getText().toString();
                if(!textNome.isEmpty()){
                    usuario = new Usuario();
                    usuario.setNome(textNome);
                    usuario.setEmail(textEmail);
                    usuario.salvar();
                }else{
                    exibirMensagem("não pode campo em branco");
                }
            }
        });
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this,texto,Toast.LENGTH_SHORT);
    }

    private void inicializarComponentes() {
        editButtonPerfil = findViewById(R.id.buttonEditPerfil);
        editTextNome = findViewById(R.id.textEditNome);
        editTextEmail = findViewById(R.id.textEditEmail);
    }
}
