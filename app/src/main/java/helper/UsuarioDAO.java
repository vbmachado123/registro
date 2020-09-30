package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Configuracao;
import model.Usuario;
import sql.Conexao;

public class UsuarioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Usuario usuario = null;

    public UsuarioDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Usuario recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM usuario", null);

        while (cursor.moveToNext()){
            usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setIdEmpresa(cursor.getInt(1));
            usuario.setIdImagem(cursor.getInt(2));
            usuario.setNome(cursor.getString(3));
            usuario.setEmail(cursor.getString(4));
            usuario.setSenha(cursor.getString(5));
        }
        return usuario;
    }

    public long inserir(Usuario usuario) {

        ContentValues values = new ContentValues();
        values.put("idEmpresa", usuario.getIdEmpresa());
        values.put("idImagem", usuario.getIdImagem());
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        return banco.insert("usuario", null, values);
    }

    public void atualizar(Usuario usuario) {

        ContentValues values = new ContentValues();
        values.put("idEmpresa", usuario.getIdEmpresa());
        values.put("idImagem", usuario.getIdImagem());
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());
        banco.update("usuario", values, "id = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void limparBanco(){
        banco.execSQL("DELETE FROM usuario");
    }

}
