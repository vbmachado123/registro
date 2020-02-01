package victor.machado.com.br.registro;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import model.Cliente;

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

    private String opcaoEscolhida="";

    private Button botaoEnviar;

    private Toolbar toolbar;

    public static final int CONSTANTE_CADASTRO = 1;

    Cliente cliente = new Cliente();

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

        //Setando valores para o CLIENTE - NÃO FUNCIONANDO
                cliente.setNome(nome.getText().toString());
                cliente.setEndereco(endereco.getText().toString());
                cliente.setRg(rg.getText().toString());
                cliente.setCpf(cpf.getText().toString());
                cliente.setCelular(celular.getText().toString());
                cliente.setOpcaoEscolhida(opcaoEscolhida);

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ConfirmaActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("endereco", endereco.getText().toString());
                bundle.putString("nome", nome.getText().toString());
                bundle.putString("rg", rg.getText().toString());
                bundle.putString("cpf", cpf.getText().toString());
                bundle.putString("celular", celular.getText().toString());
                bundle.putString("responsabilidade", opcaoEscolhida.toString());

                intent.putExtras(bundle);

                startActivityForResult(intent, CONSTANTE_CADASTRO);

            }
        });

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
                Toast.makeText(this, "Botão Sair Selecionado", Toast.LENGTH_LONG).show();
                return true;
            case R.id.itemConfiguracoes:
                Toast.makeText(this, "Botão Configurações Selecionado", Toast.LENGTH_LONG).show();
                return true;
            case R.id.item_pesquisa:
                Toast.makeText(this, "Botão Pesquisar Selecionado", Toast.LENGTH_LONG).show();
                return true;
            case  R.id.item_sincroniza:
                Toast.makeText(this, "Botão Sincronizar Selecionado", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
