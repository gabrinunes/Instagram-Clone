package gabrielcunha.cursoandroid.instagram.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String id;
    private String caminhoFoto;

    public Usuario() {
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
