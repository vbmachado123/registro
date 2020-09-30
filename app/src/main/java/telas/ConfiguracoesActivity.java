package telas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import helper.ConfiguracaoDAO;
import helper.UsuarioDAO;
import model.Configuracao;
import model.Usuario;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.ConfiguracaoFirebase;
import victor.machado.com.br.registro.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    private GifImageView gifRegistro;
    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    private EditText email, nome;
    private ImageView assinaturaFiscal;
    private TextView caminhoPasta;
    private FloatingActionButton fabSalvar;
    private Usuario usuario;
    private UsuarioDAO dao;
    private String caminhoAssinatura;
    private String textoCaminhoPasta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        usuario = new Usuario();
        dao = new UsuarioDAO(ConfiguracoesActivity.this);

        textoCaminhoPasta = Environment.getExternalStorageDirectory() + "/Registro";

        //Valida sessão
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        gifRegistro = (GifImageView) findViewById(R.id.gifRegistro);
        email = (EditText) findViewById(R.id.etEmail);
        nome = (EditText) findViewById(R.id.etNome);
        assinaturaFiscal = (ImageView) findViewById(R.id.assinaturaFiscal);
        caminhoPasta = (TextView) findViewById(R.id.caminhoPasta);
        fabSalvar = (FloatingActionButton) findViewById(R.id.botaoSave);
        recuperaInformacoes();

        //Setando a resolução do gif
        try {
            InputStream inputStream = getAssets().open("registro.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifRegistro.setBytes(bytes);
            gifRegistro.startAnimation();
        } catch (IOException e) {/*...*/}

        caminhoAssinatura = textoCaminhoPasta + "/Imagens/" + "AssinaturaFiscal" + ".jpg";

        File fileAssinatura = new File(caminhoAssinatura);
        if (fileAssinatura.exists()) {
            Bitmap assinatura = BitmapFactory.decodeFile(String.valueOf(fileAssinatura));
            assinaturaFiscal.setImageBitmap(assinatura);
        }

        assinaturaFiscal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(ConfiguracoesActivity.this, R.style.DialogStyle)
                        .setTitle("Atenção")
                        .setMessage("Deseja alterar a assinatura?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                acessaActivity(AssinaturaFiscalActivity.class);

                            }
                        }).create();
                dialog.show();
            }
        });
    }

    private void recuperaInformacoes() {

        usuario = dao.recupera();
        if (usuario != null) {
            email.setText(usuario.getEmail());
            nome.setText(usuario.getNome());
        }

        caminhoPasta.setText(textoCaminhoPasta);

        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Salvar e atualizar
                AlertDialog dialog = new AlertDialog.Builder(ConfiguracoesActivity.this, R.style.DialogStyle)
                        .setTitle("Atenção")
                        .setMessage("Realmente deseja atualizar as informações?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Usuario user = dao.recupera();
                                int valorExibeAssinatura = 0;

                                String caminhoPasta = Environment.getExternalStorageDirectory() + "/Registro";

                                usuario = new Usuario();
                                usuario.setEmail(email.getText().toString());
                                usuario.setNome(nome.getText().toString());
                                //  configuracao.setCaminhoPasta(caminhoPasta);

                                if (user != null) dao.atualizar(usuario);
                                else dao.inserir(usuario);

                                acessaActivity(MainActivity.class);

                            }
                        }).create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configuracoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSair:
                deslogarUsuario();
                return true;
            case R.id.itemConfiguracoes:
                acessaActivity(ConfiguracoesActivity.class);
                return true;
            case R.id.item_lista:
                acessaActivity(ListarFormsActivity.class);
                return true;
            case R.id.item_sincroniza:
                acessaActivity(MainActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void acessaActivity(Class c) {
        Intent it = new Intent(ConfiguracoesActivity.this, c);
        startActivity(it);
    }

    private void deslogarUsuario() {
        usuarioAutenticacao.signOut();
        acessaActivity(LoginActivity.class);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
