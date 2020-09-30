package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

import util.AtualizacoesBanco;

/**
 * Created by victor on 01/02/20.
 */
public class Conexao extends SQLiteOpenHelper {

    private static final String name = "banco.db";
    private static final int version = 2;

    public Conexao(Context context) {
        super(context, name, null, version);
    }

    private String tabelaFormulario = "CREATE TABLE IF NOT EXISTS formulario(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "endereco TEXT, nome TEXT, celular TEXT)";

    private String tabelaPdf = "CREATE TABLE IF NOT EXISTS pdf (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idInspecao INTEGER, caminhoPdf TEXT, dataHora TEXT, identificacao TEXT)";

    private String tabelaDocumento = "CREATE TABLE IF NOT EXISTS documento(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idFormulario INTEGER, tipo INTEGER, documento TEXT)";

    private String tabelaImagem = "CREATE TABLE IF NOT EXISTS imagem(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tipo INTEGER, formulario  INTEGER, idUsuario INTEGER, idFormulario INTEGER, caminho TEXT)";

    private String tabelaResponsabilidade = "CREATE TABLE IF NOT EXISTS responsabilidade(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idFormulario INTEGER, responsabilidade TEXT, selecionado INTEGER)";

    private String tabelaEmpresa = "CREATE TABLE IF NOT EXISTS empresa(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT, idImagem INTEGER, cnpj TEXT)";

    private String tabelaUsuario = "CREATE TABLE IF NOT EXISTS usuario(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idEmpresa INTEGER, idImagem INTEGER, nome TEXT, email TEXT, senha TEXT)";

    private String tabelaTermo = "CREATE TABLE IF NOT EXISTS termo(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo TEXT, categoria INTEGER)";

    private String tabelaCampoTermo = "CREATE TABLE IF NOT EXISTS campoTermo(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idTermo INTEGER, posicao INTEGER, descricao TEXT)";

    private String tabelaTermoConfig = "CREATE TABLE IF NOT EXISTS termoConfig(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idTermo INTEGER, opcoes TEXT, unicaEscolha INTEGER, quantidadeFotos INTEGER, exibeAssinaturaFiscal INTEGER, documentosNecessarios TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tabelaFormulario);
        db.execSQL(tabelaPdf);
        db.execSQL(tabelaResponsabilidade);
        db.execSQL(tabelaDocumento);
        db.execSQL(tabelaImagem);
        db.execSQL(tabelaEmpresa);
        db.execSQL(tabelaUsuario);
        db.execSQL(tabelaTermo);
        db.execSQL(tabelaCampoTermo);
        db.execSQL(tabelaTermoConfig);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*db.execSQL(tabelaFormulario);
        db.execSQL(tabelaPdf);
        db.execSQL(tabelaConfiguracao);
        db.execSQL(insereColunaImagem);*/

        String vers[] = null;

        /* AQUI É ONDE OCORRE A MÁGICA PARA ATUALIZAÇÃO DO BANCO, AO ESCREVER O CÓDIGO
         * DO BANCO VI QUE AO ATUALIZAR A VERSÃO DO BANCO, DE ACRODO COM
         * TODOS AS ORIENTAÇÕES, FAZ O DROP DA TABELA ATUALIZADA E DEPOIS RECRIAVA ELA,
         * FIZ A ALTERAÇÃO PARA QUE AO MODIFICAR A VERSÃO DO BANCO,
         * O DESENVOLVEDOR POSSA PASSAR OS DADOS CONTIDOS NA CLASSE ATUALIZACOESBANCO E
         * COM ESSES DADOS POSSA REALIZAR A ATUALIZAÇÃO */

        try {

            /* ELE PEGA A ÚLTIMA VERSÃO DO BANCO E COMPARA COM A NOVA, DE ACORDO COM A
             * COMPARAÇÃO ELE VAI NA CLASSE ATUALIZACOESBANCO E COLETA OS SCRIPTS
             * DAS VERSÕES QUE ESTÃO PENDENTES E EXECUTA UM A UM */

            for (int i = oldVersion + 1; i <= newVersion; i++) {
                vers = new AtualizacoesBanco().selectScript(i);

            /*você pode utilizar qualquer table defitions para realizar
              esse procedimento*/

                updateTabelasBanco(db, vers[0], vers[1],
                        vers[2], vers[3]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTabelasBanco(SQLiteDatabase db, String table,
                                          String column, String typ, String valor) {
        try {
            db.execSQL("ALTER TABLE " + table + " ADD " + column + " " + typ);
            if (valor != "") {
                db.execSQL("update " + table + " set " + column + " = '" + valor + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
