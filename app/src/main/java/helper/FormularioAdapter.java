package helper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import model.Formulario;
import victor.machado.com.br.registro.R;

/**
 * Created by victor on 02/02/20.
 */

public class FormularioAdapter extends BaseAdapter {

    private List<Formulario> formulario;
    private Activity activity;

    public FormularioAdapter(Activity activity, List<Formulario> formulario) {

        this.activity = activity;
        this.formulario = formulario;

    }

    @Override
    public int getCount() {
        return formulario.size();
    }

    @Override
    public Object getItem(int i) {

        return formulario.get(i);
    }

    @Override
    public long getItemId(int i) {

        return formulario.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = activity.getLayoutInflater().inflate(R.layout.lista_documentos, parent, false);
        TextView endereco = v.findViewById(R.id.txtEndereco);
        TextView nome = v.findViewById(R.id.txtNome);

        Formulario f = formulario.get(i);

        endereco.setText(f.getEndereco());
        nome.setText(f.getNome());

        return v;

    }
}
