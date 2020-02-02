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
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.Formulario;
import helper.FormularioDAO;
import model.PdfResponsabilidade;

public class ExibePDFActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    private FormularioDAO dao;
    private Formulario f = new Formulario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_pdf);

        Toast.makeText(this, f.getNome(), Toast.LENGTH_SHORT).show();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                    PackageManager.PERMISSION_DENIED){
                //Permissao negada, solicitando-a
                String[] permissoes = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissoes, STORAGE_CODE);

            }else{
                //Permissao aceita
                //salvaPdf();
            }
        }else{
            //Versao do android anterior ao Marshmallow
            //salvaPdf();
        }

    }

    private void salvaPdf() {

        PdfResponsabilidade pdf = new PdfResponsabilidade(); //Recuperando o texto para o pdf
        Document documento = new Document(); //Cria o obj do doc

        String caminhoPDF = Environment.getExternalStorageDirectory() + "/" + f.getEndereco() + ".pdf";

        try{
            //Criando a instância para escrever o doc
            PdfWriter.getInstance(documento, new FileOutputStream(caminhoPDF));
            //Abrindo o doc para a edição/leitura
            documento.open();

            //Inserindo as informações
            Paragraph titulo =  new Paragraph(pdf.getTituloPagina(), pdf.getTituloFonte());
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Phrase(pdf.getqLinha())); //LINHA EM BRANCO
            documento.add(new Phrase(pdf.getTextoInteressado(), pdf.getCorpoFonte()));
            documento.add(new Phrase(pdf.getqLinha())); //LINHA EM BRANCO

            Paragraph paragraph = new Paragraph(pdf.getTextoEndereco() + f.getEndereco(), pdf.getCorpoFonte());
            paragraph.setAlignment(Element.ALIGN_LEFT);
            documento.add(paragraph);
            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            documento.add(new Phrase(pdf.getE(), pdf.getCorpoFonte()));
            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            if (f.getResponsabilidade() == "Proprietário") {
                documento.add(new Phrase(pdf.getOpcaoMarcada()
                        + pdf.getTextoOpcaoProprietario(), pdf.getCorpoFonte()));
                documento.add(new Phrase("\n" + pdf.getOpcaoDesmarcada()
                        + pdf.getTextoOpcaoMoradorResponsavel(), pdf.getCorpoFonte()));
            }
            else {
                    documento.add(new Phrase( pdf.getOpcaoDesmarcada()
                            + pdf.getTextoOpcaoProprietario() , pdf.getCorpoFonte()));
                    documento.add(new Phrase("\n" + pdf.getOpcaoMarcada()
                            + pdf.getTextoOpcaoMoradorResponsavel() , pdf.getCorpoFonte()));
                }

            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            //Texto meio
            documento.add(new Phrase(pdf.getTextoMeioNeg1(), pdf.getCorpoFonteBold()));
            documento.add(new Phrase(pdf.getTextoMeioNormal(), pdf.getCorpoFonte()));
            documento.add(new Phrase(pdf.getTextoMeioNeg2(), pdf.getCorpoFonteBold()));

            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            documento.add(new Paragraph(pdf.getDocNaoApresentado(), pdf.getCorpoFonte()));
            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO
            documento.add(new Paragraph(pdf.getDocIPTU(), pdf.getCorpoFonte()));

            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            documento.add(new Phrase(pdf.getNomeResponsavel() + f.getNome() + "\n", pdf.getCorpoFonte()));
            documento.add(new Phrase(pdf.getRgRNE() + f.getRg() + "\n", pdf.getCorpoFonte()));
            documento.add(new Phrase(pdf.getCpf() + f.getCpf() + "\n", pdf.getCorpoFonte()));

            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            documento.add(new Phrase(pdf.getInfObrigatoria(), pdf.getCorpoFonte()));

            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO
            documento.add(new Paragraph(pdf.getqLinha()));//LINHA EM BRANCO

            documento.add(new Phrase(pdf.getAssinaturaResponsavel(), pdf.getCorpoFonte()));

            documento.close();

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
