package telas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import helper.FormularioDAO;
import helper.PdfDAO;
import model.Formulario;
import victor.machado.com.br.registro.R;

public class HomeActivity extends AppCompatActivity {

    private Button btIniciarAtendimento, btExportarBanco;
private FormularioDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dao = new FormularioDAO(HomeActivity.this);
        validaCampo();
    }

    private void validaCampo() {
        btIniciarAtendimento = (Button) findViewById(R.id.btIniciarAtendimento);
        btExportarBanco = (Button) findViewById(R.id.btExportar);

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
                    AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this , R.style.DialogStyle)
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
                    AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this , R.style.DialogStyle)
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
                                    Intent it = new Intent(HomeActivity.this, MainActivity.class);
                                    startActivity(it);
                                }
                            }).create();
                    dialog.show();
                }
            }
        });


    }

    private void limparBanco() {
        dao.limparBanco();
        PdfDAO pDao = new PdfDAO(this);
        pDao.limparBanco();
        Toast.makeText(HomeActivity.this, "Exportação concluída! O banco foi limpo com sucesso!", Toast.LENGTH_SHORT).show();
    }
}