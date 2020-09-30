package model;

import com.itextpdf.text.Font;

/**
 * Created by victor on 01/02/20.
 */

public class PdfResponsabilidade {

    //Textos
    private String tituloPagina = "TERMO DE RESPONSABILIDADE PELO IMÓVEL";
    private String textoInteressado = "O interessado, ao final assinado e qualificado, declara, sob as penas da lei, que, em relação ao imóvel localizado no endereço\n" +
            "abaixo:";

    private String textoEndereco = "Endereço: ";

    private String e = "É";

    private String opcaoMarcada = "( X )";
    private String opcaoDesmarcada = "(...)";

    private String textoOpcaoProprietario = "Proprietário e detentor da posse direta do imóvel";
    private String textoOpcaoMoradorResponsavel = "Morador e responsável pelo imóvel em referência estando apto a solicitar ligação de água e/ou esgotos para tal imóvel";

    private String textoMeioNeg1 = "<Declara, ainda, estar ciente de que será responsável pelo pagamento das conta/faturas mensais de consumo de água e/ou\n" +
            "esgoto";

    private String textoMeioNormal = "e, que, caso deixe de ser responsável pelo imóvel, é responsável por entrar em contato com a Sabesp para solicitar o\n" +
            "encerramento da relação contratual que se inicia com o pedido de ligação de água e/ou esgotos.";

    private String textoMeioNeg2 = "<Que recebeu o contrato de\n" +
            "adesão nesta data e está ciente da obrigatoridade de manter o cadastro atualizado conforme clausula quinta - deveres do\n" +
            "usuário - item 5.1.5 'Informar corretamente e manter sempre atualizados os dados cadastrais'.";

    private String docNaoApresentado = "Documentos não apresentados";
    private String docIPTU = "Documento comprobatório da propriedade ou da posse do imóvel: escritura pública ou matrícula do registro do imóvel ou\n" +
            "carnê IPTU ou contrato particular de compra e venda ou contrato de locação.";

    private String nomeResponsavel = "Nome do responsável: ";
    private String rgRNE = "RG/RNE(*): ";
    private String cpf = "CPF: ";

    private String infObrigatoria = "(*) = Informação obrigatória de acordo com a Deliberação ARSESP 106/09. Na falta de RG/RNE informar, obrigatoriamente,\n" +
            "outro documento de identificação equivalente com foto como CNH, carteira de Conselho Profissional, etc.";

    private String assinaturaResponsavel = "Assinatura do interessado";

    private String qLinha = "  "; //Para não ficar bagunçado

    //Fontes
    private Font tituloFonte = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD); //Fonte p/ o titulo em NEGRITO
    private Font corpoFonte = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL); //Fonte p/ o texto normal
    private Font corpoFonteBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD); //Fonte para o corpo em NEGRITO

    public Font getTituloFonte() {
        return tituloFonte;
    }

    public Font getCorpoFonte() {
        return corpoFonte;
    }

    public Font getCorpoFonteBold() {
        return corpoFonteBold;
    }

    public String getTituloPagina() {
        return tituloPagina;
    }

    public String getTextoInteressado() {
        return textoInteressado;
    }

    public String getTextoEndereco() {
        return textoEndereco;
    }

    public String getE() {
        return e;
    }

    public String getOpcaoMarcada() {
        return opcaoMarcada;
    }

    public String getOpcaoDesmarcada() {
        return opcaoDesmarcada;
    }

    public String getTextoOpcaoProprietario() {
        return textoOpcaoProprietario;
    }

    public String getTextoOpcaoMoradorResponsavel() {
        return textoOpcaoMoradorResponsavel;
    }

    public String getTextoMeioNeg1() {
        return textoMeioNeg1;
    }

    public String getTextoMeioNormal() {
        return textoMeioNormal;
    }

    public String getTextoMeioNeg2() {
        return textoMeioNeg2;
    }

    public String getDocNaoApresentado() {
        return docNaoApresentado;
    }

    public String getDocIPTU() {
        return docIPTU;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public String getRgRNE() {
        return rgRNE;
    }

    public String getCpf() {
        return cpf;
    }

    public String getInfObrigatoria() {
        return infObrigatoria;
    }

    public String getAssinaturaResponsavel() {
        return assinaturaResponsavel;
    }

    public String getqLinha() {return qLinha;}

    public String[] getAllCampos() {
        String[] campos = {
                getTextoInteressado(), "Campo_Endereco", getE(), "Campo_Selecionado", ">" + getTextoOpcaoProprietario(),
                ">" + getTextoOpcaoMoradorResponsavel(), getTextoMeioNeg1(), getTextoMeioNormal(),
                getTextoMeioNeg2(), getDocNaoApresentado(), getDocIPTU(),"Campo_Nome", "Campo_RG", "Campo_CPF", getInfObrigatoria()};

        return campos;
    }
}
