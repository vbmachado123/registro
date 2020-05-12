package telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.InputStream;

import helper.ConfiguracaoDAO;
import model.Configuracao;
import util.ConfiguracaoFirebase;
import victor.machado.com.br.registro.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    private GifImageView gifRegistro;
    private Toolbar toolbar;
    private FirebaseAuth usuarioAutenticacao;
    private EditText email, nome;
    private Switch exibeAssinatura;
    private TextView caminhoPasta;
    private FloatingActionButton fabSalvar;
    private Configuracao configuracao;
    private ConfiguracaoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

         configuracao = new Configuracao();
         dao = new ConfiguracaoDAO(ConfiguracoesActivity.this);

        //Valida sessão
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        gifRegistro = (GifImageView) findViewById(R.id.gifRegistro);
        email = (EditText) findViewById(R.id.etEmail);
        nome = (EditText) findViewById(R.id.etNome);
        exibeAssinatura = (Switch) findViewById(R.id.sExibeAssinatura);
        caminhoPasta = (TextView) findViewById(R.id.caminhoPasta);
        fabSalvar = (FloatingActionButton) findViewById(R.id.botaoSave);
        recuperaInformacoes();

        //Setando a resolução do gif
        try{
            InputStream inputStream = getAssets().open("registro.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifRegistro.setBytes(bytes);
            gifRegistro.startAnimation();
        } catch (IOException e){/*...*/}
    }

    private void recuperaInformacoes() {

        configuracao = dao.recupera();
        if(configuracao != null) {
            email.setText(configuracao.getEmail());
            nome.setText(configuracao.getNome());
                if(configuracao.getExibeAssinatura() == 1)
                  exibeAssinatura.setActivated(true);
                else if (configuracao.getExibeAssinatura() == 0)
                    exibeAssinatura.setActivated(false);

            caminhoPasta.setText(configuracao.getCaminhoPasta());
        }

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

                                Configuracao config = dao.recupera();
                                int valorExibeAssinatura = 0;

                                if(exibeAssinatura.isChecked())  valorExibeAssinatura = 1;
                                String caminhoPasta = Environment.getExternalStorageDirectory() + "/Registro";

                                configuracao = new Configuracao();
                                configuracao.setEmail(email.getText().toString());
                                configuracao.setNome(nome.getText().toString());
                                configuracao.setExibeAssinatura(valorExibeAssinatura);
                                configuracao.setCaminhoPasta(caminhoPasta);

                                if(config != null)dao.atualizar(configuracao);
                                else dao.inserir(configuracao);

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

        switch ( item.getItemId() ) {
            case R.id.itemSair:
                deslogarUsuario();
                return true;
            case R.id.itemConfiguracoes:
                acessaActivity(ConfiguracoesActivity.class);
                return true;
            case R.id.item_lista:
                acessaActivity(ListarFormsActivity.class);
                return true;
            case  R.id.item_sincroniza:
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

}
