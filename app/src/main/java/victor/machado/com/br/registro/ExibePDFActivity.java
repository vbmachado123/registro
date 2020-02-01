package victor.machado.com.br.registro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.PdfResponsabilidade;

public class ExibePDFActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_pdf);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                //Permissao negada, solicitando-a
                String[] permissoes = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissoes, STORAGE_CODE);

            }else{
                //Permissao aceita
                salvaPdf();
            }
        }else{
            //Versao do android anterior ao Marshmallow
            salvaPdf();
        }

    }

    private void salvaPdf() {

        PdfResponsabilidade pdf = new PdfResponsabilidade(); //Recuperando o texto para o pdf
        Document mDoc = new Document(); //Criando o objeto doc
        String nomeDoc = "Temporario";

        String caminhoPDF = Environment.getExternalStorageState() + "/" + nomeDoc + ".pdf";

        try{
            //Criando a instancia do documento
            PdfWriter.getInstance(mDoc, new FileOutputStream(caminhoPDF));
            //Abrindo-o para a escrita
            mDoc.open();

            mDoc.addTitle(pdf.getTituloPagina());
            mDoc.add(new Paragraph(pdf.getDocNaoApresentado()));

            mDoc.close();

            Toast.makeText(this, "Documento gerado", Toast.LENGTH_SHORT).show();
        }catch(Exception e){

            Toast.makeText(this, "Não foi possível gerar o pdf, tente novamente", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this, "Erro ao Salvar Arquivo", Toast.LENGTH_SHORT).show();
                }
        }

    }

    //Método para criar a Pasta do App e salvar o arquivo nela
    private void salvarDocumento(String nomeDocumento) throws IOException {

        //Criar a pasta do arquivo.
        File folder = new File(Environment.getExternalStorageDirectory() + "/Registro");
        if(folder.exists()){
            folder.mkdir();
        }
        String nomeArquivo = "Endereço";
        File arquivo = new File(Environment.getExternalStorageDirectory() + "/Registro/" + nomeArquivo);
        try{
            FileOutputStream salvar = new FileOutputStream(arquivo);
            //salvar.write(temporatio.getBytes());
            salvar.close();

            Toast.makeText(this, "Arquivo gerado com sucesso..!", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "Arquivo não encontrado!", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Erro!", Toast.LENGTH_SHORT).show();
        }
    }
}
