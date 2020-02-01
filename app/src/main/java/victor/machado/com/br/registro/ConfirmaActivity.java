package victor.machado.com.br.registro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import model.Cliente;

public class ConfirmaActivity extends AppCompatActivity {

    private TextView recuperaEnd;
    private TextView recuperaNome;
    private TextView recuperaRg;
    private TextView recuperaCpf;
    private TextView recuperaCelular;
    private TextView recuperaResponsabilidade;

    Cliente cliente = new Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma);
        recuperaEnd = (TextView) findViewById(R.id.confirmaEnd);
        recuperaNome = (TextView) findViewById(R.id.confirmaNome);
        recuperaRg = (TextView) findViewById(R.id.confirmaRg);
        recuperaCpf = (TextView) findViewById(R.id.confirmaCpf);
        recuperaCelular = (TextView) findViewById(R.id.confirmaCelular);
        recuperaResponsabilidade = (TextView) findViewById(R.id.confirmaResponsabilidade);

        //Recuperando as informações do cliente
        Bundle extras = getIntent().getExtras();
        Toast.makeText(this, extras.toString(), Toast.LENGTH_SHORT).show();

        String exibeEndereco = "Endereço: " + cliente.getEndereco();
        String exibeNome = "Nome: " + cliente.getNome();
        String exibeRg = "Rg: " + cliente.getRg();
        String exibeCpf = "Cpf: " + cliente.getCpf();
        String exibeCelular = "Celular: " + cliente.getCelular();
        String exibeResponsabilidade = "O morador é: " + cliente.getOpcaoEscolhida();

        recuperaEnd.setText(exibeEndereco);
        recuperaNome.setText(exibeNome);
        recuperaRg.setText(exibeRg);
        recuperaCpf.setText(exibeCpf);
        recuperaCelular.setText(exibeCelular);
        recuperaResponsabilidade.setText(exibeResponsabilidade);
    }

    public void salvaNoBanco(){

    }
}
