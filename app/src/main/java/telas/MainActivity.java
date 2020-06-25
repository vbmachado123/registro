package telas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import util.ConfiguracaoFirebase;
import model.Formulario;
import helper.FormularioDAO;
import util.Permissao;
import victor.machado.com.br.registro.R;

public class MainActivity extends AppCompatActivity {

    private Context context = this;

    //Informações gerais
    private EditText endereco;
    private EditText nome;
    private EditText rg;
    private EditText cpf;
    private EditText celular;
    private RadioGroup radioGroup;
    private RadioButton radioProprietario;
    private RadioButton radioMoradorResponsavel;
    private Formulario formulario = null;
    private FirebaseAuth usuarioAutenticacao;

    private String opcaoEscolhida="";

    private Button botaoEnviar;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permissao permissao = new Permissao();
        permissao.Permissoes(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);

        File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
        String caminhoLogo = mydir + "/Imagens/" + "logo" + ".png";

        final File file = new File(caminhoLogo);

        //Campos de texto
        endereco = (EditText) findViewById(R.id.endClienteId);
        nome = (EditText) findViewById(R.id.nomeClienteId);
        rg = (EditText) findViewById(R.id.rgClienteId);
        cpf = (EditText) findViewById(R.id.cpfClienteId);
        celular = (EditText) findViewById(R.id.celularClienteId);

        //Mascaras de campo
        SimpleMaskFormatter smfRG = new SimpleMaskFormatter("NN.NNN.NNN");
        MaskTextWatcher mtwRg = new MaskTextWatcher(rg, smfRG);
        rg.addTextChangedListener(mtwRg);

        SimpleMaskFormatter smfCPF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtwCPF = new MaskTextWatcher(cpf, smfCPF);
        cpf.addTextChangedListener(mtwCPF);

        SimpleMaskFormatter smfCell = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtwCell = new MaskTextWatcher(celular, smfCell);
        celular.addTextChangedListener(mtwCell);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //Valida sessão
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoEnviar = (Button) findViewById(R.id.gerarDocId);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.proprietarioId:
                        opcaoEscolhida= "Proprietário";
                        break;
                    case R.id.moradorResponsavelId:
                        opcaoEscolhida="Morador Responsável";
                        break;
                }
            }
        });

        //Recuperando o formulário para atualizar
        Intent it = getIntent();
        if(it.hasExtra("formulario")){

            //Recuperando informação
            formulario = (Formulario) it.getSerializableExtra("formulario");

            //Setando para o usuário
            endereco.setText(formulario.getEndereco());
            nome.setText(formulario.getNome());
            rg.setText(formulario.getRg());
            cpf.setText(formulario.getCpf());
            celular.setText(formulario.getCelular());
        }

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean prosseguir = validaCampoVazio();
               if(prosseguir){
                   if(!(file.exists())) criarPasta();
                   if(formulario != null){
                       AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                               .setTitle("Atenção")
                               .setMessage("Deseja gerar um novo PDF?")
                               .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       atualizaBanco();
                                   }
                               })
                               .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       geraDoc();
                                   }
                               }).create();
                       dialog.show();
                   } else {
                       geraDoc();
                   }
               } else {
                   Toast.makeText(context, "Preencha todas as informações!", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private boolean validaCampoVazio() {
        boolean valida = true;
        if(endereco.getText().toString().isEmpty())
            valida = false;
        if(nome.getText().toString().isEmpty())
            valida = false;
        if(rg.getText().toString().isEmpty())
            valida = false;
        if(cpf.getText().toString().isEmpty())
            valida = false;

        return valida;
    }

    private void criarPasta() {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
        Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
        BitmapDrawable drawable = ((BitmapDrawable) mDrawable);
        Bitmap bitmap1 = drawable.getBitmap();
        OutputStream out = null;
        File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
        File imagem = new File(mydir, "Imagens");
        String caminhoLogo = mydir + "/Imagens/" + "logo" + ".png";
        File logo = new File(imagem, "logo.png");

        if( !(logo.exists()) ){
            mydir.mkdir();
            imagem.mkdirs();
            try {
                logo.createNewFile();
                out = new FileOutputStream(logo);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 50, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void geraDoc(){
        if(opcaoEscolhida.isEmpty())
            opcaoEscolhida="Morador Responsável";
        Bundle bundle = new Bundle();
        bundle.putString("endereco", endereco.getText().toString());
        bundle.putString("nome", nome.getText().toString());
        bundle.putString("rg", rg.getText().toString());
        bundle.putString("cpf", cpf.getText().toString());
        bundle.putString("celular", celular.getText().toString());
        bundle.putString("responsabilidade", opcaoEscolhida);

        Intent intent = new Intent(MainActivity.this, ConfirmaActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void atualizaBanco(){

        formulario.setEndereco(endereco.getText().toString());
        formulario.setNome(nome.getText().toString());
        formulario.setRg(rg.getText().toString());
        formulario.setCpf(cpf.getText().toString());
        formulario.setCelular(celular.getText().toString());
        formulario.setResponsabilidade(opcaoEscolhida.toString());

        FormularioDAO dao = new FormularioDAO(MainActivity.this);
        dao.atualizar(formulario);

        Toast.makeText(MainActivity.this, "Formulário atualizado com sucesso!", Toast.LENGTH_SHORT).show();

        limpar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
            case  R.id.item_limpa:
                 limpar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void acessaActivity(Class c){
        Intent i = new Intent(MainActivity.this, c);
        startActivity(i);
    }

    private void deslogarUsuario() {
        usuarioAutenticacao.signOut();
        acessaActivity(LoginActivity.class);
        finish();
    }

    public void limpar(){
        endereco.setText("");
        nome.setText("");
        rg.setText("");
        cpf.setText("");
        celular.setText("");
    }
}
