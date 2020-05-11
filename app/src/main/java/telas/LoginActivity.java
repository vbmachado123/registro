package telas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import helper.Base64Custom;
import helper.ConfiguracaoFirebase;
import helper.Preferencias;
import model.Usuario;
import victor.machado.com.br.registro.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private TextView textoRegistrar;

    private Usuario usuario;
    private String identificadorUsuarioLogado;

    private DatabaseReference referenceFirebase;
    private FirebaseAuth autenticacao;
    private ValueEventListener valueEventListenerUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificaUsuarioLogado();

        email = (EditText) findViewById(R.id.emailLoginId);
        senha = (EditText) findViewById(R.id.senhaLoginId);
        botaoLogar = (Button) findViewById(R.id.botaoLogarId);
        textoRegistrar = (TextView) findViewById(R.id.registraId);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }
        });

        textoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {

                if(task.isSuccessful()){

                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    referenceFirebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuarioLogado);

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    referenceFirebase.addListenerForSingleValueEvent(valueEventListenerUsuario);

                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();


                }else {
                     /*Tratando os erros ao logar o usuário*/
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail inválido!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha incorreta!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o login!";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void verificaUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }

    }

    private void abrirTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

}
