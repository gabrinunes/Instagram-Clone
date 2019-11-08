package gabrielcunha.cursoandroid.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.model.Postagem;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private TextView textQtdCurtidas,textDescricao,textComentarios,textPerfilPostagem;
    private CircleImageView imagePerfilPostagem;
    private ImageView imagePostagemSelecionada;
    private Usuario usuarioSelecionado;
    private Postagem postagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        inicializarComponentes();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar Postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuario");
            postagemSelecionada = (Postagem) bundle.getSerializable("postagem");

            //Exibir foto no perfil
            if(usuarioSelecionado.getCaminhoFoto() !=null){
                Uri uri = Uri.parse(usuarioSelecionado.getCaminhoFoto());
                Glide.with(VisualizarPostagemActivity.this)
                        .load(uri)
                        .into(imagePerfilPostagem);
            }

            //Exibir foto na postagem
            if(postagemSelecionada.getCaminhoFoto() !=null){
                Uri uri = Uri.parse(postagemSelecionada.getCaminhoFoto());
                Glide.with(VisualizarPostagemActivity.this)
                        .load(uri)
                        .into(imagePostagemSelecionada);
            }

            textDescricao.setText(postagemSelecionada.getDescricao());
            textPerfilPostagem.setText(usuarioSelecionado.getNome());


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void inicializarComponentes() {
        imagePostagemSelecionada = findViewById(R.id.imagemPostagemSelecionada);
        imagePerfilPostagem = findViewById(R.id.imagePerfilPostagem);
        textComentarios = findViewById(R.id.textVisualizarComentariosPostagem);
        textQtdCurtidas = findViewById(R.id.textQtdCurtidas);
        textDescricao = findViewById(R.id.textDescricaoPostagem);
        textPerfilPostagem = findViewById(R.id.textPerfilPostagem);
    }
}
