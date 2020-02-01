package victor.machado.com.br.registro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import model.Formulario;
import model.FormularioDAO;

public class ListarFormsActivity extends AppCompatActivity {

    private ListView listView;
    private FormularioDAO dao;
    private List<Formulario> formularios;
    private List<Formulario> formulariosFiltrados = new ArrayList<>();

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_docs);

        listView = (ListView) findViewById(R.id.lista_Formularios);
        dao = new FormularioDAO(this);

        formularios = dao.obterTodos();
        formulariosFiltrados.addAll(formularios);

        ArrayAdapter<Formulario> adaptador = new ArrayAdapter<Formulario>(
                this, android.R.layout.simple_list_item_1, formularios);
        listView.setAdapter(adaptador);

    }
}
