package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sql.Conexao;
import model.Formulario;
import util.Csv;

/**
 * Created by victor on 01/02/20.
 */

public class FormularioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private Formulario formulario = null;

    public FormularioDAO(Context context) {
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public Formulario pegaForm(int formID) {

        String sql = "SELECT * FROM formulario WHERE id =" + formID;

        Cursor cursor = banco.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            formulario = new Formulario();

            formulario.setId(cursor.getInt(0));
            formulario.setEndereco(cursor.getString(1));
            formulario.setNome(cursor.getString(2));
            formulario.setCelular(cursor.getString(3));
        }

        return formulario;
    }

    public Formulario recupera() {

        Cursor cursor = banco.rawQuery("SELECT * FROM formulario", null);

        while (cursor.moveToNext()) {
            int idInspecao = cursor.getInt(cursor.getColumnIndex("id"));
            String endereco = cursor.getString(cursor.getColumnIndex("endereco"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            formulario = new Formulario();

            formulario.setId(cursor.getInt(0));
            formulario.setEndereco(cursor.getString(1));
            formulario.setNome(cursor.getString(2));
            formulario.setCelular(cursor.getString(3));
            formulario.setIdTermoSelecionado(cursor.getInt(4));
        }

        return formulario;
    }

    public long inserir(Formulario formulario) {

        ContentValues values = new ContentValues();
        values.put("endereco", formulario.getEndereco());
        values.put("nome", formulario.getNome());
        values.put("celular", formulario.getCelular());

        return banco.insert("formulario", null, values);
    }

    public List<Formulario> obterTodos() {

        List<Formulario> formularios = new ArrayList<>();
        Cursor cursor = banco.query("formularios", new String[]{"id", "endereco", "nome", "celular"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Formulario f = new Formulario();
            f.setId(cursor.getInt(0));
            f.setEndereco(cursor.getString(1));
            f.setNome(cursor.getString(2));
            f.setCelular(cursor.getString(3));

            formularios.add(f);
        }
        return formularios;
    }

    public void excluir(Formulario f) {
        banco.delete("formularios", "id = ?",
                new String[]{f.getId().toString()});
    }

    public void atualizar(Formulario formulario) {

        ContentValues values = new ContentValues();
        values.put("endereco", formulario.getEndereco());
        values.put("nome", formulario.getNome());
        values.put("celular", formulario.getCelular());

        banco.update("formularios", values, "id = ?",
                new String[]{formulario.getId().toString()});
    }

    public boolean exportarTabela() {
        boolean exportado = false;
        Cursor cursor = banco.rawQuery("SELECT * FROM formularios", null);
        Csv csv = new Csv(cursor);
        File f = csv.exportDB();
        if (f.canRead())
            exportado = true;

        return exportado;
    }

    public void limparBanco() {
        banco.execSQL("DELETE FROM formularios");
    }
}
