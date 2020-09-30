package telas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import helper.DocumentoDAO;
import helper.TermoConfigDAO;
import helper.TermoDAO;
import model.Documento;
import model.Termo;
import model.TermoConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.ConfiguracaoFirebase;
import model.Formulario;
import helper.FormularioDAO;
import util.Preferencias;
import victor.machado.com.br.registro.R;

public class ConfirmaActivity extends AppCompatActivity {

    private TextView exibeEndereco;
    private TextView exibeNome;
    private TextView exibeRg;
    private TextView exibeCpf;
    private TextView exibeCnpj;
    private TextView exibeCnh;
    private TextView exibeIE;
    private TextView exibeIM;
    private TextView exibeCelular;
    private TextView exibeResponsabilidade;

    private Button naoBotao;
    private Button simBotao;

    /* Docs */
    String recuperaRG = "";
    String recuperaCPF = "";
    String recuperaCNPJ = "";
    String recuperaIE = "";
    String recuperaIM = "";
    String recuperaCNH = "";

    private Formulario formulario;
    private Toolbar toolbar;

    private FirebaseAuth usuarioAutenticacao;

    private TermoConfig config;
    private TermoConfigDAO configDao;
    private Termo termo;
    private TermoDAO termoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Confirmar Informações");
        setSupportActionBar(toolbar);

        Preferencias preferencias = new Preferencias(this);
        termoDAO = new TermoDAO(this);
        termo = termoDAO.getById(preferencias.getTermo());

        config = new TermoConfig();
        configDao = new TermoConfigDAO(this);
        config = configDao.getByIdTermo(termo.getId());

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        exibeEndereco = (TextView) findViewById(R.id.confirmaEnd);
        exibeNome = (TextView) findViewById(R.id.confirmaNome);
        exibeRg = (TextView) findViewById(R.id.confirmaRg);
        exibeCpf = (TextView) findViewById(R.id.confirmaCpf);
        exibeCnpj = (TextView) findViewById(R.id.confirmaCnpj);
        exibeCnh = (TextView) findViewById(R.id.confirmaCnh);
        exibeIE = (TextView) findViewById(R.id.confirmaIE);
        exibeIM = (TextView) findViewById(R.id.confirmaIM);
        exibeCelular = (TextView) findViewById(R.id.confirmaCelular);
        exibeResponsabilidade = (TextView) findViewById(R.id.confirmaResponsabilidade);

        //Recuperando as informações do cliente
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Recuperando informações
        final String recuperaEnd = extras.getString("endereco");
        final String recuperaNome = extras.getString("nome");

        final String recuperaCelular = extras.getString("celular");

        final String recuperaResponsabilidade = extras.getString("responsabilidade");


        naoBotao = (Button) findViewById(R.id.botaoNao);
        simBotao = (Button) findViewById(R.id.botaoSim);

        //Exibindo informações para confirmação
        exibeEndereco.setText("Endereço: " + recuperaEnd);
        exibeNome.setText("Nome: " + recuperaNome);

        String[] documentosForm = config.getDocumentosNecessarios();
        for (int i = 0; i < documentosForm.length; i++) {
            switch (documentosForm[i]) {
                case "RG":
                    exibeRg.setVisibility(View.VISIBLE);
                    recuperaRG = extras.getString("rg");
                    exibeRg.setText("Rg: " + recuperaRG);
                    break;
                case "CPF":
                    exibeCpf.setVisibility(View.VISIBLE);
                    recuperaCPF = extras.getString("cpf");
                    exibeCpf.setText("Cpf: " + recuperaCPF);
                    break;
                case "CNPJ":
                    exibeCnpj.setVisibility(View.VISIBLE);
                    recuperaCNPJ = extras.getString("cnpj");
                    exibeCnpj.setText("Cnpj: " + recuperaCNPJ);
                    break;
                case "IE":
                    exibeIE.setVisibility(View.VISIBLE);
                    recuperaIE = extras.getString("ie");
                    exibeIE.setText("Inscrição Estadual: " + recuperaIE);
                    break;
                case "IM":
                    exibeIM.setVisibility(View.VISIBLE);
                    recuperaIM = extras.getString("im");
                    exibeIM.setText("CCM: " + recuperaIM);
                    break;
                case "CCM":
                    exibeIM.setVisibility(View.VISIBLE);
                    recuperaIM = extras.getString("im");
                    exibeIM.setText("CCM: " + recuperaIM);
                    break;
                case "CNH":
                    exibeCnh.setVisibility(View.VISIBLE);
                    recuperaCNH = extras.getString("cnh");
                    exibeCnh.setText("Cnh: " + recuperaCNH);
                    break;
            }
        }

        exibeCelular.setText("Celular: " + recuperaCelular);
        exibeResponsabilidade.setText("O cliente é: " + recuperaResponsabilidade);

        naoBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessaActivity(MainActivity.class);
            }
        });

        simBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FormularioDAO dao = new FormularioDAO(ConfirmaActivity.this);

                Formulario f = new Formulario();
                f.setEndereco(recuperaEnd);
                f.setNome(recuperaNome);
                f.setCelular(recuperaCelular);
                f.setResponsabilidade(recuperaResponsabilidade);
                long id = dao.inserir(f);

                /* Salvar os documentos */
                String[] documentosForm = config.getDocumentosNecessarios();
                for (int i = 0; i < documentosForm.length; i++) {
                    insereDocumento(documentosForm[i], (int) id);
                }

                acessaActivity(ImagemActivity.class);
            }
        });
    }

    private void insereDocumento(String s, int id) {
        DocumentoDAO documentoDAO = new DocumentoDAO(ConfirmaActivity.this);
        Documento documento = new Documento();
        documento.setIdFormulario((int) id);

        switch (s) {
            case "RG":
                documento.setTipo(s);
                documento.setDocumento(recuperaRG);
                break;
            case "CPF":
                documento.setTipo(s);
                documento.setDocumento(recuperaCPF);
                break;
            case "CNPJ":
                documento.setTipo(s);
                documento.setDocumento(recuperaCNPJ);
                break;
            case "IE":
                documento.setTipo(s);
                documento.setDocumento(recuperaIE);
                break;
            case "IM":
                documento.setTipo(s);
                documento.setDocumento(recuperaIM);
                break;
            case "CCM":
                documento.setTipo(s);
                documento.setDocumento(recuperaIM);
                break;
            case "CNH":
                documento.setTipo(s);
                documento.setDocumento(recuperaCNH);
                break;
        }

        documentoDAO.inserir(documento);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirma, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSair:
                deslogarUsuario();
                return true;
            case R.id.itemConfiguracoes:
                confirmaSaida(ConfiguracoesActivity.class);
                return true;
            case R.id.item_lista:
                confirmaSaida(ListarFormsActivity.class);
                return true;
            case R.id.item_sincroniza:
                Toast.makeText(this, "Botão Sincronizar Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario() {

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(ConfirmaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void confirmaSaida(final Class c) {

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setTitle("Atenção")
                .setMessage("Confirma saída?" +
                        " As informações serão perdidas!")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acessaActivity(c);
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void acessaActivity(Class c) {
        Intent it = new Intent(ConfirmaActivity.this, c);
        startActivity(it);
        finish();
    }
}
