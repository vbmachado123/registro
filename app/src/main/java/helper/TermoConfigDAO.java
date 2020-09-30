package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import model.Termo;
import model.TermoConfig;
import sql.Conexao;

public class TermoConfigDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;
    private TermoConfig termoConfig;

    public TermoConfigDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
        banco = conexao.getReadableDatabase();
    }

    public TermoConfig recupera(){
        Cursor cursor = banco.rawQuery("SELECT * FROM termoConfig", null);

        while (cursor.moveToNext()){
            termoConfig = new TermoConfig();
            termoConfig.setId(cursor.getInt(0));
            termoConfig.setIdTermo(cursor.getInt(1));
            termoConfig.setOpcoesConvertidas(cursor.getString(2));
            termoConfig.setUnicaEscolha(cursor.getInt(3));
            termoConfig.setQuantidadeFotos(cursor.getInt(4));
            termoConfig.setExibeAssinaturaFiscal(cursor.getInt(5));
            termoConfig.setDocumentosNecessariosConvertido(cursor.getString(6));

        }

        return termoConfig;
    }

    public long inserir(TermoConfig termoConfig) {

        ContentValues values = new ContentValues();
        values.put("idTermo", termoConfig.getIdTermo());
        values.put("opcoes", termoConfig.getOpcoesConvertidas());
        values.put("unicaEscolha", termoConfig.getUnicaEscolha());
        values.put("quantidadeFotos", termoConfig.getQuantidadeFotos());
        values.put("exibeAssinaturaFiscal", termoConfig.getExibeAssinaturaFiscal());
        values.put("documentosNecessarios", termoConfig.getDocumentosNecessariosConvertido());

        return banco.insert("termoConfig", null, values);
    }

    public TermoConfig getById(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM termoConfig WHERE id =" + id, null);

        if (cursor.moveToFirst()){
            termoConfig = new TermoConfig();
            termoConfig.setId(cursor.getInt(0));
            termoConfig.setIdTermo(cursor.getInt(1));
            termoConfig.setOpcoesConvertidas(cursor.getString(2));
            termoConfig.setUnicaEscolha(cursor.getInt(3));
            termoConfig.setQuantidadeFotos(cursor.getInt(4));
            termoConfig.setExibeAssinaturaFiscal(cursor.getInt(5));
            termoConfig.setDocumentosNecessariosConvertido(cursor.getString(6));

        }
        return termoConfig;
    }

    public TermoConfig getByIdTermo(int id){
        Cursor cursor = banco.rawQuery("SELECT * FROM termoConfig WHERE idTermo =" + id, null);

        if (cursor.moveToFirst()){
            termoConfig = new TermoConfig();
            termoConfig.setId(cursor.getInt(0));
            termoConfig.setIdTermo(cursor.getInt(1));
            termoConfig.setOpcoesConvertidas(cursor.getString(2));
            termoConfig.setUnicaEscolha(cursor.getInt(3));
            termoConfig.setQuantidadeFotos(cursor.getInt(4));
            termoConfig.setExibeAssinaturaFiscal(cursor.getInt(5));
            termoConfig.setDocumentosNecessariosConvertido(cursor.getString(6));

        }
        return termoConfig;
    }

    public void limparBanco(){
        banco.execSQL("DELETE FROM termoConfig");
    }
}
