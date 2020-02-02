package victor.machado.com.br.registro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import model.Formulario;
import helper.FormularioDAO;

public class MainActivity extends AppCompatActivity {

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

    private String opcaoEscolhida="";

    private Button botaoEnviar;

    private Toolbar toolbar;

    public static final int CONSTANTE_CADASTRO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);

        //Campos de texto
        endereco = (EditText) findViewById(R.id.endClienteId);
        nome = (EditText) findViewById(R.id.nomeClienteId);
        rg = (EditText) findViewById(R.id.rgClienteId);
        cpf = (EditText) findViewById(R.id.cpfClienteId);
        celular = (EditText) findViewById(R.id.celularClienteId);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioProprietario = (RadioButton) findViewById(R.id.proprietarioId);
        radioMoradorResponsavel = (RadioButton) findViewById(R.id.moradorResponsavelId);

        botaoEnviar = (Button) findViewById(R.id.gerarDocId);

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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
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
            }
        });
    }

    public void geraDoc(){

        Bundle bundle = new Bundle();
        bundle.putString("endereco", endereco.getText().toString());
        bundle.putString("nome", nome.getText().toString());
        bundle.putString("rg", rg.getText().toString());
        bundle.putString("cpf", cpf.getText().toString());
        bundle.putString("celular", celular.getText().toString());
        bundle.putString("responsabilidade", opcaoEscolhida.toString());

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
                Toast.makeText(this, "Botão Sair Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemConfiguracoes:
                Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_lista:
                Intent i = new Intent(MainActivity.this, ListarFormsActivity.class);
                startActivity(i);
                return true;
            case  R.id.item_sincroniza:
                Toast.makeText(this, "Botão Sincronizar Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            case  R.id.item_limpa:
                 limpar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void limpar(){

        endereco.setText("");
        nome.setText("");
        rg.setText("");
        cpf.setText("");
        celular.setText("");

    }
}
