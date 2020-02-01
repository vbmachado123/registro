package victor.machado.com.br.registro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import model.Formulario;
import model.FormularioDAO;

public class ConfirmaActivity extends AppCompatActivity {

    private TextView exibeEndereco;
    private TextView exibeNome;
    private TextView exibeRg;
    private TextView exibeCpf;
    private TextView exibeCelular;
    private TextView exibeResponsabilidade;

    private Button naoBotao;
    private Button simBotao;

    private Bundle bundle = new Bundle();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);

        exibeEndereco = (TextView) findViewById(R.id.confirmaEnd);
        exibeNome = (TextView) findViewById(R.id.confirmaNome);
        exibeRg = (TextView) findViewById(R.id.confirmaRg);
        exibeCpf = (TextView) findViewById(R.id.confirmaCpf);
        exibeCelular = (TextView) findViewById(R.id.confirmaCelular);
        exibeResponsabilidade = (TextView) findViewById(R.id.confirmaResponsabilidade);

        //Recuperando as informações do cliente
         Intent intent = getIntent();
         Bundle extras = intent.getExtras();

        //Recuperando informações
          final String recuperaEnd =  extras.getString("endereco");
          final String recuperaNome =  extras.getString("nome");
          final String recuperaRg =  extras.getString("rg");
          final String recuperaCpf =  extras.getString("cpf");
          final String recuperaCelular =  extras.getString("celular");
          final String recuperaResponsabilidade =  extras.getString("responsabilidade");

        naoBotao = (Button) findViewById(R.id.botaoNao);
        simBotao = (Button) findViewById(R.id.botaoSim);

        //Exibindo informações para confirmação
        exibeEndereco.setText("Endereço: " + recuperaEnd);
        exibeNome.setText("Nome: " + recuperaNome);
        exibeRg.setText("Rg: " + recuperaRg);
        exibeCpf.setText("Cpf: " + recuperaCpf);
        exibeCelular.setText("Celular: " + recuperaCelular);
        exibeResponsabilidade.setText("O cliente é: " + recuperaResponsabilidade);

            naoBotao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ConfirmaActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            });

            simBotao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  FormularioDAO  dao = new FormularioDAO(ConfirmaActivity.this);

                    Formulario f = new Formulario();
                    f.setEndereco(recuperaEnd.toString());
                    f.setNome(recuperaNome.toString());
                    f.setRg(recuperaRg.toString());
                    f.setCpf(recuperaCpf.toString());
                    f.setCelular(recuperaCelular.toString());
                    f.setResponsabilidade(recuperaResponsabilidade.toString());
                    long id = dao.inserir(f);

                    Intent i = new Intent(ConfirmaActivity.this, ExibePDFActivity.class);
                   // startActivity(i);

                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirma, menu);

        return true;

    }
}
