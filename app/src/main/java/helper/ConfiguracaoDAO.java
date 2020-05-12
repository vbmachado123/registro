package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Configuracao;

import sql.Conexao;

public class ConfiguracaoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Configuracao configuracao = null;

    public ConfiguracaoDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Configuracao recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM configuracao", null);

        while (cursor.moveToNext()){
            configuracao = new Configuracao();
            configuracao.setId(cursor.getInt(0));
            configuracao.setEmail(cursor.getString(1));
            configuracao.setNome(cursor.getString(2));
            configuracao.setExibeAssinatura(cursor.getInt(3));
            configuracao.setCaminhoPasta(cursor.getString(4));
        }
        return configuracao;
    }

    public long inserir(Configuracao configuracao) {

        ContentValues values = new ContentValues();
        values.put("email", configuracao.getEmail());
        values.put("nome", configuracao.getNome());
        values.put("exibeAssinatura", configuracao.getExibeAssinatura());
        values.put("caminhoPasta", configuracao.getCaminhoPasta());

        return banco.insert("configuracao", null, values);
    }

    public void atualizar(Configuracao configuracao) {

        ContentValues values = new ContentValues();
        values.put("email", configuracao.getEmail());
        values.put("nome", configuracao.getNome());
        values.put("exibeAssinatura", configuracao.getExibeAssinatura());
        values.put("caminhoPasta", configuracao.getCaminhoPasta());

        banco.update("configuracao", values, "id = ?",
                new String[]{String.valueOf(configuracao.getId())});
    }

}
