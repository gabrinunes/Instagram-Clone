package gabrielcunha.cursoandroid.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Button buttonAcaoPerfil;
    private CircleImageView imagePerfil;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioAmigoRef;
    private ValueEventListener valueEventListenerPerfilAmigo;
    private TextView textPublicacoes,textSeguidores,textSeguindo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configuracoes inicias
        usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios");

        incializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        //Recuperar usuario selecionado
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
          usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

          //Configurar o nome do usuário na toolbar
            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

          //Exibir foto no perfil
            if(usuarioSelecionado.getCaminhoFoto() !=null){
                Uri uri = Uri.parse(usuarioSelecionado.getCaminhoFoto());
                Glide.with(PerfilAmigoActivity.this)
                        .load(uri)
                        .into(imagePerfil);
            }

        }
    }

    private void incializarComponentes() {
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        imagePerfil = findViewById(R.id.imageEditarPerfil);
        buttonAcaoPerfil.setText("Seguir");
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textView6);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAmigo);
    }

    private void recuperarDadosPerfilAmigo(){

        usuarioAmigoRef = usuarioRef.child(usuarioSelecionado.getId());
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                String postagens = String.valueOf(usuario.getPostagens());
                String seguindo = String.valueOf(usuario.getSeguindo());
                String seguidores = String.valueOf(usuario.getSeguidores());

                //Configura valores recuperados
                textPublicacoes.setText(postagens);
                textSeguidores.setText(seguidores);
                textSeguindo.setText(seguindo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
