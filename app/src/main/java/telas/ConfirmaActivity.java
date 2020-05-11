package telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import util.ConfiguracaoFirebase;
import model.Formulario;
import helper.FormularioDAO;
import victor.machado.com.br.registro.R;

public class ConfirmaActivity extends AppCompatActivity {

    private TextView exibeEndereco;
    private TextView exibeNome;
    private TextView exibeRg;
    private TextView exibeCpf;
    private TextView exibeCelular;
    private TextView exibeResponsabilidade;

    private Button naoBotao;
    private Button simBotao;

    private Formulario formulario;
    private Toolbar toolbar;

    private FirebaseAuth usuarioAutenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Confirmar Informações");
        setSupportActionBar(toolbar);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

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
          final String recuperaEnd = extras.getString("endereco");
          final String recuperaNome = extras.getString("nome");
          final String recuperaRg = extras.getString("rg");
          final String recuperaCpf = extras.getString("cpf");
          final String recuperaCelular = extras.getString("celular");
          final String recuperaResponsabilidade = extras.getString("responsabilidade");

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

                   //Toast.makeText(ConfirmaActivity.this, "id: " + id, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ConfirmaActivity.this, ImagemActivity.class);
                    startActivity(i);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_confirma, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.itemSair:
                deslogarUsuario();
                return true;
            case R.id.itemConfiguracoes:
                confirmaSaida(ConfiguracoesActivity.class);
                return true;
            case R.id.item_lista:
                confirmaSaida(ListarFormsActivity.class);
                return true;
            case  R.id.item_sincroniza:
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

    public void confirmaSaida(final Class c){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Confirma saída?" +
                        " As informações serão perdidas!")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ConfirmaActivity.this, c);
                        startActivity(intent);
                    }
                }).create();
        dialog.show();
    }
}