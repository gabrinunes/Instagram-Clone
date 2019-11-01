package gabrielcunha.cursoandroid.instagram.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

   public static FirebaseUser getUsuarioAtual(){
       FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
       return usuario.getCurrentUser();
   }

    public static void atualizarNomeUsuario(String nome){

        try {

            //Usuario logado no app
            FirebaseUser usuarioLogado = getUsuarioAtual();

            //Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName( nome)
                    .build();
            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if(!task.isSuccessful()){
                      Log.d("Perfil","Erro ao atualizar nome");
                  }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
