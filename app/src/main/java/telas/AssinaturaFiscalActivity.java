package telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itextpdf.text.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import helper.FormularioDAO;
import helper.PdfDAO;
import model.Assinatura;
import model.Formulario;
import model.Pdf;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.GeraPDF;
import victor.machado.com.br.registro.R;

public class AssinaturaFiscalActivity extends AppCompatActivity {

    private Assinatura assinatura;
    private Button salvar;
    private Toolbar toolbar;
    private String caminho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assinatura Fiscal");
        setSupportActionBar(toolbar);

        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.rlAssinatura);
        assinatura = new Assinatura(this);
        parent.addView(assinatura);

        salvar = (Button) findViewById(R.id.salvar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.setDrawingCacheEnabled(true);
                Bitmap b = parent.getDrawingCache();

                FileOutputStream out = null;
                File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
                if (mydir.exists()) mydir.mkdir();
                caminho = mydir + "/Imagens/" + "AssinaturaFiscal" + ".jpg";

                try {
                    out = new FileOutputStream(caminho);
                    b.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    out.flush();
                    out.close();
                    Toast.makeText(AssinaturaFiscalActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(AssinaturaFiscalActivity.this, MainActivity.class);
                    startActivity(it);
                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_limpar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_limpar:
                assinatura.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}