package util;

import com.itextpdf.text.Font;

public class PDFComum {

    private String textoEndereco = "Endereço: ";

    private String opcaoMarcada = "( X )";
    private String opcaoDesmarcada = "(...)";

    private String nomeResponsavel = "Nome do responsável: ";
    private String rgRNE = "RG/RNE(*): ";
    private String cpf = "CPF(*): ";
    private String cnpj = "CNPJ(*): ";
    private String ie = "INSCRIÇÃO ESTADUAL(*): ";
    private String im = "INSCRIÇÃO MUNICIPAL(*): ";
    private String ccm = "CCM: ";
    private String cnh = "CNH(*): ";
    private String celular = "CELULAR: ";

    private String infObrigatoria = "(*) = Informação obrigatória de acordo com a Deliberação ARSESP 106/09. Na falta de RG/RNE informar, obrigatoriamente,\n" +
            "outro documento de identificação equivalente com foto como CNH, carteira de Conselho Profissional, etc.";

    private String assinaturaResponsavel = "Assinatura do interessado";
    private String assinaturaFiscal = "Assinatura do fiscal";

    private String qLinha = "  "; //Para não ficar bagunçado

    //Fontes
    private Font tituloFonte = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD); //Fonte p/ o titulo em NEGRITO
    private Font corpoFonte = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL); //Fonte p/ o texto normal
    private Font corpoFonteBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD); //Fonte para o corpo em NEGRITO

    public String getTextoEndereco() {
        return textoEndereco;
    }

    public void setTextoEndereco(String textoEndereco) {
        this.textoEndereco = textoEndereco;
    }

    public String getOpcaoMarcada() {
        return opcaoMarcada;
    }

    public void setOpcaoMarcada(String opcaoMarcada) {
        this.opcaoMarcada = opcaoMarcada;
    }

    public String getOpcaoDesmarcada() {
        return opcaoDesmarcada;
    }

    public void setOpcaoDesmarcada(String opcaoDesmarcada) {
        this.opcaoDesmarcada = opcaoDesmarcada;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getRgRNE() {
        return rgRNE;
    }

    public void setRgRNE(String rgRNE) {
        this.rgRNE = rgRNE;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getCcm() {
        return ccm;
    }

    public void setCcm(String ccm) {
        this.ccm = ccm;
    }

    public String getAssinaturaResponsavel() {
        return assinaturaResponsavel;
    }

    public void setAssinaturaResponsavel(String assinaturaResponsavel) {
        this.assinaturaResponsavel = assinaturaResponsavel;
    }

    public String getqLinha() {
        return qLinha;
    }

    public void setqLinha(String qLinha) {
        this.qLinha = qLinha;
    }

    public Font getTituloFonte() {
        return tituloFonte;
    }

    public void setTituloFonte(Font tituloFonte) {
        this.tituloFonte = tituloFonte;
    }

    public Font getCorpoFonte() {
        return corpoFonte;
    }

    public void setCorpoFonte(Font corpoFonte) {
        this.corpoFonte = corpoFonte;
    }

    public Font getCorpoFonteBold() {
        return corpoFonteBold;
    }

    public void setCorpoFonteBold(Font corpoFonteBold) {
        this.corpoFonteBold = corpoFonteBold;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getInfObrigatoria() {
        return infObrigatoria;
    }

    public void setInfObrigatoria(String infObrigatoria) {
        this.infObrigatoria = infObrigatoria;
    }

    public String getAssinaturaFiscal() {
        return assinaturaFiscal;
    }

    public void setAssinaturaFiscal(String assinaturaFiscal) {
        this.assinaturaFiscal = assinaturaFiscal;
    }
}
