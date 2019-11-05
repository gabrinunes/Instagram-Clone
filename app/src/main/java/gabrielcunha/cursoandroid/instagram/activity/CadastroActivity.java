package gabrielcunha.cursoandroid.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.helper.UsuarioFirebase;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText NomeCadastro,EmailCadastro,SenhaCadastro;
    private Button buttonCadastro;
    private ProgressBar progressBar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        incializarComponentes();
        progressBar.setVisibility(View.GONE);
    }

    private void incializarComponentes() {
        NomeCadastro = findViewById(R.id.editCadastroNome);
        EmailCadastro = findViewById(R.id.editCadastroEmail);
        SenhaCadastro = findViewById(R.id.editCadastroSenha);
        buttonCadastro = findViewById(R.id.buttonCadastroEntrar);
        progressBar = findViewById(R.id.progressCadastro);

        NomeCadastro.requestFocus();
    }

    public void cadastroUsuario(final Usuario usuario){
        progressBar.setVisibility(View.VISIBLE);
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    try{
                        progressBar.setVisibility(View.GONE);

                        //Salvar dados no firebase
                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);
                        exibirMensagem("Sucesso ao cadastrar");
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        usuario.salvar();

                        //Salvar dados no profile do firebase
                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    progressBar.setVisibility(View.GONE);

                    String erroExcecao="";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esta conta já foi cadastrada";
                    }catch (Exception e){
                        erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    exibirMensagem(erroExcecao);
                }
            }
        });
    }

    public void validarCadastroUsuario(View view){
        String textNome = NomeCadastro.getText().toString();
        String textEmail = EmailCadastro.getText().toString();
        String textSenha = SenhaCadastro.getText().toString();

        if(!textNome.isEmpty()){
            if(!textEmail.isEmpty()){
              if(!textSenha.isEmpty()){
                  usuario = new Usuario();
                  usuario.setNome(textNome);
                  usuario.setNomeLowerCase(textNome);
                  usuario.setEmail(textEmail);
                  usuario.setSenha(textSenha);

                  cadastroUsuario(usuario);
              }else{
                exibirMensagem("Preencha a senha");
              }
            }else{
                exibirMensagem("Preencha o email!!");
            }
        }else{
            exibirMensagem("Preencha o nome!");
        }
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this,texto,Toast.LENGTH_SHORT).show();
    }
}
