package victor.machado.com.br.registro;

import android.Manifest;
import android.app.Activity;
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

public class ConfirmaActivity extends Activity {

    private TextView exibeEndereco;
    private TextView exibeNome;
    private TextView exibeRg;
    private TextView exibeCpf;
    private TextView exibeCelular;
    private TextView exibeResponsabilidade;

    private Button naoBotao;
    private Button simBotao;

    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirma);
        exibeEndereco = (TextView) findViewById(R.id.confirmaEnd);
        exibeNome = (TextView) findViewById(R.id.confirmaNome);
        exibeRg = (TextView) findViewById(R.id.confirmaRg);
        exibeCpf = (TextView) findViewById(R.id.confirmaCpf);
        exibeCelular = (TextView) findViewById(R.id.confirmaCelular);
        exibeResponsabilidade = (TextView) findViewById(R.id.confirmaResponsabilidade);


        //Recuperando as informações do cliente
          final Intent intent = getIntent();
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
        exibeResponsabilidade.setText("O morador é: " + recuperaResponsabilidade);

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

                bundle.putString("endereco", recuperaEnd.toString());
                bundle.putString("nome", recuperaNome.toString());
                bundle.putString("rg", recuperaRg.toString());
                bundle.putString("cpf", recuperaCpf.toString());
                bundle.putString("celular", recuperaCelular.toString());
                bundle.putString("responsabilidade", recuperaResponsabilidade.toString());

                Intent i = new Intent(ConfirmaActivity.this, ImagemActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                salvaNoBanco();
            }
        });

    }

    public void salvaNoBanco(){

        Toast.makeText(this, "Salvando no banco...!", Toast.LENGTH_SHORT).show();

    }


}
