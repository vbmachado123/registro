package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import model.Pdf;
import sql.Conexao;

public class PdfDAO {
    private Conexao conexao;
    private SQLiteDatabase banco;
    private Pdf pdf = null;

    public PdfDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }



}
