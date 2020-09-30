package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Imagem;
import model.Responsabilidade;
import sql.Conexao;

public class ResponsabilidadeDAO {


    private Conexao conexao;
    private SQLiteDatabase banco;
    private Responsabilidade responsabilidade = null;

    public ResponsabilidadeDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public  Responsabilidade recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM responsabilidade", null);

        while (cursor.moveToNext()){
            responsabilidade = new Responsabilidade();
            responsabilidade.setId(cursor.getInt(0));
            responsabilidade.setIdFormulario(cursor.getInt(1));
            responsabilidade.setResponsabilidade(cursor.getString(2));
            responsabilidade.setSelecionado(cursor.getInt(3));

        }
        return responsabilidade;
    }

    public long inserir(Responsabilidade responsabilidade) {

        ContentValues values = new ContentValues();
        values.put("idFormulario", responsabilidade.getIdFormulario());
        values.put("responsabilidade", responsabilidade.getResponsabilidade());
        values.put("selecionado", responsabilidade.getSelecionado());

        return banco.insert("responsabilidade", null, values);
    }

    public Responsabilidade getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM imagem WHERE responsabilidade =" + id, null);

        if (cursor.moveToFirst()){
            responsabilidade = new Responsabilidade();
            responsabilidade.setId(cursor.getInt(0));
            responsabilidade.setIdFormulario(cursor.getInt(1));
            responsabilidade.setResponsabilidade(cursor.getString(2));
            responsabilidade.setSelecionado(cursor.getInt(3));
        }
        return responsabilidade;
    }

    public void limparBanco(){
        banco.execSQL("DELETE FROM responsabilidade");
    }


}
