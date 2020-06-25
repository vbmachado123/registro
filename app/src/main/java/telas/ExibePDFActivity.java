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
import android.view.View;
import android.view.WindowManager;
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

import static androidx.core.content.FileProvider.getUriForFile;

public class ExibePDFActivity extends AppCompatActivity {

    private PDFView view;
    private FloatingActionButton fabEnviar;
    private File file;
    private Uri uri;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        fabEnviar = (FloatingActionButton) findViewById(R.id.fabEnviar);
        fabEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    enviarArquivoM();
                else
                    enviarArquivo();
            }
        });
    }


    private void enviarArquivoM() {
        Intent enviar = new Intent();
        uri = getUriForFile(
                this, "victor.machado.com.br.registro", file);
        enviar.setAction(Intent.ACTION_SEND);
        enviar.putExtra(Intent.EXTRA_STREAM, uri);
        enviar.setType("application/pdf");
        startActivity(Intent.createChooser(enviar, "Enviar documento via..."));
    }

    private void enviarArquivo() {
        Intent enviar = new Intent();
        uri = Uri.fromFile(file);
        if(uri != null){
            enviar.setAction(Intent.ACTION_SEND);
            enviar.putExtra(Intent.EXTRA_STREAM, uri);
            enviar.setType("application/pdf");
            startActivity(Intent.createChooser(enviar, "Enviar documento via..."));
        } else Toast.makeText(this, "Não foi possível compartilhar o arquivo", Toast.LENGTH_SHORT).show();
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
                acessaActivity(HomeActivity.class);
                return true;
            case R.id.item_home:
                acessaActivity(HomeActivity.class);
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
