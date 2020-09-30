package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import java.util.ArrayList;

import model.Documento;
import model.Imagem;
import sql.Conexao;

public class ImagemDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Imagem imagem = null;

    public ImagemDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Imagem recupera() {
        Cursor cursor = banco.rawQuery("SELECT * FROM imagem", null);

        while (cursor.moveToNext()) {
            imagem = new Imagem();
            imagem.setId(cursor.getInt(0));
            imagem.setTipo(cursor.getString(1));
            imagem.setFormulario(cursor.getInt(2));
            imagem.setIdUsuario(cursor.getInt(3));
            imagem.setIdFormulario(cursor.getInt(4));
            imagem.setCaminho(cursor.getString(5));
        }
        return imagem;
    }

    public long inserir(Imagem imagem) {

        ContentValues values = new ContentValues();
        values.put("idFormulario", imagem.getIdFormulario());
        values.put("idUsuario", imagem.getIdUsuario());
        values.put("tipo", imagem.getTipo());
        values.put("formulario", imagem.getFormulario());
        values.put("caminho", imagem.getCaminho());

        return banco.insert("imagem", null, values);
    }

    public Imagem getById(int id) {
        Cursor cursor = banco.rawQuery("SELECT * FROM imagem WHERE idFormulario =" + id, null);

        if (cursor.moveToFirst()) {
            imagem = new Imagem();
            imagem.setId(cursor.getInt(0));
            imagem.setTipo(cursor.getString(1));
            imagem.setFormulario(cursor.getInt(2));
            imagem.setIdUsuario(cursor.getInt(3));
            imagem.setIdFormulario(cursor.getInt(4));
            imagem.setCaminho(cursor.getString(5));
        }
        return imagem;
    }

    public List<Imagem> obterTodosIdForm(int idForm) {

        List<Imagem> imagens = new ArrayList<>();
        Cursor cursor = banco.query("imagem", new String[]{"id", "tipo", "formulario", "idUsuario", "idFormulario", "caminho"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(5) == idForm) {
                imagem = new Imagem();
                imagem.setId(cursor.getInt(0));
                imagem.setTipo(cursor.getString(1));
                imagem.setFormulario(cursor.getInt(2));
                imagem.setIdUsuario(cursor.getInt(3));
                imagem.setIdFormulario(cursor.getInt(4));
                imagem.setCaminho(cursor.getString(5));
                imagens.add(imagem);
            }

        }
        return imagens;
    }

    public void limparBanco() {
        banco.execSQL("DELETE FROM imagem");
    }

}
