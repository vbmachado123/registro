package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Conexao;
import model.Formulario;

/**
 * Created by victor on 01/02/20.
 */

public class FormularioDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public FormularioDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public Formulario pegaForm(int formID) {

        Formulario formulario = null;

        banco = conexao.getReadableDatabase();

        String sql = "SELECT * FROM documentos WHERE id =" + formID;

        Cursor cursor = banco.rawQuery(sql, null);

        if(cursor.moveToFirst()){

            String endereco = cursor.getString(cursor.getColumnIndex("endereco"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            String rg = cursor.getString(cursor.getColumnIndex("rg"));
            String cpf = cursor.getString(cursor.getColumnIndex("cpf"));
            String opcaoEscolhida = cursor.getString(cursor.getColumnIndex("responsabilidade"));

            formulario = new Formulario();

            formulario.setId(formID);
            formulario.setEndereco(endereco);
            formulario.setNome(nome);
            formulario.setRg(rg);
            formulario.setCpf(cpf);
            formulario.setResponsabilidade(opcaoEscolhida);
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

       return banco.insert("documentos", null, values);
    }

    public List<Formulario> obterTodos(){

        List<Formulario> formularios = new ArrayList<>();
        Cursor cursor = banco.query("documentos", new String[]{"id", "endereco", "nome", "rg", "cpf",
                                        "celular", "responsabilidade"}, null, null, null, null, null);

        while(cursor.moveToNext()){
            Formulario f = new Formulario();
            f.setId(cursor.getInt(0));
            f.setEndereco(cursor.getString(1));
            f.setNome(cursor.getString(2));
            f.setRg(cursor.getString(3));
            f.setCpf(cursor.getString(4));
            f.setCelular(cursor.getString(5));
            f.setResponsabilidade(cursor.getString(6));

            formularios.add(f);
        }
        return formularios;
    }

    public void excluir(Formulario f){
        banco.delete("documentos", "id = ?",
                new String[]{f.getId().toString()} );
    }

    public void atualizar(Formulario formulario) {

        ContentValues values = new ContentValues();
        values.put("endereco", formulario.getEndereco());
        values.put("nome", formulario.getNome());
        values.put("rg", formulario.getRg());
        values.put("cpf", formulario.getCpf());
        values.put("celular", formulario.getCelular());
        values.put("responsabilidade", formulario.getResponsabilidade());

        banco.update("documentos", values, "id = ?",
                new String[]{formulario.getId().toString()});
    }
}
