package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.CampoTermo;
import model.Documento;
import model.Imagem;
import sql.Conexao;

import java.util.ArrayList;
import java.util.List;

public class CampoTermoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private CampoTermo campoTermo;

    public CampoTermoDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public CampoTermo recupera() {
        Cursor cursor = banco.rawQuery("SELECT * FROM campoTermo", null);

        while (cursor.moveToNext()) {
            campoTermo = new CampoTermo();
            campoTermo.setId(cursor.getInt(0));
            campoTermo.setIdTermo(cursor.getInt(1));
            campoTermo.setPosicao(cursor.getInt(2));
            campoTermo.setDescricao(cursor.getString(3));
        }
        return campoTermo;
    }

    public long inserir(CampoTermo campoTermo) {

        ContentValues values = new ContentValues();
        values.put("idTermo", campoTermo.getIdTermo());
        values.put("posicao", campoTermo.getPosicao());
        values.put("descricao", campoTermo.getDescricao());

        return banco.insert("campoTermo", null, values);
    }

    public CampoTermo getById(int id) {
        Cursor cursor = banco.rawQuery("SELECT * FROM campoTermo WHERE idTermo =" + id, null);

        if (cursor.moveToFirst()) {
            campoTermo = new CampoTermo();
            campoTermo.setId(cursor.getInt(0));
            campoTermo.setIdTermo(cursor.getInt(1));
            campoTermo.setPosicao(cursor.getInt(2));
            campoTermo.setDescricao(cursor.getString(3));
        }
        return campoTermo;
    }

    public List<String> obterTodosIdTermo(int idTermo) {
        List<String> campos = new ArrayList<>();

        Cursor cursor = banco.query("campoTermo", new String[]{"id", "idTermo", "posicao", "descricao"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            campoTermo = new CampoTermo();
            campoTermo.setId(cursor.getInt(0));
            campoTermo.setIdTermo(cursor.getInt(1));
            campoTermo.setPosicao(cursor.getInt(2));
            campoTermo.setDescricao(cursor.getString(3));

            campos.add(campoTermo.getDescricao());
        }

        return campos;
    }

    public void limparBanco() {
        banco.execSQL("DELETE FROM campoTermo");
    }

}
