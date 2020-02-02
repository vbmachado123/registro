package victor.machado.com.br.registro;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_docs);
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);

        //Floatting Button
        floatingActionButton = (FloatingActionButton) findViewById(R.id.botaoAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListarFormsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.lista_Formularios);
        dao = new FormularioDAO(this);

        formularios = dao.obterTodos();
        formulariosFiltrados.addAll(formularios);

        ArrayAdapter<Formulario> adaptador = new ArrayAdapter<Formulario>(
                             this, R.layout.lista_documentos, formulariosFiltrados);
        listView.setAdapter(adaptador);
    }

    @Override
    public void onResume(){

            super.onResume();
            formularios = dao.obterTodos();
            formulariosFiltrados.clear();
            formulariosFiltrados.addAll(formularios);
            listView.invalidateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);

        try{

            SearchView sv = (SearchView) menu.findItem(R.id.item_pesquisa).getActionView();

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    System.out.println("Digitou: " + s);

                    procuraFormulario(s);
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Não foi possivel realizar procura", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public void procuraFormulario(String texto){

        formulariosFiltrados.clear();

        for (Formulario f : formularios){

            if(f.getNome().toLowerCase().contains(texto.toLowerCase())){
                formulariosFiltrados.add(f);
            }
        }
        listView.invalidateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {
            case R.id.itemSair:
                Toast.makeText(this, "Botão Sair Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemConfiguracoes:
                Intent i = new Intent(ListarFormsActivity.this, ConfiguracoesActivity.class);
                startActivity(i);
                Toast.makeText(this, "Botão Configurações Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_pesquisa:
                pesquisar();
                return true;
            case  R.id.item_sincroniza:
                Toast.makeText(this, "Botão Sincronizar Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pesquisar(){

    }
}
