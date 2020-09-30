package model;

import java.util.Arrays;
import java.util.List;

public class TermoConfig {

    private int id;
    private int idTermo;
    private String[] opcoes;
    private String opcoesConvertidas;
    private String[] documentosNecessarios;
    private String documentosNecessariosConvertido;
    private int unicaEscolha = 0;
    private int quantidadeFotos;
    private int exibeAssinaturaFiscal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTermo() {
        return idTermo;
    }

    public void setIdTermo(int idTermo) {
        this.idTermo = idTermo;
    }

    public String[] getOpcoes() {

        opcoes = convertStringToList(opcoesConvertidas).toArray(new String[0]);
        return opcoes;
    }

    public void setOpcoes(String[] opcoes) {
        opcoesConvertidas = convertListToString(opcoes);
        this.opcoes = opcoes;
    }

    public int getUnicaEscolha() {
        return unicaEscolha;
    }

    public void setUnicaEscolha(int unicaEscolha) {
        this.unicaEscolha = unicaEscolha;
    }

    public int getQuantidadeFotos() {
        return quantidadeFotos;
    }

    public void setQuantidadeFotos(int quantidadeFotos) {
        this.quantidadeFotos = quantidadeFotos;
    }

    public int getExibeAssinaturaFiscal() {
        return exibeAssinaturaFiscal;
    }

    public void setExibeAssinaturaFiscal(int exibeAssinaturaFiscal) {
        this.exibeAssinaturaFiscal = exibeAssinaturaFiscal;
    }

    public String getOpcoesConvertidas() {
        opcoesConvertidas = convertListToString(opcoes);
        return opcoesConvertidas;
    }

    public String[] getDocumentosNecessarios() {
        opcoes = convertStringToList(documentosNecessariosConvertido).toArray(new String[0]);
        return documentosNecessarios;
    }

    public void setDocumentosNecessarios(String[] documentosNecessarios) {
        opcoesConvertidas = convertListToString(documentosNecessarios);
        this.documentosNecessarios = documentosNecessarios;
    }

    public String getDocumentosNecessariosConvertido() {
        documentosNecessariosConvertido = convertListToString(documentosNecessarios);
        return documentosNecessariosConvertido;
    }

    public void setDocumentosNecessariosConvertido(String documentosNecessariosConvertido) {
        documentosNecessarios = convertStringToList(documentosNecessariosConvertido).toArray(new String[0]);
        this.documentosNecessariosConvertido = documentosNecessariosConvertido;
    }

    public void setOpcoesConvertidas(String opcoesConvertidas) {
        opcoes = convertStringToList(opcoesConvertidas).toArray(new String[0]);
        this.opcoesConvertidas = opcoesConvertidas;
    }

    private static final String LIST_SEPARATOR = "__,__";

    public static String convertListToString(String[] stringList) {
        StringBuffer stringBuilder = new StringBuffer();
        for (String str : stringList) {
            stringBuilder.append(str).append(LIST_SEPARATOR);
        }

        // Remove last separator
        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());

        return stringBuilder.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }
}
