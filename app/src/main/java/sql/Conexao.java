package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by victor on 01/02/20.
 */
public class Conexao extends SQLiteOpenHelper {

    private static final String name = "banco.db";
    private static final int version = 1;

    public Conexao(Context context) {
        super(context, name, null, version);
    }

    private String tabelaFormulario = "CREATE TABLE IF NOT EXISTS documento(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "endereco TEXT, nome TEXT, rg TEXT, cpf TEXT, " +
            "celular TEXT, responsabilidade TEXT)";

    private String tabelaPdf = "CREATE TABLE IF NOT EXISTS pdf (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "idInspecao INTEGER, caminhoPdf TEXT)";

    private String tabelaConfiguracao = "CREATE TABLE IF NOT EXISTS configuracao (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "endereco TEXT, loginServidor TEXT, senhaServidor TEXT, exibeAssinatura TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tabelaFormulario);
        db.execSQL(tabelaPdf);
        db.execSQL(tabelaConfiguracao);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(tabelaFormulario);
        db.execSQL(tabelaPdf);
        db.execSQL(tabelaConfiguracao);
    }
}
