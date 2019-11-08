package gabrielcunha.cursoandroid.instagram.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.activity.EditarPerfilActivity;
import gabrielcunha.cursoandroid.instagram.activity.PerfilAmigoActivity;
import gabrielcunha.cursoandroid.instagram.adapter.AdapterGrid;
import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import gabrielcunha.cursoandroid.instagram.helper.UsuarioFirebase;
import gabrielcunha.cursoandroid.instagram.model.Postagem;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    private GridView gridViewPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private Button buttonAcaoPerfil;
    private Usuario usuarioLogado;
    private DatabaseReference firebaseRef;
    private DatabaseReference usuarioRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference postagensUsuarioRef;
    private ValueEventListener valueEventListenerPerfil;
    private AdapterGrid adapterGrid;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configuracoes Inicias
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        usuarioRef = firebaseRef.child("usuarios");
        //Configurações dos componentes
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBar = view.findViewById(R.id.progressPerfil);
        imagePerfil = view.findViewById(R.id.imageEditarPerfil);
        textPublicacoes = view.findViewById(R.id.textPublicacoes);
        textSeguidores = view.findViewById(R.id.textSeguidores);
        textSeguindo = view.findViewById(R.id.textView6);
        buttonAcaoPerfil = view.findViewById(R.id.buttonAcaoPerfil);

        //Configurar referencia postagens usuario
        postagensUsuarioRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("postagens")
                .child(usuarioLogado.getId());

        //Exibir foto no perfil
        if (usuarioLogado.getCaminhoFoto() != null) {
            Uri uri = Uri.parse(usuarioLogado.getCaminhoFoto());
            Glide.with(getActivity())
                    .load(uri)
                    .into(imagePerfil);
        }


        //Recuperar usuário logado


        //Abre edição de perfil
        buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });
        incializarImageLoader();
        carregarFotosPostagem();
        return view;

    }

    public void incializarImageLoader(){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void carregarFotosPostagem(){

        //Recupera as fotos postadas pelo usuario
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Configurar o tamanho do grid
                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid /3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlFotos = new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Postagem postagem = ds.getValue(Postagem.class);
                    urlFotos.add(postagem.getCaminhoFoto());
                }
                int qtdPostagem = urlFotos.size();
                textPublicacoes.setText(String.valueOf(qtdPostagem));

                //Configurar adapter
                adapterGrid = new AdapterGrid(getActivity(),R.layout.grid_postagem,urlFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuarioLogado();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioLogadoRef.removeEventListener(valueEventListenerPerfil);
    }

    private void recuperarDadosUsuarioLogado() {

        usuarioLogadoRef = usuarioRef.child(usuarioLogado.getId());
        valueEventListenerPerfil = usuarioLogadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                //String postagens = String.valueOf(usuario.getPostagens());
                String seguindo = String.valueOf(usuario.getSeguindo());
                String seguidores = String.valueOf(usuario.getSeguidores());

                //Configura valores recuperados
                //textPublicacoes.setText(postagens);
                textSeguidores.setText(seguidores);
                textSeguindo.setText(seguindo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
