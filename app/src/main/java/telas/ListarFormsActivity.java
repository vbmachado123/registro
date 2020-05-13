package telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import helper.PdfDAO;
import model.Formulario;
import helper.FormularioAdapter;
import helper.FormularioDAO;
import model.Pdf;
import victor.machado.com.br.registro.R;

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
        toolbar.setTitle("Formulários");
        setSupportActionBar(toolbar);

        Toast.makeText(this, "Clique longo para abrir opções", Toast.LENGTH_LONG).show();

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

        FormularioAdapter adaptador = new FormularioAdapter(this, formulariosFiltrados);

        listView.setAdapter(adaptador);
        registerForContextMenu(listView); //Recuperando qual documento foi selecionado
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

            SearchView sv = (SearchView) menu.findItem(R.id.item_pesquisa).getActionView();

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    procuraFormulario(s);
                    return false;
                }
            });

        return true;
    }

    public void procuraFormulario(String texto){

        formulariosFiltrados.clear();

        for (Formulario f : formularios){

            if(f.getNome().toLowerCase().contains(texto.toLowerCase())){
                formulariosFiltrados.add(f);
            }
            if(f.getEndereco().toLowerCase().contains(texto.toLowerCase())){
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
                return true;
            case  R.id.item_sincroniza:
                Toast.makeText(this, "Botão Sincronizar Selecionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }

    public void excluir(MenuItem item){

        AdapterView.AdapterContextMenuInfo menuInfo =
                ( AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();

        final Formulario formExcluir = formulariosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o formulário?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        formulariosFiltrados.remove(formExcluir);
                        formularios.remove(formExcluir);
                        dao.excluir(formExcluir);
                        listView.invalidateViews();

                    }
                }).create();
        dialog.show();
    }

    public void atualizar(MenuItem item){

        AdapterView.AdapterContextMenuInfo menuInfo =
        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Formulario formAtualizar = formulariosFiltrados.get(menuInfo.position);
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("formulario", formAtualizar);
        startActivity(it);
    }

    public void visualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =
                ( AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();

        final Formulario inspecaoVisualizar = formulariosFiltrados.get(menuInfo.position);
        Pdf pdf = new PdfDAO(this).getById(inspecaoVisualizar.getId());

        if(pdf != null) {
            File file = new File(pdf.getCaminhoPdf());
            if(file.exists()) {
                Intent it = new Intent(ListarFormsActivity.this, ExibePDFActivity.class);
                it.putExtra("documento", pdf.getCaminhoPdf());
                startActivity(it);
            } else Toast.makeText(this, "Documento indisponível", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Documento indisponível", Toast.LENGTH_SHORT).show();
    }
}
