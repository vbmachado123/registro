package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Configuracao;
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

    public Pdf recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM pdf", null);

        while (cursor.moveToNext()){
            pdf = new Pdf();
            pdf.setId(cursor.getInt(0));
            pdf.setIdFormulario(cursor.getInt(1));
            pdf.setCaminhoPdf(cursor.getString(2));
            pdf.setDataHora(cursor.getString(3));
            pdf.setIdentificacao(cursor.getString(4));
        }
        return pdf;
    }

    public long inserir(Pdf pdf) {

        ContentValues values = new ContentValues();
        values.put("idInspecao", pdf.getIdFormulario());
        values.put("caminhoPdf", pdf.getCaminhoPdf());
        values.put("dataHora", pdf.getDataHora());
        values.put("identificacao", pdf.getIdentificacao());

        return banco.insert("pdf", null, values);
    }

    public Pdf getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM pdf WHERE idInspecao =" + id, null);

        if (cursor.moveToFirst()){
            pdf = new Pdf();
            pdf.setIdFormulario(cursor.getInt(1));
            pdf.setCaminhoPdf(cursor.getString(2));
            pdf.setDataHora(cursor.getString(3));
            pdf.setIdentificacao(cursor.getString(4));
        }
        return pdf;
    }

    public void limparBanco(){
        banco.execSQL("DELETE FROM pdf");
    }

}
