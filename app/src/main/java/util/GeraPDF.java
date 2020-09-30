package util;

import android.app.Activity;
import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.List;
import java.io.FileOutputStream;

import helper.CampoTermoDAO;
import helper.DocumentoDAO;
import helper.FormularioDAO;
import helper.ImagemDAO;
import helper.TermoConfigDAO;
import helper.TermoDAO;
import model.CampoTermo;
import model.Documento;
import model.Formulario;
import model.Imagem;
import model.PdfResponsabilidade;
import model.Termo;
import model.TermoConfig;

public class GeraPDF {
    private FormularioDAO dao;
    private Formulario f = null;
    /* private Pdf pdf = null;
     *  private PdfDao pDao; */

    public Document GeraPDF(String destino, Activity activity) throws Exception {

        /* CARREGANDO INFORMAÇÕES */
        PdfResponsabilidade pdf = new PdfResponsabilidade(); //Recuperando o texto para o pdf
        Rectangle pagesize = new Rectangle(PageSize.A4);
        Document documento = new Document(pagesize); //Cria o obj do doc
        PDFComum comum = new PDFComum();

        Termo termo = new Termo();
        TermoConfig termoConfig = new TermoConfig();
        CampoTermo campoTermo = new CampoTermo();
        Documento doc = new Documento();
        Imagem imagem = new Imagem();

        TermoDAO termoDAO = new TermoDAO(activity);
        TermoConfigDAO configDAO = new TermoConfigDAO(activity);
        CampoTermoDAO campoTermoDAO = new CampoTermoDAO(activity);
        DocumentoDAO documentoDAO = new DocumentoDAO(activity);
        ImagemDAO imagemDAO = new ImagemDAO(activity);

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String nomePasta = root + "/Registro";
        String caminhoAssinatura = nomePasta + "/Imagens/" + "Assinatura" + ".jpg";

        f = new FormularioDAO(activity).recupera();

        List<Imagem> imagens = imagemDAO.obterTodosIdForm(f.getId());
        List<Documento> documentos = documentoDAO.obterTodosIdForm(f.getId());

        Preferencias preferencias = new Preferencias(activity);
        termo = termoDAO.getById(preferencias.getTermo());

        termoConfig = configDAO.getByIdTermo(termo.getId());
        List<String> campos = campoTermoDAO.obterTodosIdTermo(termo.getId());

        String caminhoDocumento = f.getCaminhoImagem();

        //Criando a instância para escrever o doc
        PdfWriter.getInstance(documento, new FileOutputStream(destino));

        //Abrindo o doc para a edição/leitura
        documento.open();

        /* INSERINDO INFORMAÇÕES */
        Paragraph titulo = new Paragraph(termo.getTitulo(), pdf.getTituloFonte());
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        int tamanho = campos.size() + 1;
        for (int i = 0; i < tamanho; i++) {
            Paragraph paragraph;

            String[] verifica = campos.get(i).split("_");
            if (verifica.length > 0) { /* Informação variável, validação */
                String campo = "";
                switch (verifica[1]) {
                    case "Endereco":
                        campo = comum.getTextoEndereco() + f.getEndereco();
                        break;
                    case "Nome":
                        campo = comum.getNomeResponsavel() + f.getNome();
                        break;
                    case "Celular":
                        campo = comum.getCelular() + f.getCelular();
                        break;
                    case "RG":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "RG") {
                                campo = comum.getRgRNE() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "CPF":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "CPF") {
                                campo = comum.getCpf() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "CNH":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "CNH") {
                                campo = comum.getCnh() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "CNPJ":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "CNPJ") {
                                campo = comum.getCnpj() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "IE":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "IE") {
                                campo = comum.getIe() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "IM":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "IM") {
                                campo = comum.getIm() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "CCM":
                        for (Documento docSalvo : documentos) {
                            if (docSalvo.getTipo() == "CCM") {
                                campo = comum.getCcm() + docSalvo.getDocumento();
                                break;
                            }
                        }
                        break;
                    case "Selecionado": //Opções Selecionadas

                        /* SELECIONADO
                         * Recuperar a unicaEscolha
                         * Opções do documento (contem ">" no começo)
                         * opção(oes) selecionada
                         * comparar a opção selecionada com a do documento
                         *  */
                        break;
                }

                paragraph = new Paragraph(campo, comum.getCorpoFonte());
                //documento.add(paragraph);
            } else { /* Não é uma das variáveis, insere normal */
                if (campos.get(i).contains("<")) { /* Negrito (utilizando o '<' porque o '*' não é aceito, por ser caracter reservado do java) */
                    String campoFinal = campos.get(i).replaceAll("<", "");
                    paragraph = new Paragraph(campoFinal, comum.getCorpoFonteBold());
                    //documento.add(paragraph);
                } else { /* Texto normal */
                    paragraph = new Paragraph(campos.get(i), comum.getCorpoFonte());
                }
            } /* Padrão */
            documento.add(paragraph);
        }

        /* ANTIGO */
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
        } else {
            documento.add(new Phrase(pdf.getOpcaoDesmarcada()
                    + pdf.getTextoOpcaoProprietario(), pdf.getCorpoFonte()));
            documento.add(new Phrase("\n" + pdf.getOpcaoMarcada()
                    + pdf.getTextoOpcaoMoradorResponsavel(), pdf.getCorpoFonte()));
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

        int x = 10, y = 6;

        Image imagemDoc = Image.getInstance(caminhoDocumento);
        imagemDoc.scalePercent(x, y);
        imagemDoc.setAbsolutePosition(50, 50);
        documento.add(imagemDoc);

        documento.close();

        return documento;
    }
}
