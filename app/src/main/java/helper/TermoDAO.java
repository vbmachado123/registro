package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.CampoTermo;
import model.Formulario;
import model.Termo;
import sql.Conexao;

public class TermoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Termo termo;

    public TermoDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Termo recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM termo", null);

        while (cursor.moveToNext()){
            termo = new Termo();
            termo.setId(cursor.getInt(0));
            termo.setTitulo(cursor.getString(1));
            termo.setCategoria(cursor.getInt(2));
        }
        return termo;
    }

    public long inserir(Termo termo) {

        ContentValues values = new ContentValues();
        values.put("titulo", termo.getTitulo());
        values.put("categoria", termo.getCategoria());

        return banco.insert("termo", null, values);
    }

    public Termo getByTitle(String titulo){
        Cursor cursor = banco.rawQuery("SELECT * FROM termo WHERE titulo =" + titulo, null);

        if (cursor.moveToFirst()){
            termo = new Termo();
            termo.setId(cursor.getInt(0));
            termo.setTitulo(cursor.getString(1));
            termo.setCategoria(cursor.getInt(2));
        }
        return termo;
    }

    public Termo getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM termo WHERE id =" + id, null);

        if (cursor.moveToFirst()){
            termo = new Termo();
            termo.setId(cursor.getInt(0));
            termo.setTitulo(cursor.getString(1));
            termo.setCategoria(cursor.getInt(2));
        }
        return termo;
    }

    public List<Termo> obterTodos() {

        List<Termo> termos = new ArrayList<>();
        Cursor cursor = banco.query("termo", new String[]{"id", "titulo", "categoria"},
                null, null, null, null, null);

        //if(cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                termo = new Termo();
                termo.setId(cursor.getInt(0));
                termo.setTitulo(cursor.getString(1));
                termo.setCategoria(cursor.getInt(2));

                Log.i("Termo", "Titulo: " + termo.getTitulo());

                termos.add(termo);
            }
        //}
        return termos;
    }
    public void limparBanco(){
        banco.execSQL("DELETE FROM termo");
    }

}
