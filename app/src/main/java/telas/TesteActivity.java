package telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.Document;

import java.io.File;
import java.io.FileNotFoundException;

import helper.FormularioDAO;
import model.Formulario;
import util.GeraPDF;
import victor.machado.com.br.registro.R;

public class TesteActivity extends AppCompatActivity {

    private Button gera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);

        gera = (Button) findViewById(R.id.gerarDocId);
        gera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
                Document documento;
                String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                String nomePasta = "/Registro";
                Formulario i = new FormularioDAO(TesteActivity.this).recupera();
                String nomeArquivo = i.getEndereco() + ".pdf";
                File arquivo = new File(mydir, nomeArquivo);
                GeraPDF pdf = new GeraPDF();

                try {
                    documento = pdf.GeraPDF(String.valueOf(arquivo), TesteActivity.this);
                } catch (FileNotFoundException e){

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(arquivo.canRead()){
                    String caminhoDocumento = root + nomePasta + "/" + nomeArquivo;

                    /*Pdf pdfModel = new Pdf();
                    pdfModel.setCaminhoDocumento(caminhoDocumento);
                    pdfModel.setIdInspecao(i.getId());
                    PdfDao dao = new PdfDao(FinalizaActivity.this);
                    dao.inserir(pdfModel);
*/
                    Intent it = new Intent(TesteActivity.this, ExibePDFActivity.class);
                    it.putExtra("documento", caminhoDocumento);
                    startActivity(it);
                } else {
                    Toast.makeText(TesteActivity.this, "Não foi possível gerar o documento, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(TesteActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });
    }
}
