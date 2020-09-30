package util;

public class AtualizacoesBanco {

// o modelo de atualização que é para ser passado no metodo onUpdate do        tabledefinition
// das tabelas na classe SthBancoOpenHelper é esse abaixo
// TABELA , CAMPO , TIPO , VALOR DEFAULT

    public static String VER_1[] = {"configuracao", "caminhoImagem", "TEXT", ""};


    /*AO CRIAR O CAMPO COM A VERSÃO ACIMA, VOCÊ DEVERÁ INFORMAR LOGO ABAIXO NO MÉTODO
    SELECTSTRING O CASE DA VERSÃO, NO CASO DO MÉTODO ELE SEMPRE IRÁ RETORNAR INICIANDO DA
    VERSÃO 1 POR CONTA DE QUE A PRIMEIRA VERSÃO PUBLICA COM AS ALTERAÇÕES
    */
    public static String[] selectScript(int ver) {
        switch (ver) {
            case 1:
                return VER_1;
            default:
                return null;
        }
    }
}

