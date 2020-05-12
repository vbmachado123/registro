package util;

import android.app.Activity;
import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

import helper.FormularioDAO;
import model.Formulario;
import model.PdfResponsabilidade;

public class GeraPDF {
    private FormularioDAO dao;
    private Formulario f = null;
    /* private Pdf pdf = null;
    *  private PdfDao pDao; */

    public Document GeraPDF(String destino, Activity activity) throws Exception{
        PdfResponsabilidade pdf = new PdfResponsabilidade(); //Recuperando o texto para o pdf
        Document documento = new Document(); //Cria o obj do doc

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String nomePasta = root + "/Registro";
        String caminhoAssinatura =  nomePasta + "/Imagens/" + "Assinatura" + ".jpg";
        String caminhoDocumento = nomePasta + "/Imagens/" + "Documento" + ".jpg";

        f = new FormularioDAO(activity).recupera();

        //Criando a instância para escrever o doc
        PdfWriter.getInstance(documento, new FileOutputStream(destino));
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

        Image assinatura = Image.getInstance(caminhoAssinatura);
        assinatura.scalePercent(12, 04);
        assinatura.setAbsolutePosition(50, 335); //400y MEIO DA FOLHA
        documento.add(assinatura);

        int x = 8, y = 10;

        Image imagemDoc = Image.getInstance(caminhoDocumento);
        imagemDoc.scalePercent (x, y);
        imagemDoc.setAbsolutePosition(100,90);
        documento.add(imagemDoc);

        documento.close();

        return documento;
    }
}
