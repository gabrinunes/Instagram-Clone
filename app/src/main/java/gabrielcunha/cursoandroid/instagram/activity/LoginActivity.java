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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import gabrielcunha.cursoandroid.instagram.R;
import gabrielcunha.cursoandroid.instagram.helper.ConfiguracaoFirebase;
import gabrielcunha.cursoandroid.instagram.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText EmailCadastro,SenhaCadastro;
    private ProgressBar progressBar;
    private Button buttonCadastro;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        incializarComponentes();
        progressBar.setVisibility(View.GONE);
        verificarUsuarioLogado();
    }

    private void incializarComponentes() {
        EmailCadastro = findViewById(R.id.editLoginEmail);
        SenhaCadastro = findViewById(R.id.editLoginSenha);
        buttonCadastro = findViewById(R.id.buttonLoginEntrar);
        progressBar = findViewById(R.id.progressLogin);

        EmailCadastro.requestFocus();
    }

    public void verificarUsuarioLogado(){
        if(autenticacao.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }


    public void validarLogin(View view){
        String textEmail = EmailCadastro.getText().toString();
        String textSenha = SenhaCadastro.getText().toString();

        if(!textEmail.isEmpty()){
          if(!textSenha.isEmpty()){
              usuario = new Usuario();
              usuario.setEmail(textEmail);
              usuario.setSenha(textSenha);

              loginUsuario(usuario);
          }else{
              exibirMensagem("Preencha a senha!");
            }
        }else{
            exibirMensagem("Preencha o email!");
        }

    }

    public void loginUsuario(Usuario usuario) {
        progressBar.setVisibility(View.VISIBLE);
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  progressBar.setVisibility(View.GONE);
                  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                  finish();
              }else{
                  progressBar.setVisibility(View.GONE);
                  exibirMensagem("Erro ao fazer login!!");
                  String excecao="";
                  try {
                      throw task.getException();
                  }catch(FirebaseAuthInvalidUserException e) {
                      excecao = "Usuário não está cadastrado.";
                  }catch (FirebaseAuthInvalidCredentialsException e){
                      excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                  }catch (Exception e){
                      excecao ="Erro ao cadastrar usuário: " + e.getMessage();
                      e.printStackTrace();
                  }

                  exibirMensagem(excecao);
              }
            }
        });
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this,texto,Toast.LENGTH_SHORT).show();
    }

    public void abrirCadastro(View view){
        Intent i = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(i);
    }
}
