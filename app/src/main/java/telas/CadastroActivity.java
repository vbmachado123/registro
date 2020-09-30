package telas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import helper.ConfiguracaoDAO;
import model.Configuracao;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.Base64Custom;
import util.ConfiguracaoFirebase;
import util.Preferencias;
import model.Usuario;
import victor.machado.com.br.registro.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botao;
    private TextView textoLogin;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.cadastroNomeId);
        email = (EditText) findViewById(R.id.cadastroEmailId);
        senha = (EditText) findViewById(R.id.cadastroSenhaId);
        botao = (Button) findViewById(R.id.cadastrarBotaoId);
        textoLogin = (TextView) findViewById(R.id.loginId);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                cadastrarUsuario();

            }
        });

        textoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLoginUsuario();
            }
        });
    }

    private void cadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    //usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarDados(identificadorUsuario, usuario.getNome());

                    Configuracao configuracao = new Configuracao();
                    ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO(CadastroActivity.this);

                    String caminhoPasta = Environment.getExternalStorageDirectory() + "/Registro";
                    configuracao.setEmail(email.getText().toString());
                    configuracao.setNome(nome.getText().toString());
                    configuracao.setCaminhoPasta(caminhoPasta);
                    configuracaoDAO.inserir(configuracao);

                    abrirLoginUsuario();

                } else {

                    /*Tratando os erros ao logar o usuário*/
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Este e-mail já está sendo usado!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
