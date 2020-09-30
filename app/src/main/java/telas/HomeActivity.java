package telas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import helper.CampoTermoDAO;
import helper.FormularioDAO;
import helper.PdfDAO;
import helper.TermoConfigDAO;
import helper.TermoDAO;
import model.CampoTermo;
import model.Formulario;
import model.PdfResponsabilidade;
import model.Termo;
import model.TermoConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.Preferencias;
import victor.machado.com.br.registro.R;

public class HomeActivity extends AppCompatActivity {

    private Button btIniciarAtendimento, btExportarBanco, btConfiguracao;
    private FormularioDAO dao;
    private String caminhoAssinatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            TextView versao = (TextView) findViewById(R.id.txt_versaapp);
            versao.setText("Versão: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dao = new FormularioDAO(HomeActivity.this);
        validaCampo();
    }

    private void validaCampo() {
        btIniciarAtendimento = (Button) findViewById(R.id.btIniciarAtendimento);
        btExportarBanco = (Button) findViewById(R.id.btExportar);
        btConfiguracao = (Button) findViewById(R.id.btConfiguracao);

        File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
        caminhoAssinatura = mydir + "/Imagens/" + "AssinaturaFiscal" + ".jpg";
        File fileAssinatura = new File(caminhoAssinatura);
        if( !fileAssinatura.exists() ){
            acessaActivity(AssinaturaFiscalActivity.class);
            Toast.makeText(this, "Assine para prosseguir", Toast.LENGTH_SHORT).show();
        }

        btIniciarAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        btExportarBanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formulario f = new Formulario();
                final FormularioDAO dao = new FormularioDAO(HomeActivity.this);
                f = dao.recupera();
                if (f != null) { /* Banco possui registros para exportar */
                    AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this, R.style.DialogStyle)
                            .setTitle("Atenção")
                            .setMessage("O banco será limpo após exportação! Deseja continuar?")
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean exportar = dao.exportarTabela();
                                    if (exportar) limparBanco();
                                    else
                                        Toast.makeText(HomeActivity.this, "Não foi possível exportar, tente novamente!", Toast.LENGTH_SHORT).show();
                                }
                            }).create();
                    dialog.show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this, R.style.DialogStyle)
                            .setTitle("Atenção")
                            .setMessage("O banco não possui registros! Deseja cria-los?")
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    acessaActivity(MainActivity.class);
                                }
                            }).create();
                    dialog.show();
                }
            }
        });

        btConfiguracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acessaActivity(ConfiguracoesActivity.class);
            }
        });

        adicionarFormResponsabilidade();
    }

    private void adicionarFormResponsabilidade() {
        TermoConfig config = new TermoConfig();
        TermoConfigDAO termoConfigDAO = new TermoConfigDAO(this);
        Termo termo = new Termo();
        CampoTermo campoTermo = new CampoTermo();

        TermoDAO termoDAO = new TermoDAO(this);
        Termo t = termoDAO.recupera();

        if(t == null) { //Criar Termo no banco
            PdfResponsabilidade responsabilidade = new PdfResponsabilidade();
            termo.setTitulo(responsabilidade.getTituloPagina());
            termo.setCategoria(0);

            CampoTermoDAO campoTermoDAO = new CampoTermoDAO(this);

            int idTermo = (int) termoDAO.inserir(termo);

            Log.i("Termo", "Titulo: " + termo.getTitulo());

            String[] opcoes = {"Proprietário", "Morador Responsável"};
            String[] documentos = {"RG", "CPF"};
            config.setOpcoes(opcoes);
            config.setUnicaEscolha(1);
            config.setExibeAssinaturaFiscal(1);
            config.setQuantidadeFotos(1);
            config.setIdTermo(idTermo);
            config.setDocumentosNecessarios(documentos);

            termoConfigDAO.inserir(config);

            Log.i("Termo", "Configurações do Termo: " + config.getOpcoesConvertidas() + " - " + config.getUnicaEscolha() + " - " + config.getQuantidadeFotos());

            for(int i = 0; i < responsabilidade.getAllCampos().length; i++){
                campoTermo.setIdTermo(idTermo);
                campoTermo.setDescricao(responsabilidade.getAllCampos()[i]);
                campoTermo.setPosicao(i);
                campoTermoDAO.inserir(campoTermo);
                Log.i("Termo", "Inserindo: " + campoTermo.getDescricao() + " - Na posição: " + campoTermo.getPosicao());
            }

            Preferencias preferencias = new Preferencias(this);
            preferencias.salvarTermoSelecionado(idTermo);

        termo.setTitulo(responsabilidade.getTituloPagina() + " I");
        termo.setCategoria(0);

        idTermo = (int) termoDAO.inserir(termo);

        Log.i("Termo", "Titulo: " + termo.getTitulo());

        String[] opcoesI = {"Proprietário", "Morador Responsável"};
        String[] documentosI = {"CNPJ", "IE", "IM"};
        config.setOpcoes(opcoesI);
        config.setUnicaEscolha(2);
        config.setExibeAssinaturaFiscal(2);
        config.setQuantidadeFotos(3);
        config.setIdTermo(idTermo);
        config.setDocumentosNecessarios(documentosI);

        termoConfigDAO.inserir(config);

        Log.i("Termo", "Configurações do Termo: " + config.getOpcoesConvertidas() + " - " + config.getUnicaEscolha() + " - " + config.getQuantidadeFotos());

        for(int i = 0; i < responsabilidade.getAllCampos().length; i++){
            campoTermo.setIdTermo(idTermo);
            campoTermo.setDescricao(responsabilidade.getAllCampos()[i]);
            campoTermo.setPosicao(i);
            campoTermoDAO.inserir(campoTermo);
            Log.i("Termo", "Inserindo: " + campoTermo.getDescricao() + " - Na posição: " + campoTermo.getPosicao());
        }

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void limparBanco() {
        dao.limparBanco();
        PdfDAO pDao = new PdfDAO(this);
        pDao.limparBanco();
        Toast.makeText(HomeActivity.this, "Exportação concluída! O banco foi limpo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void acessaActivity(Class c){
        Intent it = new Intent(HomeActivity.this, c);
        startActivity(it);
        finish();
    }
}