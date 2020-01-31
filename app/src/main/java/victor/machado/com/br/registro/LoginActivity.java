package victor.machado.com.br.registro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private TextView textoRegistrar;

    private String loginTemporario = "teste";
    private String emailInserido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailLoginId);
        senha = (EditText) findViewById(R.id.senhaLoginId);
        botaoLogar = (Button) findViewById(R.id.botaoLogarId);
        textoRegistrar = (TextView) findViewById(R.id.registraId);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailInserido = email.getText().toString();

                if (emailInserido == loginTemporario) {

                    Toast.makeText(LoginActivity.this, "Email correto!", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(LoginActivity.this, loginTemporario, Toast.LENGTH_LONG).show();
                }

            }
        });

        textoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });


    }
}
