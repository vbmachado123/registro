package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Empresa;
import model.Pdf;
import sql.Conexao;

public class EmpresaDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Empresa empresa = null;

    public EmpresaDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public  Empresa recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM empresa", null);

        while (cursor.moveToNext()){
            empresa = new Empresa();
            empresa.setId(cursor.getInt(0));
            empresa.setNome(cursor.getString(1));
            empresa.setIdImagem(cursor.getInt(2));
            empresa.setCnpj(cursor.getString(3));
        }
        return empresa;
    }

    public long inserir(Empresa empresa) {

        ContentValues values = new ContentValues();
        values.put("nome", empresa.getNome());
        values.put("idImagem", empresa.getIdImagem());
        values.put("cnpj", empresa.getCnpj());

        return banco.insert("empresa", null, values);
    }

    public Empresa getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM empresa WHERE nome =" + id, null);

        if (cursor.moveToFirst()){
            empresa = new Empresa();
            empresa.setId(cursor.getInt(0));
            empresa.setNome(cursor.getString(1));
            empresa.setIdImagem(cursor.getInt(2));
            empresa.setCnpj(cursor.getString(3));
        }
        return empresa;
    }

    public void limparBanco(){
        banco.execSQL("DELETE FROM empresa");
    }

}
