package gabrielcunha.cursoandroid.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;

public class Usuario implements Serializable {
    private String nome;
    private String nomeLowerCase;
    private String email;
    private String senha;
    private String id;
    private String caminhoFoto;
    private int seguidores =0;
    private int seguindo =0;
    private int postagens =0;

    public Usuario() {
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getPostagens() {
        return postagens;
    }

    public void setPostagens(int postagens) {
        this.postagens = postagens;
    }

    public String getNomeLowerCase() {
        return nomeLowerCase.toLowerCase();
    }

    public void setNomeLowerCase(String nomeLowerCase) {
        this.nomeLowerCase = nomeLowerCase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseRef.child("usuarios")
                   .child(getId())
                   .setValue(this);
    }

    public void atualizar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getId());
        Map<String,Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public void atualizarQtdePostagem(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getId());
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("postagens",getSeguindo());
        usuarioRef.updateChildren(dados);
    }

    public Map<String,Object> converterParaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("id",getId());
        usuarioMap.put("caminhoFoto",getCaminhoFoto());
        usuarioMap.put("seguidores",getSeguidores());
        usuarioMap.put("postagens",getPostagens());
        usuarioMap.put("seguindo",getSeguindo());

        return usuarioMap;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
