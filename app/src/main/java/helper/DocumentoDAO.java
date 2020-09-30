package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Documento;
import model.Empresa;
import model.Formulario;
import sql.Conexao;

public class DocumentoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Documento documento = null;

    public DocumentoDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Documento recupera() {
        Cursor cursor = banco.rawQuery("SELECT * FROM documento", null);

        while (cursor.moveToNext()) {
            documento = new Documento();
            documento.setId(cursor.getInt(0));
            documento.setIdFormulario(cursor.getInt(1));
            documento.setTipo(cursor.getString(2));
            documento.setDocumento(cursor.getString(3));
        }
        return documento;
    }

    public long inserir(Documento documento) {

        ContentValues values = new ContentValues();
        values.put("idFormulario", documento.getIdFormulario());
        values.put("tipo", documento.getTipo());
        values.put("documento", documento.getDocumento());

        return banco.insert("documento", null, values);
    }

    public Documento getById(int id) {
        Cursor cursor = banco.rawQuery("SELECT * FROM documento WHERE idFormulario =" + id, null);

        if (cursor.moveToFirst()) {
            documento = new Documento();
            documento.setId(cursor.getInt(0));
            documento.setIdFormulario(cursor.getInt(1));
            documento.setTipo(cursor.getString(2));
            documento.setDocumento(cursor.getString(3));
        }
        return documento;
    }

    public List<Documento> obterTodosIdForm(int idForm) {

        List<Documento> documentos = new ArrayList<>();
        Cursor cursor = banco.query("documento", new String[]{"id", "idFormulario", "tipo", "documento"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(1) == idForm) {

                documento = new Documento();
                documento.setId(cursor.getInt(0));
                documento.setIdFormulario(cursor.getInt(1));
                documento.setTipo(cursor.getString(2));
                documento.setDocumento(cursor.getString(3));

                documentos.add(documento);
            }
        }
        return documentos;
    }


    public void limparBanco() {
        banco.execSQL("DELETE FROM documento");
    }

}
