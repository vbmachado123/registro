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

        String sql = "SELECT * FROM documento WHERE id =" + formID;

        Cursor cursor = banco.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            String endereco = cursor.getString(cursor.getColumnIndex("endereco"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            String rg = cursor.getString(cursor.getColumnIndex("rg"));
            String cpf = cursor.getString(cursor.getColumnIndex("cpf"));
            String opcaoEscolhida = cursor.getString(cursor.getColumnIndex("responsabilidade"));
            String caminhoFoto = cursor.getString(cursor.getColumnIndex("caminhoImagem"));
            formulario = new Formulario();

            formulario.setId(formID);
            formulario.setEndereco(endereco);
            formulario.setNome(nome);
            formulario.setRg(rg);
            formulario.setCpf(cpf);
            formulario.setResponsabilidade(opcaoEscolhida);
            formulario.setCaminhoImagem(caminhoFoto);
        }

        return formulario;
    }

    public Formulario recupera() {

        Cursor cursor = banco.rawQuery("SELECT * FROM documento", null);

        while (cursor.moveToNext()) {
            int idInspecao = cursor.getInt(cursor.getColumnIndex("id"));
            String endereco = cursor.getString(cursor.getColumnIndex("endereco"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            String rg = cursor.getString(cursor.getColumnIndex("rg"));
            String cpf = cursor.getString(cursor.getColumnIndex("cpf"));
            String opcaoEscolhida = cursor.getString(cursor.getColumnIndex("responsabilidade"));
            String caminhoFoto = cursor.getString(cursor.getColumnIndex("caminhoImagem"));
            formulario = new Formulario();

            formulario.setId(idInspecao);
            formulario.setEndereco(endereco);
            formulario.setNome(nome);
            formulario.setRg(rg);
            formulario.setCpf(cpf);
            formulario.setResponsabilidade(opcaoEscolhida);
            formulario.setCaminhoImagem(caminhoFoto);
        }

        return formulario;
    }

    public long inserir(Formulario formulario) {

        ContentValues values = new ContentValues();
        values.put("endereco", formulario.getEndereco());
        values.put("nome", formulario.getNome());
        values.put("rg", formulario.getRg());
        values.put("cpf", formulario.getCpf());
        values.put("celular", formulario.getCelular());
        values.put("responsabilidade", formulario.getResponsabilidade());
        values.put("caminhoImagem", formulario.getCaminhoImagem());

        return banco.insert("documento", null, values);
    }

    public List<Formulario> obterTodos() {

        List<Formulario> formularios = new ArrayList<>();
        Cursor cursor = banco.query("documento", new String[]{"id", "endereco", "nome", "rg", "cpf",
                "celular", "responsabilidade", "caminhoImagem"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Formulario f = new Formulario();
            f.setId(cursor.getInt(0));
            f.setEndereco(cursor.getString(1));
            f.setNome(cursor.getString(2));
            f.setRg(cursor.getString(3));
            f.setCpf(cursor.getString(4));
            f.setCelular(cursor.getString(5));
            f.setResponsabilidade(cursor.getString(6));
            f.setCaminhoImagem(cursor.getString(7));

            formularios.add(f);
        }
        return formularios;
    }

    public void excluir(Formulario f) {
        banco.delete("documento", "id = ?",
                new String[]{f.getId().toString()});
    }

    public void atualizar(Formulario formulario) {

        ContentValues values = new ContentValues();
        values.put("endereco", formulario.getEndereco());
        values.put("nome", formulario.getNome());
        values.put("rg", formulario.getRg());
        values.put("cpf", formulario.getCpf());
        values.put("celular", formulario.getCelular());
        values.put("responsabilidade", formulario.getResponsabilidade());
        values.put("caminhoImagem", formulario.getCaminhoImagem());

        banco.update("documento", values, "id = ?",
                new String[]{formulario.getId().toString()});
    }

    public boolean exportarTabela() {
        boolean exportado = false;
        Cursor cursor = banco.rawQuery("SELECT * FROM documento", null);
        Csv csv = new Csv(cursor);
        File f = csv.exportDB();
        if (f.canRead())
            exportado = true;

        return exportado;
    }

    public void limparBanco() {
        banco.execSQL("DELETE FROM documento");
    }
}
