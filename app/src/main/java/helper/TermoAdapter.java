package helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.Termo;
import victor.machado.com.br.registro.R;

public class TermoAdapter extends BaseAdapter {
    private List<Termo> termo;
    private Activity activity;

    public TermoAdapter(Activity activity, ArrayList<Termo> termo) {
        this.activity = activity;
        this.termo = termo;
    }

    @Override
    public int getCount() {
        return termo.size();
    }

    @Override
    public Object getItem(int i) {
        return termo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return termo.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.lista_termos, parent, false);
        TextView titulo = v.findViewById(R.id.txtTitulo);
        TextView categoria = v.findViewById(R.id.txtCategoria);

        Termo t = termo.get(i);
        String categoriaConvertida = "";

        titulo.setText(t.getTitulo());

        switch (t.getCategoria()){

            /* CATEGORIAS
             * 0 - CADASTRO
             * 1 - VISTORIA
             * 2 - COBRANÇAS
             * 3 - IRREGULARIDADES */

            case 0:
                categoriaConvertida = "Cadastro";
                break;
            case 1:
                categoriaConvertida = "Vistoria";
                break;
            case 2:
                categoriaConvertida = "Cobrança";
                break;
            case 3:
                categoriaConvertida = "Irregularidade";
                break;
        }

        categoria.setText(categoriaConvertida);

        return v;
    }
}
