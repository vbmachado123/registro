package victor.machado.com.br.registro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmaActivity extends AppCompatActivity {

    private TextView recuperaEnd;
    private TextView recuperaNome;
    private TextView recuperaRg;
    private TextView recuperaCpf;
    private TextView recuperaCelular;
    private TextView recuperaResponsabilidade;

    private Button naoBotao;
    private Button simBotao;

    //Recuperando as informações do cliente
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

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

        //Concatenando informações para confirmação
        String exibeEndereco = "Endereço: " + extras.getString("endereco");
        String exibeNome = "Nome: " + extras.getString("nome");
        String exibeRg = "Rg: " + extras.getString("rg");
        String exibeCpf = "Cpf: " + extras.getString("cpf");
        String exibeCelular = "Celular: " + extras.getString("celular");
        String exibeResponsabilidade = "O morador é: " + extras.getString("responsabilidade");

        //Exibindo informações para confirmação
        recuperaEnd.setText(exibeEndereco);
        recuperaNome.setText(exibeNome);
        recuperaRg.setText(exibeRg);
        recuperaCpf.setText(exibeCpf);
        recuperaCelular.setText(exibeCelular);
        recuperaResponsabilidade.setText(exibeResponsabilidade);

        naoBotao = (Button) findViewById(R.id.botaoNao);
        simBotao = (Button) findViewById(R.id.botaoSim);

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
                if(ActivityCompat.checkSelfPermission(ConfirmaActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ConfirmaActivity.this,
                        new String[] {Manifest.permission.CAMERA}, 0);
                }
                salvaNoBanco();
            }
        });

    }

    public void salvaNoBanco(){

        Toast.makeText(this, "Salvando no banco...!", Toast.LENGTH_SHORT).show();

 /*       Cliente c = new Cliente();
        c.setEndereco(extras.getString("endereco"));
        c.setNome(extras.getString("nome"));
        c.setRg(extras.getString("rg"));
        c.setCpf(extras.getString("cpf"));
        c.setCelular(extras.getString("celular"));
        c.setOpcaoEscolhida(extras.getString("responsabilidade"));*/

        Intent intent = new Intent(ConfirmaActivity.this, ImagemActivity.class);
        intent.putExtras(extras);
        startActivity(intent);

    }


}
