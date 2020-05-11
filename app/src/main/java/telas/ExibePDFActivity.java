package telas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.Formulario;
import helper.FormularioDAO;
import model.PdfResponsabilidade;
import victor.machado.com.br.registro.R;

public class ExibePDFActivity extends AppCompatActivity {


    private PDFView view;
    private FloatingActionButton fabEnviar;
    private File file;
    private Uri uri;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_pdf);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Documento Inspeção");
        setSupportActionBar(toolbar);

        //Recuperando as informações do cliente
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        view = (PDFView) findViewById(R.id.pdfview);

        /*Log.i("Doc", extras.getString("documento"));*/

        file = new File(extras.getString("documento"));

        view.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exibe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_salvar:
                Toast.makeText(this, "Documento salvo em: " + file, Toast.LENGTH_SHORT).show();
                acessaActivity(MainActivity.class);
                return true;
            case R.id.item_home:
                acessaActivity(MainActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void acessaActivity(Class c){
        Intent it = new Intent(ExibePDFActivity.this, c);
        startActivity(it);
    }

}
